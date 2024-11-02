package org.sture.java.budgeting.services.category.module;

import org.sture.java.budgeting.di.DiContainer;
import org.sture.java.budgeting.di.RegisterType;
import org.sture.java.budgeting.modules.BaseModule;
import org.sture.java.budgeting.services.category.BudgetCategoryProvider;
import org.sture.java.budgeting.store.budgetcategory.dto.BudgetEntryCategoryDTOConverter;
import org.sture.java.budgeting.store.budgetcategory.service.BudgetCategoryStoreService;

public class CategoryModule implements BaseModule  {
    @Override
    public void RegisterModule(DiContainer container) {
        container.Register(BudgetEntryCategoryDTOConverter.class, RegisterType.Singleton);
        container.Register(BudgetCategoryStoreService.class, RegisterType.Singleton);
        container.Register(BudgetCategoryProvider.class, RegisterType.Singleton);
    }

    @Override
    public void OnAllModulesLoaded(DiContainer container) {

    }
}
