package org.sture.java.budgeting.store.dto;

import org.sture.java.budgeting.models.BudgetEntryCategory;
import org.sture.java.budgeting.models.BudgetEntrySubCategory;
import org.sture.java.budgeting.models.TrackingEntry;
import org.sture.java.budgeting.utils.DTO;

import java.io.Serializable;
import java.time.LocalDate;

public class TrackingEntryDTO implements DTO<TrackingEntry>, Serializable {
    private final LocalDate date;
    private final LocalDate effectiveDate;
    private final BudgetEntryCategory category;
    private final BudgetEntrySubCategory subCategory;
    private final String details;
    private final double amount;
    private final double balance;
    public TrackingEntryDTO(TrackingEntry entry){
        this.date           = entry.getDate();
        this.effectiveDate  = entry.getEffectiveDate();
        this.category = entry.getCategory();
        this.subCategory = entry.getSubCategory();
        this.details        = entry.getDetails();
        this.amount         = entry.getAmount();
        this.balance        = entry.getBalance();
    }

    public TrackingEntry Convert() {
        return new TrackingEntry(
                date,
                effectiveDate,
                category,
                subCategory,
                details,
                amount,
                balance);
    }
}