package api.exceptions;

/**
 * Base exception for all framework-related errors.
 * Extends RuntimeException — no need for try/catch everywhere.
 * <p>
 * All custom exceptions in this framework extend this class,
 * allowing callers to catch either:
 * - Specific exception (AuthenticationException)
 * - Or any framework exception (FrameworkException)
 */
public class FrameworkException extends RuntimeException {

    public FrameworkException(String message) {
        super(message);
    }

    // Preserves original cause - important for stack trace!
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }
}