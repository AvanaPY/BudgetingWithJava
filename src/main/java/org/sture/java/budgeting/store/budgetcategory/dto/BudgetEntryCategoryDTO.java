package org.sture.java.budgeting.store.budgetcategory.dto;

import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.utils.DTO;

import java.io.Serializable;

public class BudgetEntryCategoryDTO implements DTO<BudgetEntryCategory>, Serializable {
    private final String name;
    private final boolean isPositive;
    private final BudgetEntryCategoryDTO[] subCategories;

    public BudgetEntryCategoryDTO(BudgetEntryCategory category){
        name = category.name();
        isPositive = category.isPositive();
        subCategories = new BudgetEntryCategoryDTO[category.GetSubCategories().length];
        for(int i = 0; i < subCategories.length; i++){
            subCategories[i] = new BudgetEntryCategoryDTO(category.GetSubCategory(i));
        }
    }

    @Override
    public BudgetEntryCategory Convert() {
        BudgetEntryCategory[] cats = new BudgetEntryCategory[subCategories.length];
        for(int i = 0; i < cats.length; i++)
            cats[i] = subCategories[i].Convert();

        return new BudgetEntryCategory(
                name,
                isPositive,
                cats
        );
    }
}
