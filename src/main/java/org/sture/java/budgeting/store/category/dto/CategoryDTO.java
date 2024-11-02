package org.sture.java.budgeting.store.category.dto;

import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.store.DTO;

import java.io.Serializable;

public class CategoryDTO implements DTO<BudgetEntryCategory>, Serializable {
    private final String name;
    private final boolean isPositive;
    private final CategoryDTO[] subCategories;

    public CategoryDTO(BudgetEntryCategory category){
        name = category.name();
        isPositive = category.isPositive();
        subCategories = new CategoryDTO[category.GetSubCategories().length];
        for(int i = 0; i < subCategories.length; i++){
            subCategories[i] = new CategoryDTO(category.GetSubCategory(i));
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
