package api.utils.listeners;

import api.tests.KeycloakTests;
import api.utils.RetryAnalyzer;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class RetryListener implements IAnnotationTransformer {

    /**
     * Called by TestNG for every @Test method before execution.
     * Automatically applies RetryAnalyzer to ALL tests
     * without manually adding retryAnalyzer = RetryAnalyzer.class
     */
    @Override
    public void transform(ITestAnnotation annotation,
                          Class testClass,
                          Constructor testConstructor,
                          Method testMethod) {

        // Skip retry for Keycloak tests
        // Keycloak may not be available on retry attempts
        if (testClass != null && testClass.equals(KeycloakTests.class)) {
            return;
        }

        // Apply RetryAnalyzer to every @Test automatically
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }
}
