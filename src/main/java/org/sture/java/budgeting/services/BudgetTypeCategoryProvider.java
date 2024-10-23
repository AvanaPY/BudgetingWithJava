package org.sture.java.budgeting.services;

import org.sture.java.budgeting.models.BudgetEntrySubCategory;
import org.sture.java.budgeting.models.BudgetEntryCategory;
import org.sture.java.budgeting.test.TestHarness;

import java.util.HashMap;

public class BudgetTypeCategoryProvider {

    private BudgetEntryCategory[] budgetEntryCategories = new BudgetEntryCategory[] {
            new BudgetEntryCategory("Income", true),
            new BudgetEntryCategory("Expense", false),
            new BudgetEntryCategory("Saving", false)
    };

    private final HashMap<String, BudgetEntrySubCategory[]> categorySubCategoryMap = new HashMap<>();

    public BudgetEntryCategory[] GenerateAllBudgetCategories() {
        return budgetEntryCategories;
    }

    public BudgetEntrySubCategory[] GenerateSubCategoriesFromBudgetCategory(BudgetEntryCategory category)
    {
        return categorySubCategoryMap.getOrDefault(category.name(), null);
//        switch(category.name())
//        {
//            case "Income" -> {
//                return new BudgetEntrySubCategory[] {
//                        new BudgetEntrySubCategory("Employment"),
//                        new BudgetEntrySubCategory("CSN"),
//                        new BudgetEntrySubCategory("Misc"),
//                };
//            }
//            case "Expense" -> {
//                return new BudgetEntrySubCategory[] {
//                        new BudgetEntrySubCategory("Rent"),
//                        new BudgetEntrySubCategory("Utilities"),
//                        new BudgetEntrySubCategory("Groceries"),
//                        new BudgetEntrySubCategory("Insurance"),
//                        new BudgetEntrySubCategory("Misc"),
//
//                };
//            }
//            case "Saving" -> {
//                return new BudgetEntrySubCategory[] {
//                        new BudgetEntrySubCategory("Investment"),
//                        new BudgetEntrySubCategory("Buffer"),
//                        new BudgetEntrySubCategory("Summer"),
//                };
//            }
//            default -> {
//                return new BudgetEntrySubCategory[0];
//            }
//        }
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
