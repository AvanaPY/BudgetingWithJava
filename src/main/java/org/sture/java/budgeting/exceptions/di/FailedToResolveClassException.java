package org.sture.java.budgeting.exceptions.di;

public class FailedToResolveClassException extends RuntimeException {
    public FailedToResolveClassException(String message) {
        super(message);
    }
}
