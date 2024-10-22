package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.dto.TrackingEntryDTO;
import com.we.suck.at.java.budgettingiguess.dto.TrackingEntryDTOConverter;
import com.we.suck.at.java.budgettingiguess.models.TrackingEntry;

public class TrackingEntryStoreService extends StoreService<TrackingEntry, TrackingEntryDTO> {

    public TrackingEntryStoreService(TrackingEntryDTOConverter dtoFactory) {
        super("trackingEntry.store", dtoFactory);
    }
}
