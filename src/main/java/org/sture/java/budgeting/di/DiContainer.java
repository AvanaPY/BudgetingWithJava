package org.sture.java.budgeting.di;
import org.sture.java.budgeting.developer.Developer;
import org.sture.java.budgeting.exceptions.di.FailedToRegisterClassException;
import org.sture.java.budgeting.exceptions.di.FailedToResolveClassException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DiContainer {
    /**
     * Set of all registered interfaced classes
     */
    private final Set<Class<?>> classInterfaces;
    /**
     * Map which maps registered interfaces to RegisterTypes.
     */
    private final Map<Class<?>, RegisterType> registerTypeMap;
    /**
     * Instance cache for singleton-registered interfaces.
     */
    private final Map<Class<?>, Object> singletonRegisteredCache;

    /**
     *  Map of which class to resolve to when resolving an interface.
     */
    private final Map<Class<?>, Class<?>> interfaceClassMap;

    public DiContainer() {
        this.classInterfaces = new HashSet<>();
        this.singletonRegisteredCache = new HashMap<>();
        this.registerTypeMap = new HashMap<>();
        this.interfaceClassMap = new HashMap<>();
    }

    /**
     * @param clazz A class to register.
     * @param registerType The RegisterType of which to register it with.
     * @throws FailedToRegisterClassException If it fails to register the class
     */
    public void Register(Class<?> clazz, RegisterType registerType) throws FailedToRegisterClassException {
        this.Register(clazz, clazz, registerType);
    }

    /**
     * Registers a class which can later be resolved into an instance of said class through {@code ResolveInstance}
     * @param iclazz An interface to register
     * @param clazz A class to register. Must implement {@code iclazz}.
     * @param registerType The RegisterType of which to register it with.
     * @throws FailedToRegisterClassException If it fails to register the class
     */
    public void Register(Class<?> iclazz, Class<?> clazz, RegisterType registerType) throws FailedToRegisterClassException {
        if (classInterfaces.contains(iclazz))
            throw new FailedToRegisterClassException("Class has already been registered");

        classInterfaces.add(iclazz);
        registerTypeMap.put(iclazz, registerType);
        if(iclazz != clazz) {
            interfaceClassMap.put(iclazz, clazz);
            classInterfaces.add(clazz);
        }

        Developer.DebugMessage(this + " registered <" + iclazz + ">/<" + clazz + "> as " + registerType);
    }

    /**
     * Attempts to resolve an instance of a class or interface based on what has been registered so far through {@code Register}
     * @param clazz A class of which to attempt resolving.
     * @param <T> The type to resolve
     * @return An instance of type {@code T}
     * @throws FailedToResolveClassException If it fails to resolve {@code clazz} for any reason
     */
    public <T> T ResolveInstance(Class<T> clazz) throws FailedToResolveClassException {
        Developer.DebugMessage(this + " resolving " + clazz);

        if(singletonRegisteredCache.containsKey(clazz)) {
            Developer.DebugMessage(this + " returns singleton instance of " + clazz);
            return clazz.cast(singletonRegisteredCache.get(clazz));
        }

        Developer.IndentDebugMessagesOnce();
        Constructor<?> constructor = ResolveConstructor(clazz);
        Object[] dependencies = ResolveDependenciesForConstructor(constructor);
        Developer.DeindentDebugMessagesOnce();

        Developer.DebugMessage("  Identified constructor for " + clazz + " has " + dependencies.length + " dependencies");

        T instance;
        try {
            instance = clazz.cast(constructor.newInstance(dependencies));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new FailedToResolveClassException("Constructor could not create instance of " + clazz + ": " + e);
        } catch (InvocationTargetException e){
            throw new FailedToResolveClassException("Failed to invoke constructor " + constructor.getName() + ". Dependencies: " + Arrays.toString(dependencies) + " (" + e + ")");
        }

        if(registerTypeMap.get(clazz) == RegisterType.Singleton) {
            singletonRegisteredCache.put(clazz, instance);
        }

        Developer.DebugMessage(this + " resolved " + clazz);

        return instance;
    }

    /**
     * Tries to resolve all the dependencies (parameters) for a given constructor.
     * @param constructor The constructor of which to resolve its dependencies
     * @return An Object[] with all dependencies for {@code constructor}
     * @throws FailedToResolveClassException If it fails to resolve any of the dependencies
     */
    private Object[] ResolveDependenciesForConstructor(Constructor<?> constructor) throws FailedToResolveClassException {
        Class<?>[] parameterClasses = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterClasses.length];

        for(int i = 0; i < parameterClasses.length; i++) {
            try {
                Developer.DebugMessage("Resolving dependency " + i + " for constructor...");
                Developer.IndentDebugMessagesOnce();
                dependencies[i] = ResolveInstance(parameterClasses[i]);
                Developer.DeindentDebugMessagesOnce();
            }
            catch (FailedToResolveClassException e) {
                throw new FailedToResolveClassException("Failed to resolve dependency: " + e.getMessage());
            }
        }
        return dependencies;
    }

    /**
     * @param clazz The interface or class of which to identify a constructor for
     * @return An instance of a Constructor<?> for clazz
     * @throws FailedToResolveClassException if clazz has more than 1 constructor or a constructor cannot be inferred for a registered interface
     */
    private Constructor<?> ResolveConstructor(Class<?> clazz) throws FailedToResolveClassException {
        // Trying to resolve a constructor for a null class? why
        if(clazz == null)
            return null;

        // Trying to resolve a constructor for a non-registered type? Uhh, handleable but fuck you
        if(!classInterfaces.contains(clazz))
            throw new FailedToResolveClassException(clazz  + " has not been registered");

        Constructor<?>[] constructors = clazz.getConstructors();

        // Just crash if we have more than one constructor; we don't know which to choose from
        if (constructors.length > 1) {
            throw new FailedToResolveClassException("Class " + clazz.getName() + " has more than one constructors");
        }

        // If we have 0 constructors we will check if it is an interface we are trying to resolve
        // and resolve the constructor the registered class instead
        if(constructors.length == 0) {
            Class<?> c = interfaceClassMap.get(clazz);
            Developer.DebugMessage(clazz + " has no constructors and interface mapping yielded " + c);
            return ResolveConstructor(c);
        }

        // Guaranteed to only have 1 constructor here located at index 0
        return constructors[0];
    }
}
