package api.utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class AuthService {

    public TokenResponse fetchToken() {
        String authUrl = ConfigReader.getOrEnv("auth.url", "AUTH_URL");
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

        // If login failed, dummyjson returns message instead of token
        if (response.getAccessToken() == null) {
            throw new RuntimeException("Auth failed! Message: "
                    + response.getMessage());
        }

        return response;
    }
}