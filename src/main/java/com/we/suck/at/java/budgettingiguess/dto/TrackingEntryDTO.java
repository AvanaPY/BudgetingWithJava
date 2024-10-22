package com.we.suck.at.java.budgettingiguess.dto;

import com.we.suck.at.java.budgettingiguess.models.BudgetType;
import com.we.suck.at.java.budgettingiguess.models.TrackingEntry;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;

public class TrackingEntryDTO implements Serializable {
    private final LocalDate date;
    private final LocalDate effectiveDate;
    private final BudgetType type;
    private final String category;
    private final String details;
    private final double amount;
    private final double balance;
    public TrackingEntryDTO(TrackingEntry entry){
        this.date           = entry.getDate();
        this.effectiveDate  = entry.getEffectiveDate();
        this.type           = entry.getType();
        this.category       = entry.getCategory();
        this.details        = entry.getDetails();
        this.amount         = entry.getAmount();
        this.balance        = entry.getBalance();
    }

    public TrackingEntry toTrackingEntry() {
        return new TrackingEntry(date, effectiveDate, type, category, details, amount, balance);
    }
}
