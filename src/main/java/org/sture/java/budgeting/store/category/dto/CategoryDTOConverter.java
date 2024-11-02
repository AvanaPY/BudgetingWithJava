package org.sture.java.budgeting.store.category.dto;

import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.store.DTOConverter;

import java.util.List;

public class CategoryDTOConverter implements DTOConverter<BudgetEntryCategory, CategoryDTO> {
    @Override
    public CategoryDTO ConvertToDTO(BudgetEntryCategory budgetEntryCategory) {
        return new CategoryDTO(budgetEntryCategory);
    }

    @Override
    public CategoryDTO[] DoMagicToDTO(List<CategoryDTO> lst) {
        return lst.toArray(new CategoryDTO[0]);
    }

    @Override
    public BudgetEntryCategory ConvertFromDTO(CategoryDTO t) {
        return t.Convert();
    }

    @Override
    public BudgetEntryCategory[] DoMagicFromDTO(List<BudgetEntryCategory> lst) {
        return lst.toArray(new BudgetEntryCategory[0]);
    }
}
