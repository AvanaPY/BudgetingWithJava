package org.sture.java.budgeting.exceptions.di;

public class FailedToResolveDependencyForConstructor extends DiException {
    public FailedToResolveDependencyForConstructor(Object constructor, Object dependency, Exception e){
        super("Failed to resolve dependency \"" + dependency + "\" for constructor " + constructor, e);
    }
    public FailedToResolveDependencyForConstructor(Object constructor, Object dependency) {
        this(constructor, dependency, null);
    }
}
