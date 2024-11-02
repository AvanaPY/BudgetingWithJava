package org.sture.java.budgeting.di;
import org.sture.java.budgeting.controller.iface.BudgetController;
import org.sture.java.budgeting.developer.Developer;
import org.sture.java.budgeting.exceptions.di.*;

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

    /** Registers a direct instance of an object for its class
     * @param clazz The interface to register for
     * @param instance The instance which to resolve
     */
    public <T> void Register(Class<T> clazz, T instance) {
        if(instance.getClass().isInstance(clazz))
            throw new FailedToRegisterClassException("Cannot register " + instance + " as instance of class " + clazz.getSimpleName());
        storeInstanceOfSingleton(clazz, instance);
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
     * Registers and initializes a JavaFX controller along with its instance.
     * @param iController A controller interface
     * @param controllerInstance A controller instance
     */
    public <T extends BudgetController> void RegisterController(Class<T> iController, T controllerInstance) {
        controllerInstance.InitializeControllerWithContainer(this);
        Register(iController, controllerInstance);
    }

    /**
     * Attempts to resolve an instance of a class or interface based on what has been registered so far through {@code Register}
     * @param clazz A class of which to attempt resolving.
     * @param <T> The type to resolve
     * @return An instance of type {@code T}
     * @throws FailedToResolveClassException If it fails to resolve {@code clazz} for any reason
     */
    public <T> T ResolveInstance(Class<T> clazz) throws FailedToResolveClassException, ClassNotRegisteredException {
        Developer.DebugMessage("Resolving " + clazz.getSimpleName(), true);

        T instance = null;

        if(singletonRegisteredCache.containsKey(clazz)) {
            Developer.DebugMessage("Returns singleton instance of " + clazz.getSimpleName());
            instance = clazz.cast(singletonRegisteredCache.get(clazz));
        }

        if(instance == null && !classInterfaces.contains(clazz))
            throw new ClassNotRegisteredException(clazz);

        if (instance == null)
        {
            try {
                Constructor<?> constructor = ResolveConstructor(clazz);

                Object[] dependencies = ResolveDependenciesForConstructor(constructor);
                Developer.DebugMessage("Found constructor and dependencies for " + clazz.getSimpleName());

                instance = clazz.cast(constructor.newInstance(dependencies));
            } catch (ClassNotRegisteredException e) {
                throw new FailedToResolveClassException(clazz, e);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new FailedToResolveClassException(clazz, "Constructor could not create instance of class", e);
            }

            Developer.DebugMessage("Resolved " + clazz.getSimpleName() + " as " + instance);

            if(registerTypeMap.get(clazz) == RegisterType.Singleton) {
                storeInstanceOfSingleton(clazz, instance);
                Developer.IndentDebugMessagesOnce();
                Developer.DebugMessage("Caching instance as Singleton");
                Developer.DeindentDebugMessagesOnce();
            }
        }

        // ´instance´ cannot be null here
        Developer.DeindentDebugMessagesOnce();
        Developer.DebugMessage("Resolve returning (" + clazz.getSimpleName() + ") " + instance);

        return instance;
    }

    private <T> void storeInstanceOfSingleton(Class<?> clazz, T instance){
        if(singletonRegisteredCache.containsKey(clazz)){
            throw new RuntimeException("Failed to store instance: Class " + clazz + " has already been stored once before");
        }
        singletonRegisteredCache.put(clazz, instance);
    }

    /**
     * Tries to resolve all the dependencies (parameters) for a given constructor.
     * @param constructor The constructor of which to resolve its dependencies
     * @return An Object[] with all dependencies for {@code constructor}
     * @throws FailedToResolveClassException If it fails to resolve any of the dependencies
     */
    private Object[] ResolveDependenciesForConstructor(Constructor<?> constructor) throws FailedToResolveDependencyForConstructor {
        Class<?>[] parameterClasses = constructor.getParameterTypes();
        Object[] dependencies = new Object[parameterClasses.length];

        for(int i = 0; i < parameterClasses.length; i++) {
            try {
                Developer.DebugMessage("Resolving dependency " + (i+1) + "/" + parameterClasses.length + " for constructor <" + constructor.toGenericString() + ">");
                Developer.IndentDebugMessagesOnce();
                dependencies[i] = ResolveInstance(parameterClasses[i]);
                Developer.DeindentDebugMessagesOnce();
            }
            catch(ClassNotRegisteredException | FailedToResolveClassException e) {
                throw new FailedToResolveDependencyForConstructor(constructor, parameterClasses[i].getSimpleName(), e);
            }
        }
        return dependencies;
    }

    /**
     * @param clazz The interface or class of which to identify a constructor for
     * @return An instance of a Constructor<?> for clazz
     * @throws FailedToResolveClassException if clazz has more than 1 constructor or a constructor cannot be inferred for a registered interface
     */
    private Constructor<?> ResolveConstructor(Class<?> clazz) throws ClassNotRegisteredException, FailedToResolveClassException {
        // Trying to resolve a constructor for a null class? why
        if(clazz == null)
            return null;

        // Trying to resolve a constructor for a non-registered type? Uhh, handleable but fuck you
        if(!classInterfaces.contains(clazz))
            throw new ClassNotRegisteredException(clazz);

        Constructor<?>[] constructors = clazz.getConstructors();

        // Just crash if we have more than one constructor; we don't know which to choose from
        if (constructors.length > 1) {
            throw new FailedToResolveClassException(clazz.getSimpleName(), "Class has more than one constructor");
        }

        // If we have 0 constructors we will check if it is an interface we are trying to resolve
        // and resolve the constructor the registered class instead
        if(constructors.length == 0) {
            Class<?> c = interfaceClassMap.get(clazz);
            Developer.DebugMessage(clazz.getSimpleName() + " has no constructors and interface mapping yielded " + c);
            return ResolveConstructor(c);
        }

        // Guaranteed to only have 1 constructor here located at index 0
        return constructors[0];
    }
}
