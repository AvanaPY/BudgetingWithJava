package org.sture.java.budgeting.services;

import org.sture.java.budgeting.exceptions.InvalidEntryException;
import org.sture.java.budgeting.models.BudgetEntryCategory;
import org.sture.java.budgeting.models.BudgetEntrySubCategory;
import org.sture.java.budgeting.models.TrackingEntry;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.sture.java.budgeting.store.TrackingEntryStoreService;

import java.time.LocalDate;
import java.util.*;

public class TrackingService {
    private final TrackingEntryStoreService storeService;
    private final ObservableList<TrackingEntry> trackingEntryObservableList;
    private final ObservableList<BudgetEntryCategory> budgetEntryCategoryObservableList;
    private final ObservableList<BudgetEntrySubCategory> budgetEntrySubCategoryObservableList;
    private final BudgetTypeCategoryProvider categoryProvider;

    public TrackingService(
            TrackingEntryStoreService storeService,
            BudgetTypeCategoryProvider categoryProvider) {
        this.storeService = storeService;
        this.categoryProvider = categoryProvider;
        trackingEntryObservableList = FXCollections.observableArrayList();
        budgetEntrySubCategoryObservableList = FXCollections.observableArrayList();
        budgetEntryCategoryObservableList = FXCollections.observableArrayList();


        trackingEntryObservableList.addListener((ListChangeListener<TrackingEntry>) change -> updateStore());

        var loadedData = storeService.Read();
        if(loadedData != null){
            trackingEntryObservableList.addAll(loadedData);
        }
    }

    private void updateStore() {
        storeService.Store(trackingEntryObservableList);
    }

    public void initialize(){
        initializeBudgetTypeList();
        initializeCategoryTypeList();
    }

    public ObservableList<TrackingEntry> GetTrackingEntryList() {
        return trackingEntryObservableList;
    }

    public ObservableList<BudgetEntryCategory> GetBudgetCategoryList(){
        return budgetEntryCategoryObservableList;
    }

    public ObservableList<BudgetEntrySubCategory> GetBudgetSubCategoryList(){
        return budgetEntrySubCategoryObservableList;
    }

    private void initializeBudgetTypeList() {
        budgetEntryCategoryObservableList.addAll(categoryProvider.GenerateAllBudgetCategories());
    }

    private void initializeCategoryTypeList(){
        BudgetEntryCategory type = budgetEntryCategoryObservableList.getFirst();
        budgetEntrySubCategoryObservableList.addAll(categoryProvider.GenerateSubCategoriesFromBudgetCategory(type));
    }

    public void UpdateCategories(BudgetEntryCategory category){
        budgetEntrySubCategoryObservableList.clear();
        BudgetEntrySubCategory[] subCategories = categoryProvider.GenerateSubCategoriesFromBudgetCategory(category);
        budgetEntrySubCategoryObservableList.addAll(subCategories);
    }

    public void addNewEntry(
            LocalDate date,
            BudgetEntryCategory category,
            BudgetEntrySubCategory subCategory,
            String details,
            Double amount) throws InvalidEntryException {
        if(date == null || amount == null)
            return;

        verifyValidSubCategoryForCategory(category, subCategory);
        if(amount == 0)
            throw new InvalidEntryException("\"Amount\" cannot be less than or equal to zero. Received: " + amount);

        LocalDate effectiveDate = date.getDayOfMonth() < 23 ? date : LocalDate.of(date.getYear(), date.getMonthValue() + 1, 1);
        TrackingEntry entry = new TrackingEntry(
                date,
                effectiveDate,
                category,
                subCategory,
                details,
                amount,
                0d
        );

        // Find where to insert it in the list such that the list stays sorted
        boolean inserted = false;
        for(int i = 0; i < trackingEntryObservableList.size(); i++) {
            if(trackingEntryObservableList.get(i).getDate().isAfter(date)){
                trackingEntryObservableList.add(i, entry);
                inserted = true;
                break;
            }
        }
        if(!inserted)
            trackingEntryObservableList.add(entry);

        recalculateBalanceForAllDateAfterAndDuring(date);
    }

    private void verifyValidSubCategoryForCategory(BudgetEntryCategory category, BudgetEntrySubCategory subCategory) {
        BudgetEntrySubCategory[] validSubCategories = categoryProvider.GenerateSubCategoriesFromBudgetCategory(category);
        for(BudgetEntrySubCategory validCat : validSubCategories)
            if(subCategory.equals(validCat))
                return;
        throw new InvalidEntryException("Category <" + subCategory + "> is an invalid category. Expected any of " + Arrays.toString(validSubCategories));
    }

    public void DeleteTrackingEntry(TrackingEntry trackingEntry) {
        if(!trackingEntryObservableList.contains(trackingEntry))
            return;
        trackingEntryObservableList.remove(trackingEntry);
        recalculateBalanceForAllDateAfterAndDuring(trackingEntry.getDate());
    }

    private void recalculateBalanceForAllDateAfterAndDuring(LocalDate date){
        List<LocalDate> datesDuringAndAfter =
                trackingEntryObservableList
                        .stream()
                        .filter(p -> !p.getDate().isBefore(date))
                        .sorted(this::trackerComparer)
                        .map(TrackingEntry::getDate)
                        .toList();
        Set<LocalDate> uniqueDatesDuringAndAfter =
                new LinkedHashSet<>(datesDuringAndAfter);
        for(LocalDate uniqueDate : uniqueDatesDuringAndAfter) {
            recalculateBalanceForDate(uniqueDate);
        }
    }

    private void recalculateBalanceForDate(LocalDate date){
        List<TrackingEntry> entriesOnDay = trackingEntryObservableList.stream()
                .filter(p -> p.getDate().isEqual(date)).toList();

        double amountDifferenceOnDay = 0;
        for(TrackingEntry entry : entriesOnDay) {
            amountDifferenceOnDay += entry.getAmount() * (entry.getCategory().isPositive() ? 1 : -1);
        }

        Optional<TrackingEntry> lastEntryBeforeDay = trackingEntryObservableList.stream()
                .filter(p -> p.getDate().isBefore(date)).max(this::trackerComparer);
        double balanceForDay;
        if(lastEntryBeforeDay.isEmpty()) {
            balanceForDay = amountDifferenceOnDay;
        }
        else {
            balanceForDay = lastEntryBeforeDay.get().getBalance() + amountDifferenceOnDay;
        }

        balanceForDay = Math.round(100 * balanceForDay) / 100d;

        for(TrackingEntry entry : entriesOnDay) {
            entry.setBalance(balanceForDay);
        }
    }

    private void sortEntriesByDate() {
        trackingEntryObservableList.sort(this::trackerComparer);
    }

    private int trackerComparer(TrackingEntry a, TrackingEntry b){
        if(a.getDate().isEqual(b.getDate())) {
            return a.getCategory().name().compareTo(b.getCategory().name());
        }
        return a.getDate().isBefore(b.getDate()) ? -1 : 1;
    }
}
