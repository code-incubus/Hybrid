package api.utils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {

    /**
     * Handles both formats:
     * dummyjson → "accessToken" (camelCase)
     * Keycloak  → "access_token" (snake_case)
     *
     * @JsonProperty sets primary name
     * @JsonAlias sets alternative names Jackson will also accept
     */
    @JsonProperty("access_token")
    @JsonAlias({"accessToken", "access_token"})
    private String accessToken;

    // Keycloak returns expires_in (seconds)
    @JsonProperty("expires_in")
    private Integer expiresIn;

    // Error messages
    private String message;
    private String error;

    /**
     * Returns token regardless of format
     */
    public String getToken() {
        return accessToken;
    }
}