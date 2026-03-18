package api.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class AuthService {

    /**
     * dummyjson login — username/password flow
     */
    public TokenResponse fetchTokenDummyJson() {

        String authUrl = ConfigReader.get("auth.url");
        String username = ConfigReader.getOrEnv("auth.username", "AUTH_USERNAME");
        String password = ConfigReader.getOrEnv("auth.password", "AUTH_PASSWORD");

        TokenResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(String.format(
                        "{\"username\":\"%s\",\"password\":\"%s\",\"expiresInMins\":30}",
                        username, password
                ))
                .post(authUrl)
                .as(TokenResponse.class);

        if (response.getToken() == null) {
            throw new RuntimeException("Auth failed! Message: "
                    + response.getMessage());
        }

        return response;
    }

    /**
     * Keycloak OAuth2 — Client Credentials flow
     */
    public TokenResponse fetchTokenKeycloak() {

        String authUrl = ConfigReader.getOrEnv(
                "keycloak.url", "AUTH_URL");
        String clientId = ConfigReader.getOrEnv(
                "keycloak.client.id", "AUTH_CLIENT_ID");
        String clientSecret = ConfigReader.getOrEnv(
                "keycloak.client.secret", "AUTH_CLIENT_SECRET");
        String grantType = ConfigReader.getOrEnv(
                "keycloak.grant.type", "AUTH_GRANT_TYPE");

        // OAuth2 uses form encoding — NOT JSON!
        TokenResponse response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("grant_type", grantType)
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .post(authUrl)
                .as(TokenResponse.class);

        if (response.getToken() == null) {
            throw new RuntimeException("Keycloak auth failed! Error: "
                    + response.getError());
        }

        return response;
    }
}