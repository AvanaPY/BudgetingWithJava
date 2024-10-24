package org.sture.java.budgeting.services.job;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sture.java.budgeting.mock.controller.StatusBarControllerMock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BackgroundJobExecutionServiceTest {
    private BackgroundJobExecutionService backgroundJobExecutionService;
    private volatile boolean receivedOnDoneCallback;
    private volatile boolean receivedlockThreadUntilOkCallback;
    private volatile boolean threadMayRelease = false;

    @BeforeEach
    void setup(){
        backgroundJobExecutionService = new BackgroundJobExecutionService(new StatusBarControllerMock());
        receivedOnDoneCallback = false;
        receivedlockThreadUntilOkCallback = false;
        threadMayRelease = false;
    }

    @AfterEach
    void tearDown(){
        backgroundJobExecutionService = null;
    }

    @Test
    void LaunchJobCheckSameId() {
        UUID uuid = UUID.fromString("0000-00-00-00-000000");
        Job job = new Job(uuid) {
            @Override
            public void Execute() {}
        };
        UUID jobId = backgroundJobExecutionService.LaunchBackgroundJob(job);
        assertEquals(jobId, uuid);
    }

    @Test
    void LaunchJobVerifyPossibleToWaitForJobFinish(){

        UUID uuid = UUID.fromString("0000-00-00-00-000000");
        Job job = new Job(uuid) {
            @Override
            public void Execute() {}
        };

        job.setupOnDoneCallback(this::JobOnDoneCallback);
        job.setupMessageCallback(this::lockThreadUntilOkCallback);
        UUID jobId = backgroundJobExecutionService.LaunchBackgroundJob(job);

        assertFalse(receivedOnDoneCallback);
        assertTrue(backgroundJobExecutionService.BackgroundJobIsActive(jobId));

        threadMayRelease = true;
        backgroundJobExecutionService.WaitForJobToFinish(jobId);

        assertTrue(receivedOnDoneCallback);
        assertTrue(receivedlockThreadUntilOkCallback);
        assertFalse(backgroundJobExecutionService.BackgroundJobIsActive(jobId));
    }

    private void JobOnDoneCallback(String msg){
        receivedOnDoneCallback = true;
    }

    private void lockThreadUntilOkCallback(String msg){
        receivedlockThreadUntilOkCallback = true;
        while (!threadMayRelease) {
            Thread.onSpinWait();
        }
    }
}