package api.tests;

import api.base.BaseRequest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.util.logging.Logger;

public class BaseTest {

    protected static final Logger logger = Logger.getLogger(BaseTest.class.getName());

    @BeforeSuite
    public void suiteSetup() {
        logger.info(">>> Test Suite started. Loading configuration...");
    }

    @BeforeMethod
    public void testSetup(ITestResult result) {
        logger.info(">>> Running: " + result.getMethod().getMethodName());
    }

    @AfterMethod
    public void testTeardown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.severe("!!! FAILED: " + result.getMethod().getMethodName());
        }
        // Clean up ThreadLocal after each test
        BaseRequest.clear();
        logger.info(">>> Finished: " + result.getMethod().getMethodName());
    }

    @AfterSuite
    public void suiteTeardown() {
        logger.info(">>> Test Suite finished.");
    }
}