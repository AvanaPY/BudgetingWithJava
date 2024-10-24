package org.sture.java.budgeting.mock.controller;

import org.sture.java.budgeting.IStatusBarController;

import java.util.HashMap;
import java.util.UUID;

public class StatusBarControllerMock implements IStatusBarController {
    private final HashMap<UUID, Boolean> started;
    private final HashMap<UUID, Boolean> completed;
    private final HashMap<UUID, String> messages;
    private final HashMap<UUID, Double> progress;

    public StatusBarControllerMock(){
        started = new HashMap<>();
        completed = new HashMap<>();
        messages = new HashMap<>();
        progress = new HashMap<>();
    }

    @Override
    public void SetProgressMessageTo(UUID uuid, String msg) {
        messages.put(uuid, msg);
    }

    @Override
    public void SetProgressProgressionTo(UUID uuid, Double progress) {
        this.progress.put(uuid, progress);
    }

    @Override
    public void SetProgressStarted(UUID uuid) {
        started.put(uuid, true);
    }

    @Override
    public void SetProgressCompleted(UUID uuid, String msg) {
        messages.put(uuid, msg);
        completed.put(uuid, true);
    }

    @Override
    public UUID CreateNewProgression() {
        return UUID.randomUUID();
    }

    @Override
    public void DeleteProgressionAfterDelay(UUID jobId, double delaySeconds) {
        DeleteProgression(jobId);
    }

    @Override
    public void DeleteProgression(UUID jobId) {
        started.remove(jobId);
        completed.remove(jobId);
        messages.remove(jobId);
        progress.remove(jobId);
    }
}
