package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.exceptions.InvalidEntryException;
import com.we.suck.at.java.budgettingiguess.models.BudgetType;
import com.we.suck.at.java.budgettingiguess.models.TrackingEntry;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.*;

public class TrackingService {
    private final TrackingEntryStoreService storeService;
    private final ObservableList<TrackingEntry> trackingEntryObservableList;
    private final ObservableList<BudgetType> budgetTypesObservableList;
    private final ObservableList<String> categoryObservableList;

    public TrackingService(TrackingEntryStoreService storeService) {
        this.storeService = storeService;
        trackingEntryObservableList = FXCollections.observableArrayList();
        categoryObservableList = FXCollections.observableArrayList();
        budgetTypesObservableList = FXCollections.observableArrayList();

        trackingEntryObservableList.addListener((ListChangeListener<TrackingEntry>) change -> {
            updateStore();
        });

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
        UpdateCategories(BudgetType.Income);
    }

    public ObservableList<TrackingEntry> GetTrackingEntryList() {
        return trackingEntryObservableList;
    }

    public ObservableList<BudgetType> GetBudgetTypeList(){
        return budgetTypesObservableList;
    }

    public ObservableList<String> GetCategoryList(){
        return categoryObservableList;
    }

    private void initializeBudgetTypeList() {
        budgetTypesObservableList.add(BudgetType.Income);
        budgetTypesObservableList.add(BudgetType.Expense);
        budgetTypesObservableList.add(BudgetType.Saving);
    }

    public void UpdateCategories(BudgetType type){
        categoryObservableList.clear();
        String[] cats = CategoryProvider.GenerateCategoriesFromBudgetType(type);
        categoryObservableList.addAll(cats);
    }

    public void addNewEntry(
            LocalDate date,
            BudgetType type,
            String category,
            String details,
            Double amount) throws InvalidEntryException {
        if(date == null || amount == null)
            return;

        verifyValidCategoryForBudgetType(type, category);
        if(amount <= 0)
            throw new InvalidEntryException("\"Amount\" cannot be less than or equal to zero. Received: " + amount);

        LocalDate effectiveDate = date.getDayOfMonth() < 23 ? date : LocalDate.of(date.getYear(), date.getMonthValue() + 1, 1);
        TrackingEntry entry = new TrackingEntry(
                date,
                effectiveDate,
                type,
                category,
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

    private void verifyValidCategoryForBudgetType(BudgetType type, String category) {
        String[] validCategories = CategoryProvider.GenerateCategoriesFromBudgetType(type);
        for(String validCat : validCategories)
            if(category.equals(validCat))
                return;
        throw new InvalidEntryException("Category <" + category + "> is an invalid category. Expected any of " + Arrays.toString(validCategories));
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
            amountDifferenceOnDay += entry.getAmount() * (entry.getType() == BudgetType.Income ? 1 : -1);
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
            return a.getType() .compareTo(b.getType());
        }
        return a.getDate().isBefore(b.getDate()) ? -1 : 1;
    }
}
