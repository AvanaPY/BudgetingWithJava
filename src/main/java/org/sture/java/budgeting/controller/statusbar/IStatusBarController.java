package org.sture.java.budgeting.controller.statusbar;

import org.sture.java.budgeting.controller.iface.BudgetController;

import java.util.UUID;

public interface IStatusBarController extends BudgetController {
    void SetProgressMessageTo(UUID uuid, String msg);
    void SetProgressProgressionTo(UUID uuid, Double progress);
    void SetProgressStarted(UUID uuid);
    void SetProgressCompleted(UUID uuid, String msg);
    UUID CreateNewProgression();
    void DeleteProgressionAfterDelay(UUID jobId, double delaySeconds);
    void DeleteProgression(UUID jobId);
}
