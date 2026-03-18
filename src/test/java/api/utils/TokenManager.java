package api.utils;

import java.time.Instant;

public class TokenManager {

    // dummyjson token cache
    private static String dummyJsonToken;
    private static Instant dummyJsonExpiry;

    // Keycloak token cache
    private static String keycloakToken;
    private static Instant keycloakExpiry;

    private static final AuthService authService = new AuthService();
    private static final int DUMMY_JSON_LIFETIME = 1740; // 29 minutes

    /**
     * Get dummyjson token
     */
    public synchronized static String getToken() {
        if (dummyJsonToken == null || isExpired(dummyJsonExpiry)) {
            refreshDummyJsonToken();
        }
        return dummyJsonToken;
    }

    /**
     * Get Keycloak JWT token
     */
    public synchronized static String getKeycloakToken() {
        if (keycloakToken == null || isExpired(keycloakExpiry)) {
            refreshKeycloakToken();
        }
        return keycloakToken;
    }

    private static boolean isExpired(Instant expiry) {
        return expiry == null || Instant.now().isAfter(expiry);
    }

    private static void refreshDummyJsonToken() {
        TokenResponse response = authService.fetchTokenDummyJson();
        dummyJsonToken = response.getToken();
        dummyJsonExpiry = Instant.now().plusSeconds(DUMMY_JSON_LIFETIME);
        System.out.println(">>> DummyJson token refreshed. Expires at: "
                + dummyJsonExpiry);
    }

    private static void refreshKeycloakToken() {
        TokenResponse response = authService.fetchTokenKeycloak();
        keycloakToken = response.getToken();

        // Use expires_in from Keycloak response — 30 second buffer
        int lifetime = response.getExpiresIn() != null
                ? response.getExpiresIn() - 30
                : 270; // fallback 4.5 minutes

        keycloakExpiry = Instant.now().plusSeconds(lifetime);
        System.out.println(">>> Keycloak token refreshed. Expires at: "
                + keycloakExpiry);
    }

    public synchronized static void clear() {
        dummyJsonToken = null;
        dummyJsonExpiry = null;
        keycloakToken = null;
        keycloakExpiry = null;
    }
}