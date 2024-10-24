package org.sture.java.budgeting.services.job;

public interface ProgressOperator {
    void OnStartProgress();
    void OnProgressUpdate(String status, double progress);
    void OnProgressComplete();
}