package com.we.suck.at.java.budgettingiguess.utils;

public interface ProgressOperator {
    void OnStartProgress();
    void OnProgressUpdate(String status, double progress);
    void OnProgressComplete();
}