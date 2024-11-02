package org.sture.java.budgeting.services.job.module;

import org.sture.java.budgeting.di.DiContainer;
import org.sture.java.budgeting.di.RegisterType;
import org.sture.java.budgeting.modules.BaseModule;
import org.sture.java.budgeting.services.job.BackgroundJobExecutionService;

public class BackgroundJobModule implements BaseModule {
    @Override
    public void RegisterModule(DiContainer container) {
        container.Register(BackgroundJobExecutionService.class, RegisterType.Singleton);
    }

    @Override
    public void OnAllModulesLoaded(DiContainer container) {

    }
}
