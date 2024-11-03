package org.sture.java.budgeting.services.category;

import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.store.category.service.CategoryStoreService;
import org.sture.java.budgeting.utils.TestHarness;

import java.util.Arrays;

public class CategoryService {
    private final CategoryStoreService categoryStoreService;
    private BudgetEntryCategory[] categories;

    public CategoryService(CategoryStoreService categoryStoreService) {
        this.categoryStoreService = categoryStoreService;

        categories = categoryStoreService.Read();
        if(categories == null || categories.length == 0)
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

            categories = new BudgetEntryCategory[]{
                    incomeCategory,
                    expenseCategory,
                    savingsCategory
            };
            setCategories(categories);
        }
    }

    public void addNewCategory(BudgetEntryCategory category) {
        if(category == null)
            return;

        var newCategories = new BudgetEntryCategory[categories.length + 1];
        System.arraycopy(
                categories, 0,
                newCategories, 0,
                categories.length);
        newCategories[newCategories.length - 1] = category;
        setCategories(newCategories);
    }

    public void updateCategory(
            BudgetEntryCategory category,
            String newName,
            boolean isPositive,
            BudgetEntryCategory[] subCategories){
        category.setName(newName);
        category.setIsPositive(isPositive);
        category.setSubCategories(subCategories);
        store();
    }

    public void deleteCategoryByName(String name) {
        var category = findCategoryByName(name);
        if(category != null) {
            var newCategories = new BudgetEntryCategory[categories.length - 1];
            var idx = 0;
            for(var c : categories){
                if(c != category){
                    newCategories[idx] = c;
                    idx++;
                }
            }
            setCategories(newCategories);
        }
    }

    public void addNewSubCategory(String categoryName, BudgetEntryCategory subCategory) {
        var category = findCategoryByName(categoryName);
        if(category == null || subCategory == null)
            return;
        category.AddSubCategory(subCategory);
        store();
    }

    public void updateSubCategory(
            BudgetEntryCategory subCategory,
            String newName,
            Boolean newIsPositive){
        subCategory.setName(newName);
        subCategory.setIsPositive(newIsPositive);
        store();
    }

    public void deleteSubCategoryByName(BudgetEntryCategory category, String subCategoryName){
        if(category == null || subCategoryName.isEmpty())
            return;
        var subCategory = category.GetSubCategoryByName(subCategoryName);
        if(subCategory != null)
            category.deleteSubCategory(subCategory);
        store();
    }

    public BudgetEntryCategory findCategoryByName(String name){
        var result = Arrays.stream(categories).filter(p -> p.name().equals(name)).findFirst();
        return result.orElse(null);
    }

    public BudgetEntryCategory findSubCategoryByName(String categoryName, String subCategoryName) {
        var category = findCategoryByName(categoryName);
        if(category == null)
            return null;

        for(BudgetEntryCategory subCategory : category.GetSubCategories()){
            if(subCategory.name().equals(subCategoryName))
                return subCategory;
        }

        return null;
    }

    public BudgetEntryCategory[] GenerateAllBudgetCategories() {
        return categories;
    }

    public void FromTestOverrideCategories(BudgetEntryCategory[] categories) {
        if(!TestHarness.isRunningFromTestHarness())
            throw new RuntimeException("Attempted to override categories outside of testing.");
        this.categories = categories;
    }

    private void setCategories(BudgetEntryCategory[] cats){
        categories = cats;
        store();
    }

    private void store(){
        this.categoryStoreService.Store(categories);
    }
}
