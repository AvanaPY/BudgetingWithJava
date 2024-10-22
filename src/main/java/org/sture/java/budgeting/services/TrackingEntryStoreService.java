package org.sture.java.budgeting.services;

import org.sture.java.budgeting.dto.TrackingEntryDTO;
import org.sture.java.budgeting.dto.TrackingEntryDTOConverter;
import org.sture.java.budgeting.models.TrackingEntry;

public class TrackingEntryStoreService extends StoreService<TrackingEntry, TrackingEntryDTO> {

    public TrackingEntryStoreService(TrackingEntryDTOConverter dtoFactory) {
        super("trackingEntry.store", dtoFactory);
    }
}
