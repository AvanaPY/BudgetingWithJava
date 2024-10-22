package org.sture.java.budgeting.utils;

public interface ProgressOperator {
    void OnStartProgress();
    void OnProgressUpdate(String status, double progress);
    void OnProgressComplete();
}