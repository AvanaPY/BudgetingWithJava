package org.sture.java.budgeting.services;

import org.sture.java.budgeting.services.tracking.models.BudgetEntrySubCategory;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.utils.TestHarness;

import java.util.HashMap;

public class BudgetTypeCategoryProvider {
    private BudgetEntryCategory[] budgetEntryCategories;
    private final HashMap<String, BudgetEntrySubCategory[]> categorySubCategoryMap;

    public BudgetTypeCategoryProvider() {
        budgetEntryCategories = new BudgetEntryCategory[]{
                new BudgetEntryCategory("Income", true),
                new BudgetEntryCategory("Expense", false),
                new BudgetEntryCategory("Saving", false)
        };
        categorySubCategoryMap = new HashMap<>();
        categorySubCategoryMap.put(budgetEntryCategories[0].name(), new BudgetEntrySubCategory[] {
                new BudgetEntrySubCategory("Employment"),
                new BudgetEntrySubCategory("CSN"),
                new BudgetEntrySubCategory("Misc"),
        });
        categorySubCategoryMap.put(budgetEntryCategories[1].name(), new BudgetEntrySubCategory[] {
                new BudgetEntrySubCategory("Rent"),
                new BudgetEntrySubCategory("Utilities"),
                new BudgetEntrySubCategory("Groceries"),
                new BudgetEntrySubCategory("Services"),
                new BudgetEntrySubCategory("Transportation"),
                new BudgetEntrySubCategory("Entertainment"),
                new BudgetEntrySubCategory("Eating Out"),
                new BudgetEntrySubCategory("Clothes"),
                new BudgetEntrySubCategory("Travel"),
                new BudgetEntrySubCategory("Loan"),
                new BudgetEntrySubCategory("Misc"),
        });
        categorySubCategoryMap.put(budgetEntryCategories[2].name(), new BudgetEntrySubCategory[] {
                new BudgetEntrySubCategory("Investment"),
                new BudgetEntrySubCategory("Buffer"),
                new BudgetEntrySubCategory("Summer"),
        });
    }

    public BudgetEntryCategory[] GenerateAllBudgetCategories() {
        return budgetEntryCategories;
    }

    public BudgetEntrySubCategory[] GenerateSubCategoriesFromBudgetCategory(BudgetEntryCategory category)
    {
        if(categorySubCategoryMap.containsKey(category.name()))
            return categorySubCategoryMap.get(category.name());
        throw new RuntimeException("Category <" + category + "> does not have any sub categories registered");
    }

    public void FromTestOverrideCategories(BudgetEntryCategory[] categories) {
        if(!TestHarness.isRunningFromTestHarness())
            throw new RuntimeException("Attempted to override categories outside of testing.");
        budgetEntryCategories = categories;
        categorySubCategoryMap.clear();
    }

    public void FromTestMakeSubCategoryEntry(BudgetEntryCategory category, BudgetEntrySubCategory[] subCategories){
        if(!TestHarness.isRunningFromTestHarness())
            throw new RuntimeException("Attempted to override sub categories outside of testing.");
        categorySubCategoryMap.put(category.name(), subCategories);
    }
}
