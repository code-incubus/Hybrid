package api.exceptions;

/**
 * Thrown when authentication fails.
 * <p>
 * Common causes:
 * - Invalid username/password (dummyjson)
 * - Invalid client_id/client_secret (Keycloak)
 * - Auth server is down
 * - Token is null in response
 */
public class AuthenticationException extends FrameworkException {

    private final String authUrl;

    public AuthenticationException(String authUrl, String reason) {
        super("Authentication failed!" +
                "\n  Auth URL: " + authUrl +
                "\n  Reason:   " + reason);
        this.authUrl = authUrl;
    }

    public String getAuthUrl() {
        return authUrl;
    }
}