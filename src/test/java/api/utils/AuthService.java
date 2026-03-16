package api.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class AuthService {

    private static final String AUTH_URL =
            ConfigReader.getOrEnv("auth.url", "AUTH_URL");

    public TokenResponse fetchToken() {

        String username = ConfigReader.getOrEnv("auth.username", "AUTH_USERNAME");
        String password = ConfigReader.getOrEnv("auth.password", "AUTH_PASSWORD");

        TokenResponse response = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(String.format(
                        "{\"username\":\"%s\",\"password\":\"%s\",\"expiresInMins\":30}",
                        username, password
                ))
                .post(AUTH_URL)
                .as(TokenResponse.class);

        // If login failed, dummyjson returns message instead of token
        if (response.getAccessToken() == null) {
            throw new RuntimeException("Auth failed! Message: "
                    + response.getMessage());
        }

        return response;
    }
}