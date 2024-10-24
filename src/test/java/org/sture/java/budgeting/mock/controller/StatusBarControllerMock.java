package org.sture.java.budgeting.mock.controller;

import org.sture.java.budgeting.IStatusBarController;

import java.util.UUID;

public class StatusBarControllerMock implements IStatusBarController {
    @Override
    public void SetProgressMessageTo(UUID uuid, String msg) {
    }

    @Override
    public void SetProgressProgressionTo(UUID uuid, Double progress) {
    }

    @Override
    public void SetProgressStarted(UUID uuid) {
    }

    @Override
    public void SetProgressCompleted(UUID uuid, String msg) {
    }

    @Override
    public UUID CreateNewProgression() {
        return UUID.randomUUID();
    }

    @Override
    public void DeleteProgressionAfterDelay(UUID jobId, double delaySeconds) {
    }

    @Override
    public void DeleteProgression(UUID jobId) {
    }
}
