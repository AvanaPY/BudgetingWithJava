package org.sture.java.budgeting.models;

import java.io.Serializable;

public record BudgetEntryCategory(String name, boolean isPositive) implements Serializable
{
    @Override
    public boolean equals(Object o){
        if (o instanceof BudgetEntryCategory oc){
            return oc.name.equals(name)
                    && oc.isPositive == isPositive();
        }
        return false;
    }
}

