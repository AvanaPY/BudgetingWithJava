package com.we.suck.at.java.budgettingiguess;

public interface DownloadController {
    void startProgress();
    void setCurrentProgress(double progress);
    void onProgressComplete();
}
