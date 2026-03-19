package api.exceptions;

/**
 * Thrown when API returns an unexpected response.
 * <p>
 * Common causes:
 * - Server is down (503)
 * - Connection timeout
 * - Unexpected status code
 */
public class ApiException extends FrameworkException {

    private final int statusCode;
    private final String endpoint;

    public ApiException(String endpoint, int statusCode, String responseBody) {
        super("API call failed!" +
                "\n  Endpoint:    " + endpoint +
                "\n  Status code: " + statusCode +
                "\n  Response:    " + responseBody);
        this.statusCode = statusCode;
        this.endpoint = endpoint;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getEndpoint() {
        return endpoint;
    }
}