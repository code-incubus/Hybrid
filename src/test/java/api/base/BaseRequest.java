package api.base;

import api.utils.ConfigReader;
import api.utils.TokenManager;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseRequest {

    // ThreadLocal ensures each parallel thread has its OWN request spec
    // Without this, parallel tests would overwrite each other's spec
    private static final ThreadLocal<RequestSpecification> requestSpec = new ThreadLocal<>();

    public static RequestSpecification getSpec(AuthType authType) {

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getOrEnv("base.url", "BASE_URL"))
                .setContentType(ContentType.JSON)
                .addFilter(new AllureRestAssured());

        switch (authType) {

            case BEARER:
                builder.addHeader("Authorization",
                        "Bearer " + TokenManager.getToken());
                break;

            case BEARER_KEYCLOAK:
                // Uses Keycloak JWT token
                builder.addHeader("Authorization",
                        "Bearer " + TokenManager.getKeycloakToken());
                break;

            case NO_AUTH:
                break;
        }

        requestSpec.set(builder.build());
        return requestSpec.get();
    }

    // Called after each test to prevent memory leaks in parallel execution
    public static void clear() {
        requestSpec.remove();
    }
}