package org.sture.java.budgeting;

import java.util.UUID;

public interface IStatusBarController {
    void SetProgressMessageTo(UUID uuid, String msg);
    void SetProgressProgressionTo(UUID uuid, Double progress);
    void SetProgressStarted(UUID uuid);
    void SetProgressCompleted(UUID uuid, String msg);
    UUID CreateNewProgression();
    void DeleteProgressionAfterDelay(UUID jobId, double delaySeconds);
    void DeleteProgression(UUID jobId);
}
