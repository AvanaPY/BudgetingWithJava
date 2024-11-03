package org.sture.java.budgeting.services.tracking.models;

import java.io.Serializable;

public class BudgetEntryCategory implements Serializable {
    private String name;
    private boolean isPositive;
    private BudgetEntryCategory[] subCategories;

    public BudgetEntryCategory(String name, boolean isPositive, BudgetEntryCategory[] subCategories){
        this.name = name;
        this.isPositive = isPositive;
        this.subCategories = subCategories;
    }

    public BudgetEntryCategory(String name, boolean isPositive) {
        this(name, isPositive, new BudgetEntryCategory[0]);
    }

    public BudgetEntryCategory(String name) {
        this(name, false);
    }

    public String name(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean isPositive(){
        return isPositive;
    }

    public void setIsPositive(boolean isPositive){
        this.isPositive = isPositive;
    }

    public BudgetEntryCategory GetSubCategory(int index){
        return subCategories[index];
    }

    public BudgetEntryCategory GetSubCategoryByName(String subCategoryName) {
        for(var cat : subCategories)
            if(cat.name().equals(subCategoryName))
                return cat;
        return null;
    }

    public BudgetEntryCategory[] GetSubCategories(){
        return subCategories;
    }

    public void setSubCategories(BudgetEntryCategory[] subCategories){
        this.subCategories = subCategories;
    }

    public void AddSubCategory(BudgetEntryCategory subCategory){
        if(subCategory == null)
            return;

        for (BudgetEntryCategory category : subCategories)
            if (category == subCategory)
                return;
        BudgetEntryCategory[] newArray = new BudgetEntryCategory[subCategories.length + 1];
        System.arraycopy(subCategories, 0, newArray, 0, subCategories.length);
        newArray[newArray.length - 1] = subCategory;
        subCategories = newArray;
    }

    public void deleteSubCategory(BudgetEntryCategory subCategory){
        if(subCategory == null)
            return;

        boolean found = false;
        for(var cat : subCategories) {
            if (cat == subCategory) {
                found = true;
                break;
            }
        }

        if(!found)
            return;

        BudgetEntryCategory[] newArray = new BudgetEntryCategory[subCategories.length - 1];
        int idx = 0;
        for(var cat : subCategories){
            if(cat != subCategory){
                newArray[idx] = cat;
                idx++;
            }
        }
        subCategories = newArray;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof BudgetEntryCategory oc){
            return oc.name.equals(name)
                    && oc.isPositive == isPositive();
        }
        return false;
    }
}

