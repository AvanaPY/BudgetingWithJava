package org.sture.java.budgeting.exceptions.di;

public class FailedToResolveClassException extends DiException {
    public FailedToResolveClassException(Object clazz, String message, Exception cause) {
        super("Failed to resolve class: " + clazz + ": " + message, cause);
    }
    public FailedToResolveClassException(Class<?> clazz, Exception cause){
        this(clazz, "", cause);
    }
    public FailedToResolveClassException(Object clazz, String message){
        this(clazz, message, null);
    }
}
