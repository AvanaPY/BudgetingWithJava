package org.sture.java.budgeting.services.job;

public interface JobCallback<T>  {
    void Run(T t);
}