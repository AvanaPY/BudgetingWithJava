package org.sture.java.budgeting.modules;

import org.sture.java.budgeting.di.DiContainer;

public interface BaseModule {

    /**
     * Registers all the relevant classes and interfaces of the module.
     * @param container A DiContainer which to register module classes to
     */
    void RegisterModule(DiContainer container);

    /**
     * Called after all the modules have been registered.
     * Useful if you want to build module-specific classes.
     * Not very useful now but maybe in the future.
     * @param container A DiContainer which to register additional module classes to
     */
    void OnAllModulesLoaded(DiContainer container);
}
