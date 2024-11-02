package org.sture.java.budgeting.services.category.module;

import org.sture.java.budgeting.di.DiContainer;
import org.sture.java.budgeting.di.RegisterType;
import org.sture.java.budgeting.modules.BaseModule;
import org.sture.java.budgeting.services.category.CategoryService;
import org.sture.java.budgeting.store.category.dto.CategoryDTOConverter;
import org.sture.java.budgeting.store.category.service.CategoryStoreService;

public class CategoryModule implements BaseModule  {
    @Override
    public void RegisterModule(DiContainer container) {
        container.Register(CategoryDTOConverter.class, RegisterType.Singleton);
        container.Register(CategoryStoreService.class, RegisterType.Singleton);
        container.Register(CategoryService.class, RegisterType.Singleton);
    }

    @Override
    public void OnAllModulesLoaded(DiContainer container) {

    }
}
