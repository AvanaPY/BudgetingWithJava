package org.sture.java.budgeting.services;

import org.sture.java.budgeting.BaseTest;
import org.sture.java.budgeting.controller.statusbar.IStatusBarController;
import org.sture.java.budgeting.mock.controller.StatusBarControllerMock;
import org.sture.java.budgeting.services.category.CategoryService;
import org.sture.java.budgeting.services.job.BackgroundJobExecutionService;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.services.tracking.TrackingService;
import org.sture.java.budgeting.store.category.dto.CategoryDTOConverter;
import org.sture.java.budgeting.store.category.service.CategoryStoreService;
import org.sture.java.budgeting.store.tracking.dto.TrackingEntryDTOConverter;
import org.sture.java.budgeting.exceptions.InvalidEntryException;
import org.sture.java.budgeting.services.tracking.models.TrackingEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sture.java.budgeting.store.tracking.service.TrackingEntryStoreService;
import org.sture.java.budgeting.utils.TestHarness;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrackingServiceTest extends BaseTest {
    private TrackingService trackingService;
    private TrackingEntryStoreService storeService;

    private CategoryStoreService categoryStoreService;
    private CategoryService categoryService;

    private final LocalDate date = LocalDate.of(2024, 10, 22);
    private final String details  = "";
    private final double amount = 100;
    private BudgetEntryCategory category;
    private BudgetEntryCategory category2;
    private BudgetEntryCategory subCategory;
    private BudgetEntryCategory subCategory2;

    private final BudgetEntryCategory[] budgetEntryCategories = new BudgetEntryCategory[]{
            new BudgetEntryCategory("Income", true,
                    new BudgetEntryCategory[] {
                            new BudgetEntryCategory("Employment"),
                            new BudgetEntryCategory("CSN")
                    }),
            new BudgetEntryCategory("Expense", false,
                    new BudgetEntryCategory[] {
                            new BudgetEntryCategory("Rent"),
                            new BudgetEntryCategory("Bills")
                    })
        };

    @BeforeEach
    void setUp() {
        // Setup mocks
        IStatusBarController statusBarControllerMock = new StatusBarControllerMock();

        // Setup necessary services
        BackgroundJobExecutionService jes = new BackgroundJobExecutionService(statusBarControllerMock);

        TrackingEntryDTOConverter dtoFactory = new TrackingEntryDTOConverter();
        storeService = new TrackingEntryStoreService(
                dtoFactory,
                jes);

        CategoryDTOConverter categoryDtoFactory = new CategoryDTOConverter();
        categoryStoreService = new CategoryStoreService(categoryDtoFactory, jes);
        categoryService = new CategoryService(categoryStoreService);
        categoryService.FromTestOverrideCategories(budgetEntryCategories);

        category = categoryService.GenerateAllBudgetCategories()[0];
        subCategory = category.GetSubCategories()[0];
        category2 = categoryService.GenerateAllBudgetCategories()[1];
        subCategory2 = category2.GetSubCategories()[0];

        storeService.DeleteStoreIfExists();

        trackingService = new TrackingService(
                storeService,
                categoryService);
        trackingService.initialize();
    }

    @AfterEach
    void tearDown() {
        trackingService = null;
        storeService.DeleteStoreIfExists();
        storeService = null;
        categoryStoreService.DeleteStoreIfExists();
        categoryService = null;
        TestHarness.DeleteTestStoreDirectory();
    }

    @Test
    void TestAddNewValidEntry() {
        trackingService.addNewEntry(date, category, subCategory, details, amount);
        assertEquals(1, trackingService.GetTrackingEntryList().size());
    }

    @Test
    void TestDeleteEntry() {
        trackingService.addNewEntry(date, category, subCategory, details, amount);
        assertEquals(1, trackingService.GetTrackingEntryList().size());

        trackingService.DeleteTrackingEntry(trackingService.GetTrackingEntryList().getFirst());
        assertEquals(0, trackingService.GetTrackingEntryList().size());
    }

    @Test
    void TestAddMultipleIncomeEntriesCheckIfBalanceIsCorrect() {
        // Expected balance is 100
        trackingService.addNewEntry(date, category, subCategory, details, amount);
        assertEquals(100, trackingService.GetTrackingEntryList().getFirst().getBalance());

        // 200
        trackingService.addNewEntry(date, category, subCategory, details, amount);
        assertEquals(200, trackingService.GetTrackingEntryList().getFirst().getBalance());

        // 250
        trackingService.addNewEntry(date, category, subCategory, details, 50d);
        assertEquals(250, trackingService.GetTrackingEntryList().getFirst().getBalance());
    }

    @Test
    void TestAddIncomeEntryThenExpenseEntryCheckIfBalanceIsCorrect(){
        trackingService.addNewEntry(date, category, subCategory, "", 100d);
        assertEquals(100, trackingService.GetTrackingEntryList().getFirst().getBalance());

        trackingService.addNewEntry(date, category2, subCategory2, "", 80d);
        assertEquals(20, trackingService.GetTrackingEntryList().getFirst().getBalance());
    }

    @Test
    void TestAddNewEntryAmountCannotBeZero() {
        assertThrows(
                InvalidEntryException.class,
                () -> trackingService.addNewEntry(date, category, subCategory, details, 0d)
        );

        assertDoesNotThrow(
                () -> trackingService.addNewEntry(date, category, subCategory, details, -1d)
        );

        assertDoesNotThrow(
                () -> trackingService.addNewEntry(date, category, subCategory, details, 1d)
        );
    }

    @Test
    void TestToAddInvalidCategoryForBudgetTypes(){
        var date = LocalDate.of(2024, 10, 22);

        var categories = trackingService.GetBudgetCategoryList();
        for(BudgetEntryCategory category : categories)
        {
            assertThrows(
                    InvalidEntryException.class,
                    () -> trackingService.addNewEntry(date, category, new BudgetEntryCategory("Invalid"), details, amount)
            );
        }
    }

    @Test
    void TestCategoriesChangeWhenChangingBudgetType(){
        trackingService.UpdateCategories(category);
        BudgetEntryCategory[] initialCategories = trackingService.GetBudgetSubCategoryList().toArray(new BudgetEntryCategory[0]);

        trackingService.UpdateCategories(category2);
        BudgetEntryCategory[] secondCategories = trackingService.GetBudgetSubCategoryList().toArray(new BudgetEntryCategory[0]);

        trackingService.UpdateCategories(category);
        BudgetEntryCategory[] thirdCategories = trackingService.GetBudgetSubCategoryList().toArray(new BudgetEntryCategory[0]);

        assertThrows(
                Exception.class,
                () -> assertArraysEqual(initialCategories, secondCategories)
        );

        assertDoesNotThrow(
                () -> assertArraysEqual(initialCategories, thirdCategories)
        );
    }

    @Test
    void TestThatEntriesAreSortedByDateAfterAddingNewEntries(){
        var date1 = LocalDate.of(2024, 10, 22);
        var date2 = LocalDate.of(2024, 10, 23);
        var date3 = LocalDate.of(2024, 10, 19);

        trackingService.addNewEntry(date1, category, subCategory, details, amount);
        trackingService.addNewEntry(date2, category, subCategory, details, amount);
        assertDoesNotThrow(() -> assertListIsSorted(trackingService.GetTrackingEntryList()), "List is not sorted.");
        trackingService.addNewEntry(date3, category, subCategory, details, amount);
        assertDoesNotThrow(() -> assertListIsSorted(trackingService.GetTrackingEntryList()), "List is not sorted.");
        trackingService.addNewEntry(date1, category, subCategory, details, amount);
        assertDoesNotThrow(() -> assertListIsSorted(trackingService.GetTrackingEntryList()), "List is not sorted.");
    }

    private void assertListIsSorted(List<TrackingEntry> entries) throws Exception {
        for(int i = 0; i < entries.size() - 1; i++) {
            var date1 = entries.get(i).getDate();
            var date2 = entries.get(i+1).getDate();
            if(date1.isAfter(date2))
                throw new Exception("Found " + date1 + " before " + date2 + ", list should be sorted");
        }
    }

    private <T> void assertArraysEqual(T[] array1, T[] array2) throws Exception {
        if(array1.length != array2.length)
            throw new Exception("Array lengths differ");

        for(int i = 0; i < array1.length; i++)
            if(!array1[i].equals(array2[i]))
                throw new Exception("Arrays differ at index " + i + ": " + array1[i] + " != " + array2[i]);
    }
}