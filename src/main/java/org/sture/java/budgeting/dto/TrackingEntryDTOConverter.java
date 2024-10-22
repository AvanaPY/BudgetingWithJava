package org.sture.java.budgeting.dto;

import org.sture.java.budgeting.models.TrackingEntry;

import java.util.List;

public class TrackingEntryDTOConverter implements DTOConverter<TrackingEntry, TrackingEntryDTO> {
    @Override
    public TrackingEntryDTO ConvertToDTO(TrackingEntry trackingEntry) {
        return new TrackingEntryDTO(trackingEntry);
    }

    @Override
    public TrackingEntryDTO[] DoMagicToDTO(List<TrackingEntryDTO> lst) {
        return lst.toArray(new TrackingEntryDTO[0]);
    }

    @Override
    public TrackingEntry ConvertFromDTO(TrackingEntryDTO t) {
        return t.Convert();
    }

    @Override
    public TrackingEntry[] DoMagicFromDTO(List<TrackingEntry> lst) {
        return lst.toArray(new TrackingEntry[0]);
    }
}
