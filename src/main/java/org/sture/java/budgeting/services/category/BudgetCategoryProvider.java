package org.sture.java.budgeting.services.category;

import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.store.budgetcategory.service.BudgetCategoryStoreService;
import org.sture.java.budgeting.utils.TestHarness;

public class BudgetCategoryProvider {
    private final BudgetCategoryStoreService budgetCategoryStoreService;
    private BudgetEntryCategory[] budgetEntryCategories;

    public BudgetCategoryProvider(BudgetCategoryStoreService budgetCategoryStoreService) {
        this.budgetCategoryStoreService = budgetCategoryStoreService;

        budgetEntryCategories = budgetCategoryStoreService.Read();
        if(budgetEntryCategories == null || budgetEntryCategories.length == 0)
        {
            var incomeCategory = new BudgetEntryCategory("Income", true);
            incomeCategory.AddSubCategory(new BudgetEntryCategory("Employment"));
            incomeCategory.AddSubCategory(new BudgetEntryCategory("Misc"));

            var expenseCategory = new BudgetEntryCategory("Expense", false);
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Rent"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Utilities"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Groceries"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Services"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Transportation"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Entertainment"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Eating Out"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Clothes"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Travel"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Loan"));
            expenseCategory.AddSubCategory(new BudgetEntryCategory("Misc"));

            var savingsCategory = new BudgetEntryCategory("Saving", false);
            savingsCategory.AddSubCategory(new BudgetEntryCategory("Investment"));
            savingsCategory.AddSubCategory(new BudgetEntryCategory("Buffer"));
            savingsCategory.AddSubCategory(new BudgetEntryCategory("Summer"));

            budgetEntryCategories = new BudgetEntryCategory[]{
                    incomeCategory,
                    expenseCategory,
                    savingsCategory
            };
            setCategories(budgetEntryCategories);
        }
    }

    private void setCategories(BudgetEntryCategory[] cats){
        budgetEntryCategories = cats;
        this.budgetCategoryStoreService.Store(budgetEntryCategories);

    }

    public BudgetEntryCategory[] GenerateAllBudgetCategories() {
        return budgetEntryCategories;
    }

    public void FromTestOverrideCategories(BudgetEntryCategory[] categories) {
        if(!TestHarness.isRunningFromTestHarness())
            throw new RuntimeException("Attempted to override categories outside of testing.");
        budgetEntryCategories = categories;
    }
}
