package org.sture.java.budgeting;

public interface DownloadController {
    void startProgress();
    void setCurrentProgress(double progress);
    void onProgressComplete();
}
