package org.sture.java.budgeting.exceptions.di;

public class ClassNotRegisteredException extends RuntimeException {
    public ClassNotRegisteredException(Class<?> clazz) {
        super("Class not registered: " + clazz.getSimpleName());
    }
}
