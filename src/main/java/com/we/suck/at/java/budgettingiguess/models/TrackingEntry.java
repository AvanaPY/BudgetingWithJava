package com.we.suck.at.java.budgettingiguess.models;

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

    public Double getAmount()
    {
        return amount.get();
    }

    public Double getBalance()
    {
        return balance.get();
    }

    public void setBalance(double value)
    {
        balance.setValue(value);
    }
}

