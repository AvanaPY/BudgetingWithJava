package com.we.suck.at.java.budgettingiguess.services;

import com.we.suck.at.java.budgettingiguess.dto.TrackingEntryDTOFactory;
import com.we.suck.at.java.budgettingiguess.models.BudgetType;
import com.we.suck.at.java.budgettingiguess.models.TrackingEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.we.suck.at.java.budgettingiguess.BaseTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrackingEntryStoreServiceTest extends BaseTest {
    private TrackingEntryDTOFactory dtoFactory;
    private TrackingEntryStoreService storeService;
    @BeforeEach
    void setUp() {
        dtoFactory = new TrackingEntryDTOFactory();
        storeService = new TrackingEntryStoreService(dtoFactory);
    }

    @AfterEach
    void tearDown() {
        storeService.DeleteStoreIfExists();
    }

    @Test
    void TestSerializeAndDeserializeSingleEntry() {
        var entry = new TrackingEntry(
                LocalDate.of(2024, 10, 22),
                LocalDate.of(2024, 10, 22),
                BudgetType.Income,
                "Employment",
                "Details",
                100.0d,
                100.0d
        );

        // Write
        storeService.Store(entry);

        // Read
        var deserialized = storeService.Read();
        assertEquals(1, deserialized.length);
        assertEquals(entry, deserialized[0], "Objects differ: " + entry + " -> " + deserialized[0]);
    }

    @Test
    void TestSerializeAndDeserializeMultipleObjects(){
        var entries = new TrackingEntry[]{
                generateTrackingEntry(),
                generateTrackingEntry(),
                generateTrackingEntry(),
                generateTrackingEntry(),
                generateTrackingEntry()
        };

        // Write
        storeService.Store(entries);

        // Read
        var objects = storeService.Read();
        assertEquals(entries.length, objects.length);
        assertArrayEquals(entries, objects);
    }

    @Test
    void TestWriteEntriesAndDeleteStore(){
        var entries = new TrackingEntry[]{
                generateTrackingEntry(),
                generateTrackingEntry(),
                generateTrackingEntry()
        };

        // Write
        storeService.Store(entries);

        // Remove once
        assertTrue(storeService.DeleteStoreIfExists());

        // Remove second and expect false
        assertFalse(storeService.DeleteStoreIfExists());
    }

    private TrackingEntry generateTrackingEntry(){
        return new TrackingEntry(
                LocalDate.of(2024, 10, 22),
                LocalDate.of(2024, 10, 22),
                BudgetType.Income,
                "Employment",
                "Details",
                42,
                69
        );
    }
}