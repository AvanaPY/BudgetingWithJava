package org.sture.java.budgeting.services.tracking.module;

import org.sture.java.budgeting.di.DiContainer;
import org.sture.java.budgeting.di.RegisterType;
import org.sture.java.budgeting.modules.BaseModule;
import org.sture.java.budgeting.services.tracking.TrackingService;
import org.sture.java.budgeting.store.tracking.dto.TrackingEntryDTOConverter;
import org.sture.java.budgeting.store.tracking.service.TrackingEntryStoreService;

public class TrackingModule implements BaseModule {
    @Override
    public void RegisterModule(DiContainer container) {
        container.Register(TrackingEntryDTOConverter.class, RegisterType.Singleton);
        container.Register(TrackingEntryStoreService.class, RegisterType.Singleton);
        container.Register(TrackingService.class, RegisterType.Singleton);
    }

    @Override
    public void OnAllModulesLoaded(DiContainer container) {

    }
}
