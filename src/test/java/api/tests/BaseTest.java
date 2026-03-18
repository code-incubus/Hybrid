package api.tests;

import api.base.BaseRequest;
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

        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                logger.info("✅ PASSED: ["
                        + result.getMethod().getMethodName() + "]"
                        + " | Duration: " + duration + "s");
                break;

            case ITestResult.FAILURE:
                // Attach failure reason to Allure report
                Allure.addAttachment(
                        "Failure reason",
                        result.getThrowable().getMessage()
                );
                logger.severe("❌ FAILED: ["
                        + result.getMethod().getMethodName() + "]"
                        + " | Duration: " + duration + "s"
                        + " | Reason: " + result.getThrowable().getMessage());
                break;

            case ITestResult.SKIP:
                logger.warning("⚠️ SKIPPED: ["
                        + result.getMethod().getMethodName() + "]");
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
}