package org.sture.java.budgeting.services.tracking.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class TrackingEntry {
    private final LocalDate date;
    private final LocalDate effectiveDate;
    private final SimpleObjectProperty<BudgetEntryCategory> category;
    private final SimpleObjectProperty<BudgetEntryCategory> subCategory;
    private final SimpleStringProperty details;
    private final SimpleDoubleProperty amount;
    private final DoubleProperty balance;

    public TrackingEntry(
            LocalDate date,
            LocalDate effectiveDate,
            BudgetEntryCategory category,
            BudgetEntryCategory subCategory,
            String details,
            double amount,
            double balance)
    {
        this.date = date;
        this.effectiveDate = effectiveDate;
        this.category = new SimpleObjectProperty<>(category);
        this.subCategory = new SimpleObjectProperty<>(subCategory);
        this.details = new SimpleStringProperty(details);
        this.amount = new SimpleDoubleProperty(amount);
        this.balance = new SimpleDoubleProperty(balance);
    }

    @Override
    public String toString(){
        return "[%s %s | %s %s %s | %s %s]".formatted(
                getDate(),
                getEffectiveDate(),
                getCategory(),
                getSubCategory(),
                getDetails(),
                getAmount(),
                getBalance());
    }

    public LocalDate getDate()
    {
        return date;
    }

    public LocalDate getEffectiveDate()
    {
        return effectiveDate;
    }

    public BudgetEntryCategory getCategory()
    {
        return category.get();
    }

    public BudgetEntryCategory getSubCategory() { return subCategory.get(); }

    public String getDetails()
    {
        return details.get();
    }

    public double getAmount()
    {
        return amount.get();
    }

    public double getBalance()
    {
        return balance.get();
    }

    public void setBalance(double value)
    {
        balance.setValue(value);
    }

    @Override
    public boolean equals(Object o){
        boolean result = false;
        if(o instanceof TrackingEntry to)
        {
            result = to.getDate().equals(getDate())
                    && to.getEffectiveDate().equals(getEffectiveDate())
                    && to.getCategory().equals(getCategory())
                    && to.getSubCategory().equals(getSubCategory())
                    && to.getDetails().equals(getDetails())
                    && to.getAmount() == getAmount()
                    && to.getBalance() == getBalance();
        }
        return result;
    }
}

