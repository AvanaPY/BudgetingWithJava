package org.sture.java.budgeting.services.tracking.models;

import java.io.Serializable;

public class BudgetEntryCategory implements Serializable {
    private final String name;
    private final boolean isPositive;
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

    public boolean isPositive(){
        return isPositive;
    }

    public BudgetEntryCategory GetSubCategory(int index){
        return subCategories[index];
    }

    public BudgetEntryCategory[] GetSubCategories(){
        return subCategories;
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

