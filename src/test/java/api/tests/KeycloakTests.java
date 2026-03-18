package api.tests;

import api.utils.AuthService;
import api.utils.TokenResponse;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Keycloak OAuth2")
public class KeycloakTests extends BaseTest {

    private final AuthService authService = new AuthService();

    @Test
    @Story("Client Credentials Flow")
    public void testKeycloakTokenIsObtained() {

        // Directly test token fetching from Keycloak
        TokenResponse response = authService.fetchTokenKeycloak();

        // Verify token exists
        Assert.assertNotNull(response.getToken(),
                "Token should not be null!");

        // Verify it's a JWT (starts with eyJ)
        Assert.assertTrue(response.getToken().startsWith("eyJ"),
                "Token should be a JWT!");

        // Verify expiry
        Assert.assertNotNull(response.getExpiresIn(),
                "expires_in should not be null!");
        Assert.assertTrue(response.getExpiresIn() > 0,
                "expires_in should be greater than 0!");

        logger.info("✅ Keycloak token obtained!"
                + " | expires_in: " + response.getExpiresIn() + "s"
                + " | token: " + response.getToken().substring(0, 20) + "...");
    }

    @Test
    @Story("Client Credentials Flow")
    public void testKeycloakTokenIsJWT() {

        TokenResponse response = authService.fetchTokenKeycloak();
        String token = response.getToken();

        // JWT has exactly 3 parts separated by "."
        String[] parts = token.split("\\.");
        Assert.assertEquals(parts.length, 3,
                "JWT should have exactly 3 parts: header.payload.signature");

        logger.info("✅ Token is valid JWT format!"
                + " | Header length: " + parts[0].length()
                + " | Payload length: " + parts[1].length());
    }

    @Test
    @Story("Token caching")
    public void testKeycloakTokenIsCached() {

        // First call — fetches from Keycloak
        TokenResponse response1 = authService.fetchTokenKeycloak();

        // Second call — should be same token (from cache via TokenManager)
        String token1 = response1.getToken();
        String token2 = api.utils.TokenManager.getKeycloakToken();

        Assert.assertEquals(token1, token2,
                "Token should be cached — same token on second call!");

        logger.info("✅ Token caching works!");
    }
}