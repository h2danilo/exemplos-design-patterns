package com.example.pedido;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests to ensure the hexagonal architecture is respected.
 */
@SpringBootTest
public class ArchitectureTest {

    @Autowired
    private ApplicationContext context;

    private static final String BASE_PACKAGE = "com.example.pedido";
    private static final String ADAPTER_PACKAGE = BASE_PACKAGE + ".infrastructure.adapter";
    private static final String APPLICATION_PACKAGE = BASE_PACKAGE + ".application";
    private static final String MODEL_PACKAGE = BASE_PACKAGE + ".domain.model";

    /**
     * Test to ensure adapters are not accessed by model or application.
     */
    @Test
    public void adaptersShouldNotBeAccessedByModelOrApplication() throws IOException, ClassNotFoundException {
        Set<Class<?>> modelClasses = findClassesInPackage(MODEL_PACKAGE);
        Set<Class<?>> applicationClasses = findClassesInPackage(APPLICATION_PACKAGE);
        Set<Class<?>> adapterClasses = findClassesInPackage(ADAPTER_PACKAGE);

        // Check model classes don't access adapters
        for (Class<?> modelClass : modelClasses) {
            for (Class<?> adapterClass : adapterClasses) {
                assertClassDoesNotDependOn(modelClass, adapterClass, 
                    "Model class " + modelClass.getName() + " should not depend on adapter class " + adapterClass.getName());
            }
        }

        // Check application classes don't access adapters
        for (Class<?> applicationClass : applicationClasses) {
            // Skip port interfaces as they define the contracts for adapters
            if (applicationClass.getName().contains(".port.")) {
                continue;
            }

            for (Class<?> adapterClass : adapterClasses) {
                assertClassDoesNotDependOn(applicationClass, adapterClass,
                    "Application class " + applicationClass.getName() + " should not depend on adapter class " + adapterClass.getName());
            }
        }
    }

    /**
     * Test to ensure application classes are not accessed by model.
     */
    @Test
    public void applicationsShouldNotBeAccessedByModel() throws IOException, ClassNotFoundException {
        Set<Class<?>> modelClasses = findClassesInPackage(MODEL_PACKAGE);
        Set<Class<?>> applicationClasses = findClassesInPackage(APPLICATION_PACKAGE);

        for (Class<?> modelClass : modelClasses) {
            for (Class<?> applicationClass : applicationClasses) {
                assertClassDoesNotDependOn(modelClass, applicationClass,
                    "Model class " + modelClass.getName() + " should not depend on application class " + applicationClass.getName());
            }
        }
    }

    /**
     * Test to ensure there are no circular dependencies between packages.
     */
    @Test
    public void should_not_have_circular_dependency() throws IOException, ClassNotFoundException {
        Map<String, Set<String>> packageDependencies = new HashMap<>();

        // Collect all classes in the base package
        Set<Class<?>> allClasses = findClassesInPackage(BASE_PACKAGE);

        // Build dependency graph
        for (Class<?> clazz : allClasses) {
            String packageName = clazz.getPackage().getName();
            if (!packageDependencies.containsKey(packageName)) {
                packageDependencies.put(packageName, new HashSet<>());
            }

            // Find dependencies
            Set<String> dependencies = findPackageDependencies(clazz);
            packageDependencies.get(packageName).addAll(dependencies);
        }

        // Check for circular dependencies
        for (String pkg : packageDependencies.keySet()) {
            Set<String> visited = new HashSet<>();
            Set<String> recursionStack = new HashSet<>();

            checkForCircularDependencies(pkg, packageDependencies, visited, recursionStack);
        }
    }

    private void checkForCircularDependencies(String pkg, Map<String, Set<String>> packageDependencies, 
                                             Set<String> visited, Set<String> recursionStack) {
        if (!visited.contains(pkg)) {
            visited.add(pkg);
            recursionStack.add(pkg);

            Set<String> dependencies = packageDependencies.getOrDefault(pkg, Collections.emptySet());
            for (String dependency : dependencies) {
                // Skip self-dependencies (a package depending on itself is not a circular dependency)
                if (dependency.equals(pkg)) {
                    continue;
                }

                if (!visited.contains(dependency)) {
                    checkForCircularDependencies(dependency, packageDependencies, visited, recursionStack);
                } else if (recursionStack.contains(dependency)) {
                    fail("Circular dependency detected: " + pkg + " -> " + dependency);
                }
            }
        }

        recursionStack.remove(pkg);
    }

    private Set<String> findPackageDependencies(Class<?> clazz) {
        Set<String> dependencies = new HashSet<>();

        // Check fields
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (fieldType.getPackage() != null && fieldType.getPackage().getName().startsWith(BASE_PACKAGE)) {
                dependencies.add(fieldType.getPackage().getName());
            }
        }

        // Check methods
        for (Method method : clazz.getDeclaredMethods()) {
            // Return type
            Class<?> returnType = method.getReturnType();
            if (returnType.getPackage() != null && returnType.getPackage().getName().startsWith(BASE_PACKAGE)) {
                dependencies.add(returnType.getPackage().getName());
            }

            // Parameter types
            for (Class<?> paramType : method.getParameterTypes()) {
                if (paramType.getPackage() != null && paramType.getPackage().getName().startsWith(BASE_PACKAGE)) {
                    dependencies.add(paramType.getPackage().getName());
                }
            }
        }

        return dependencies;
    }

    private void assertClassDoesNotDependOn(Class<?> sourceClass, Class<?> targetClass, String message) {
        // Check fields
        for (Field field : sourceClass.getDeclaredFields()) {
            assertFalse(field.getType().equals(targetClass), message + " (field: " + field.getName() + ")");
        }

        // Check methods
        for (Method method : sourceClass.getDeclaredMethods()) {
            // Check return type
            assertFalse(method.getReturnType().equals(targetClass), 
                message + " (method return type: " + method.getName() + ")");

            // Check parameter types
            for (Class<?> paramType : method.getParameterTypes()) {
                assertFalse(paramType.equals(targetClass), 
                    message + " (method parameter: " + method.getName() + ")");
            }
        }
    }

    private Set<Class<?>> findClassesInPackage(String packageName) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        Set<Class<?>> classes = new HashSet<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());
            findClassesInDirectory(directory, packageName, classes);
        }

        return classes;
    }

    private void findClassesInDirectory(File directory, String packageName, Set<Class<?>> classes) throws ClassNotFoundException {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        findClassesInDirectory(file, packageName + "." + file.getName(), classes);
                    } else if (file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        try {
                            Class<?> clazz = Class.forName(className);
                            classes.add(clazz);
                        } catch (NoClassDefFoundError e) {
                            // Skip classes that can't be loaded
                        }
                    }
                }
            }
        }
    }
}
