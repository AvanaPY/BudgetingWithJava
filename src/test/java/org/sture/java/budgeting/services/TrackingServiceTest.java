package org.sture.java.budgeting.services;

import org.sture.java.budgeting.dto.TrackingEntryDTOConverter;
import org.sture.java.budgeting.exceptions.InvalidEntryException;
import org.sture.java.budgeting.models.BudgetType;
import org.sture.java.budgeting.models.TrackingEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrackingServiceTest {
    private TrackingService trackingService;
    private TrackingEntryStoreService storeService;

    @BeforeEach
    void setUp() {
        TrackingEntryDTOConverter dtoFactory = new TrackingEntryDTOConverter();
        storeService = new TrackingEntryStoreService(dtoFactory);
        storeService.DeleteStoreIfExists();
        trackingService = new TrackingService(storeService);
        trackingService.initialize();
    }

    @AfterEach
    void tearDown() {
        trackingService = null;
        storeService.DeleteStoreIfExists();
        storeService = null;
    }

    @Test
    void TestAddNewValidEntry()
    {
        var date = LocalDate.of(2024, 10, 22);
        var type = BudgetType.Income;
        var category = "Employment";
        var details  = "";
        double amount = 100;

        trackingService.addNewEntry(date, type, category, details, amount);
        assertEquals(1, trackingService.GetTrackingEntryList().size());
    }

    @Test
    void TestDeleteEntry() {
        var date = LocalDate.of(2024, 10, 22);
        var type = BudgetType.Income;
        var category = "Employment";
        var details  = "";
        double amount = 100;

        trackingService.addNewEntry(date, type, category, details, amount);
        assertEquals(1, trackingService.GetTrackingEntryList().size());

        trackingService.DeleteTrackingEntry(trackingService.GetTrackingEntryList().getFirst());
        assertEquals(0, trackingService.GetTrackingEntryList().size());
    }

    @Test
    void TestAddMultipleIncomeEntriesCheckIfBalanceIsCorrect() {
        var date = LocalDate.of(2024, 10, 22);
        var type = BudgetType.Income;
        var category = "Employment";
        var details  = "";
        double amount = 100;

        // Expected balance is 100
        trackingService.addNewEntry(date, type, category, details, amount);
        assertEquals(100, trackingService.GetTrackingEntryList().getFirst().getBalance());

        // 200
        trackingService.addNewEntry(date, type, category, details, amount);
        assertEquals(200, trackingService.GetTrackingEntryList().getFirst().getBalance());

        // 250
        trackingService.addNewEntry(date, type, category, details, 50d);
        assertEquals(250, trackingService.GetTrackingEntryList().getFirst().getBalance());
    }

    @Test
    void TestAddIncomeEntryThenExpenseEntryCheckIfBalanceIsCorrect(){
        var date = LocalDate.of(2024, 10, 22);

        trackingService.addNewEntry(date, BudgetType.Income, "Employment", "", 100d);
        assertEquals(100, trackingService.GetTrackingEntryList().getFirst().getBalance());

        trackingService.addNewEntry(date, BudgetType.Expense, "Rent", "", 80d);
        assertEquals(20, trackingService.GetTrackingEntryList().getFirst().getBalance());
    }

    @Test
    void TestAddNewEntryAmountCannotBeNegativeOrZero() {
        var date = LocalDate.of(2024, 10, 22);

        assertThrows(
                InvalidEntryException.class,
                () -> trackingService.addNewEntry(date, BudgetType.Income, "Employment", "", 0d)
        );

        assertThrows(
                InvalidEntryException.class,
                () -> trackingService.addNewEntry(date, BudgetType.Income, "Employment", "", -1d)
        );

        trackingService.addNewEntry(date, BudgetType.Income, "Employment", "", 1d);
        assertEquals(1, trackingService.GetTrackingEntryList().size());
    }

    @Test
    void TestToAddInvalidCategoryForBudgetTypes(){
        var date = LocalDate.of(2024, 10, 22);

        var types = trackingService.GetBudgetTypeList();
        for(BudgetType type : types)
        {
            assertThrows(
                    InvalidEntryException.class,
                    () -> trackingService.addNewEntry(date, type, "invalidcategory", "", 0d)
            );
        }
    }

    @Test
    void TestCategoriesChangeWhenChangingBudgetType(){
        BudgetType initialBudgetType = BudgetType.Income;
        BudgetType secondBudgetType = BudgetType.Expense;

        trackingService.UpdateCategories(initialBudgetType);
        String[] initialCategories = trackingService.GetCategoryList().toArray(new String[0]);

        trackingService.UpdateCategories(secondBudgetType);
        String[] secondCategories = trackingService.GetCategoryList().toArray(new String[0]);

        trackingService.UpdateCategories(initialBudgetType);
        String[] thirdCategories = trackingService.GetCategoryList().toArray(new String[0]);

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

        trackingService.addNewEntry(date1, BudgetType.Income, "Employment", "", 1d);
        trackingService.addNewEntry(date2, BudgetType.Income, "Employment", "", 1d);
        assertDoesNotThrow(() -> assertListIsSorted(trackingService.GetTrackingEntryList()), "List is not sorted.");
        trackingService.addNewEntry(date3, BudgetType.Income, "Employment", "", 1d);
        assertDoesNotThrow(() -> assertListIsSorted(trackingService.GetTrackingEntryList()), "List is not sorted.");
        trackingService.addNewEntry(date1, BudgetType.Income, "Employment", "", 1d);
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