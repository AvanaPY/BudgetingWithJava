package org.sture.java.budgeting.store.tracking.service;

import org.sture.java.budgeting.services.job.BackgroundJobExecutionService;
import org.sture.java.budgeting.store.StoreService;
import org.sture.java.budgeting.store.tracking.dto.TrackingEntryDTO;
import org.sture.java.budgeting.store.tracking.dto.TrackingEntryDTOConverter;
import org.sture.java.budgeting.services.tracking.models.TrackingEntry;

public class TrackingEntryStoreService extends StoreService<TrackingEntry, TrackingEntryDTO> {

    public TrackingEntryStoreService(
            TrackingEntryDTOConverter dtoFactory,
            BackgroundJobExecutionService jes) {
        super("trackingEntry.store", dtoFactory, jes);
    }
}
