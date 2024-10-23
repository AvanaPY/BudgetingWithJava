package org.sture.java.budgeting.store;

import org.sture.java.budgeting.store.dto.TrackingEntryDTO;
import org.sture.java.budgeting.store.dto.TrackingEntryDTOConverter;
import org.sture.java.budgeting.services.tracking.models.TrackingEntry;

public class TrackingEntryStoreService extends StoreService<TrackingEntry, TrackingEntryDTO> {

    public TrackingEntryStoreService(TrackingEntryDTOConverter dtoFactory) {
        super("trackingEntry.store", dtoFactory);
    }
}
