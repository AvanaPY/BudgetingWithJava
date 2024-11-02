package org.sture.java.budgeting.modules;

import org.sture.java.budgeting.di.DiContainer;

public interface BaseModule {
    void RegisterModule(DiContainer container);
    void OnAllModulesLoaded(DiContainer container);
}
