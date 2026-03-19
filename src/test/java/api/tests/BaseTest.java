package api.tests;

import api.base.BaseRequest;
import api.exceptions.ApiException;
import api.exceptions.AuthenticationException;
import api.exceptions.ConfigurationException;
import api.utils.TokenManager;
import io.qameta.allure.Allure;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.logging.Logger;

public class BaseTest {

    protected static final Logger logger =
            Logger.getLogger(BaseTest.class.getName());

    @BeforeSuite
    public void suiteSetup() {
        logger.info(">>> Test Suite started.");
    }

    @BeforeMethod
    public void testSetup(ITestResult result) {
        logger.info(">>> Running: ["
                + result.getMethod().getMethodName() + "]"
                + " | Thread: " + Thread.currentThread().getId());
    }

    @AfterMethod
    public void testTeardown(ITestResult result) {

        // Calculate test duration in SEC
        double duration = (result.getEndMillis() - result.getStartMillis()) / 1000.0;
        String testName = result.getMethod().getMethodName();

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                logger.info("✅ PASSED: [" + testName + "] | Duration: " + duration + "ms");
                break;

            case ITestResult.FAILURE:
                Throwable cause = result.getThrowable();
                String failureType = resolveFailureType(cause);
                String message = "❌ FAILED " + failureType + ": [" + testName + "]\n" + cause.getMessage();
                logger.severe(message);
                Allure.addAttachment("Failure reason", cause.getMessage());
                break;

            case ITestResult.SKIP:
                logger.warning("⚠️ SKIPPED: [" + testName + "]");
                break;
        }

        // Clean up ThreadLocal after each test — prevents memory leaks
        BaseRequest.clear();
    }

    @AfterSuite
    public void suiteTeardown() {
        // Clear cached token after all tests
        TokenManager.clear();
        logger.info(">>> Test Suite finished. Token cleared.");
    }

    /**
     * Resolves failure type label based on exception class.
     * Returns a short label for logging.
     */
    private String resolveFailureType(Throwable cause) {
        if (cause instanceof AuthenticationException) return "(Auth)";
        if (cause instanceof ConfigurationException) return "(Config)";
        if (cause instanceof ApiException) return "(API)";
        return "";
    }
}