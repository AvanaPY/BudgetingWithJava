package org.sture.java.budgeting.services;

import org.sture.java.budgeting.mock.controller.StatusBarControllerMock;
import org.sture.java.budgeting.services.job.BackgroundJobExecutionService;
import org.sture.java.budgeting.services.tracking.models.BudgetEntryCategory;
import org.sture.java.budgeting.store.tracking.dto.TrackingEntryDTOConverter;
import org.sture.java.budgeting.services.tracking.models.TrackingEntry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sture.java.budgeting.BaseTest;
import org.sture.java.budgeting.store.tracking.service.TrackingEntryStoreService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrackingEntryStoreServiceTest extends BaseTest {
    private TrackingEntryStoreService storeService;

    @BeforeEach
    void setUp() {
        storeService = new TrackingEntryStoreService(
                new TrackingEntryDTOConverter(),
                new BackgroundJobExecutionService(new StatusBarControllerMock())
        );
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
                new BudgetEntryCategory("Income", true),
                new BudgetEntryCategory("Employment"),
                "Details",
                100.0d,
                100.0d
        );

        // Write
        storeService.Store(entry);
        storeService.WaitForBackgroundJobToComplete();

        // Read
        var deserialized = storeService.Read();
        assertNotNull(deserialized);
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
        storeService.WaitForBackgroundJobToComplete();

        // Read
        var objects = storeService.Read();
        assertNotNull(objects);
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
        storeService.WaitForBackgroundJobToComplete();

        // Remove once
        assertTrue(storeService.DeleteStoreIfExists());

        // Remove second and expect false
        assertFalse(storeService.DeleteStoreIfExists());
    }

    private TrackingEntry generateTrackingEntry(){
        return new TrackingEntry(
                LocalDate.of(2024, 10, 22),
                LocalDate.of(2024, 10, 22),
                new BudgetEntryCategory("Income", true),
                new BudgetEntryCategory("Employment"),
                "Details",
                42,
                69
        );
    }
}