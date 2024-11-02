package org.sture.java.budgeting.exceptions.di;

public class FailedToRegisterClassException extends DiException {
    public FailedToRegisterClassException(String message, Exception cause) {
        super(message, cause);
    }
    public FailedToRegisterClassException(String message){
        this(message, null);
    }
}
