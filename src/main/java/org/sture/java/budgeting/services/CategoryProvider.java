package org.sture.java.budgeting.services;

import org.sture.java.budgeting.models.BudgetType;

public class CategoryProvider {
    public static String[] GenerateCategoriesFromBudgetType(BudgetType type)
    {
        switch(type)
        {
            case Income -> {
                return new String[] {
                  "Employment",
                  "Misc"
                };
            }
            case Expense -> {
                return new String[] {
                        "Rent",
                        "Utilities",
                        "Entertainment",
                        ""
                };
            }
            case Saving -> {
                return new String[] {
                        "Investment",
                        "Buffer",
                        "Summer"
                };
            }
            default -> {
                return new String[] {};
            }
        }
    }
}
