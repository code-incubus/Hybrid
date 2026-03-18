package api.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    // Current retry attempt counter — per thread (parallel safe)
    private final ThreadLocal<Integer> retryCount = ThreadLocal.withInitial(() -> 0);

    // Maximum number of retry attempts
    private static final int MAX_RETRY = 2;

    /**
     * Called by TestNG when a test fails.
     * Returns true  → TestNG will retry the test
     * Returns false → TestNG marks test as FAILED
     */
    @Override
    public boolean retry(ITestResult result) {
        int currentCount = retryCount.get();

        if (currentCount < MAX_RETRY) {
            retryCount.set(currentCount + 1);

            System.out.println(">>> RETRY " + retryCount.get() + "/" + MAX_RETRY
                    + " for test: " + result.getMethod().getMethodName()
                    + " | Thread: " + Thread.currentThread().getId());

            return true;   // retry!
        }

        // Reset counter for next test
        retryCount.set(0);
        return false;      // give up
    }
}