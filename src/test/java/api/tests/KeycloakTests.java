package api.tests;

import api.utils.AuthService;
import api.utils.TokenManager;
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

        TokenResponse response = authService.fetchTokenKeycloak();

        Assert.assertNotNull(response.getToken(),
                "Token should not be null!");
        Assert.assertTrue(response.getToken().startsWith("eyJ"),
                "Token should be a JWT!");
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

        String[] parts = token.split("\\.");
        Assert.assertEquals(parts.length, 3,
                "JWT should have 3 parts: header.payload.signature");

        logger.info("✅ Token is valid JWT!"
                + " | Header: " + parts[0].length()
                + " | Payload: " + parts[1].length());
    }

    @Test
    @Story("Token caching")
    public void testKeycloakTokenIsCached() {

        // Both calls through TokenManager — ensures caching is tested
        String token1 = TokenManager.getKeycloakToken();  // fetchuje i kešira
        String token2 = TokenManager.getKeycloakToken();  // vraća iz keša

        Assert.assertEquals(token1, token2,
                "Token should be cached — same token on both calls!");
        logger.info("✅ Token caching works!");
    }
}