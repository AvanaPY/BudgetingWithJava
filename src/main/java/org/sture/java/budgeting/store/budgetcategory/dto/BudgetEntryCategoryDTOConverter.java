package org.sture.java.budgeting.store.budgetcategory.dto;

import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.store.DTOConverter;

import java.util.List;

public class BudgetEntryCategoryDTOConverter implements DTOConverter<BudgetEntryCategory, BudgetEntryCategoryDTO> {
    @Override
    public BudgetEntryCategoryDTO ConvertToDTO(BudgetEntryCategory budgetEntryCategory) {
        return new BudgetEntryCategoryDTO(budgetEntryCategory);
    }

    @Override
    public BudgetEntryCategoryDTO[] DoMagicToDTO(List<BudgetEntryCategoryDTO> lst) {
        return lst.toArray(new BudgetEntryCategoryDTO[0]);
    }

    @Override
    public BudgetEntryCategory ConvertFromDTO(BudgetEntryCategoryDTO t) {
        return t.Convert();
    }

    @Override
    public BudgetEntryCategory[] DoMagicFromDTO(List<BudgetEntryCategory> lst) {
        return lst.toArray(new BudgetEntryCategory[0]);
    }
}
