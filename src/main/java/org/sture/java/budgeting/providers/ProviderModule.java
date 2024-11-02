package org.sture.java.budgeting.providers;

import org.sture.java.budgeting.di.DiContainer;
import org.sture.java.budgeting.di.RegisterType;
import org.sture.java.budgeting.modules.BaseModule;

public class ProviderModule implements BaseModule {
    @Override
    public void RegisterModule(DiContainer container) {
        container.Register(DirectoryFileProvider.class, RegisterType.Singleton);
    }

    @Override
    public void OnAllModulesLoaded(DiContainer container) {

    }
}
