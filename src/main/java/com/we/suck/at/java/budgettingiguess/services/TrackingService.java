package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.models.BudgetType;
import com.we.suck.at.java.budgettingiguess.models.TrackingEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class TrackingService {
    private final ObservableList<TrackingEntry> trackingEntryObservableList;
    private final ObservableList<BudgetType> budgetTypesObservableList;
    private final ObservableList<String> categoryObservableList;

    public TrackingService() {
        categoryObservableList = FXCollections.observableArrayList();
        trackingEntryObservableList = FXCollections.observableArrayList();
        budgetTypesObservableList = FXCollections.observableArrayList();

        setupDummyData();
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
        UpdateCategories(GetBudgetTypeList().getFirst());
        return categoryObservableList;
    }

    private void initializeBudgetTypeList() {
        budgetTypesObservableList.add(BudgetType.Income);
        budgetTypesObservableList.add(BudgetType.Expense);
        budgetTypesObservableList.add(BudgetType.Saving);
    }

    private void setupDummyData() {
        trackingEntryObservableList.add(new TrackingEntry(
                LocalDate.of(2024, Month.AUGUST, 19),
                LocalDate.of(2024, Month.AUGUST, 19),
                BudgetType.Income,
                "Employment",
                "Just more monies!",
                10,
                10
        ));
        trackingEntryObservableList.add(new TrackingEntry(
                LocalDate.of(2024, Month.AUGUST, 20),
                LocalDate.of(2024, Month.AUGUST, 20),
                BudgetType.Income,
                "Employment",
                "Not so much monies :(",
                100,
                110
        ));
        trackingEntryObservableList.add(new TrackingEntry(
                LocalDate.now(),
                LocalDate.now(),
                BudgetType.Expense,
                "Rent",
                "So expensive wtf :(",
                80,
                30
        ));
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
            Double amount) {

        if(date == null || amount == null)
            return;

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
        trackingEntryObservableList.add(entry);

        recalculateBalanceForAllDateAfterAndDuring(date);
    }

    public void DeleteTrackingEntry(TrackingEntry trackingEntry) throws Exception {
        if(!trackingEntryObservableList.contains(trackingEntry))
            throw new Exception(trackingEntry + " could not be found.");
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
        sortEntriesByDate();
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
        sortEntriesByDate();
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
