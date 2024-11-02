package org.sture.java.budgeting.exceptions.di;

public class DiException extends RuntimeException {
    public DiException(String message, Exception cause) {
        super(message, cause);
    }
}
