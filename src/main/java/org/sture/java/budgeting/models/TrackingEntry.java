package org.sture.java.budgeting.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class TrackingEntry {
    private final LocalDate date;
    private final LocalDate effectiveDate;
    private final SimpleObjectProperty<BudgetType> type;
    private final SimpleStringProperty category;
    private final SimpleStringProperty details;
    private final SimpleDoubleProperty amount;
    private final DoubleProperty balance;

    public TrackingEntry(
            LocalDate date,
            LocalDate effectiveDate,
            BudgetType type,
            String category,
            String details,
            double amount,
            double balance)
    {
        this.date = date;
        this.effectiveDate = effectiveDate;
        this.type = new SimpleObjectProperty<>(type);
        this.category = new SimpleStringProperty(category);
        this.details = new SimpleStringProperty(details);
        this.amount = new SimpleDoubleProperty(amount);
        this.balance = new SimpleDoubleProperty(balance);
    }

    @Override
    public String toString(){
        return "[%s %s | %s %s %s | %s %s]".formatted(
                getDate(),
                getEffectiveDate(),
                getType(),
                getCategory(),
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

    public BudgetType getType()
    {
        return type.get();
    }

    public String getCategory()
    {
        return category.get();
    }

    public String getDetails()
    {
        return details.get();
    }

    public double getAmount()
    {
        return (double)amount.get();
    }

    public double getBalance()
    {
        return ((double) balance.get());
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
                    && to.getType() == getType()
                    && to.getCategory().equals(getCategory())
                    && to.getDetails().equals(getDetails())
                    && to.getAmount() == getAmount()
                    && to.getBalance() == getBalance();
        }
        return result;
    }
}

