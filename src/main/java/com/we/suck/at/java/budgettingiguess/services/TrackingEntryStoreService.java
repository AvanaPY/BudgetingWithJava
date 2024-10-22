package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.dto.TrackingEntryDTO;
import com.we.suck.at.java.budgettingiguess.dto.TrackingEntryDTOFactory;
import com.we.suck.at.java.budgettingiguess.models.TrackingEntry;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class TrackingEntryStoreService extends StoreService<TrackingEntry, TrackingEntryDTO> {

    public TrackingEntryStoreService(TrackingEntryDTOFactory dtoFactory) {
        super("trackingEntry.store", dtoFactory);
        try {
            Files.createDirectories(dataDirectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void Store(TrackingEntry entry) {
        var dto = new TrackingEntryDTO(entry);
        Store(dto);
    }

    public void Store(TrackingEntry[] entries) {
        TrackingEntryDTO[] dtoEntries = Arrays.stream(entries).map(TrackingEntryDTO::new).toList().toArray(new TrackingEntryDTO[0]);
        Store(dtoEntries);
    }

    private void Store(TrackingEntryDTO object){
        Store(new TrackingEntryDTO[] { object });
    }
}
