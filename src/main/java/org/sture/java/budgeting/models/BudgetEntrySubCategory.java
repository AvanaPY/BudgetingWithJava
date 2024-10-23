package org.sture.java.budgeting.models;

import java.io.Serializable;

public record BudgetEntrySubCategory(String name) implements Serializable {
    @Override
    public boolean equals(Object o){
        if (o instanceof BudgetEntrySubCategory oc){
            return oc.name.equals(name);
        }
        return false;
    }
}
