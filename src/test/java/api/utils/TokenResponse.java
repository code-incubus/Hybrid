package api.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {

    // dummyjson returns "accessToken"
    // Keycloak returns "access_token"
    @JsonProperty("access_token")
    private String accessToken;

    // Keycloak returns expires_in (seconds)
    @JsonProperty("expires_in")
    private Integer expiresIn;

    // dummyjson returns "accessToken" (camelCase)
    // Jackson will try both due to @JsonProperty
    private String accessTokenCamel;

    // Error message
    private String message;
    private String error;

    /**
     * Returns token regardless of which field is populated
     * Handles both dummyjson and Keycloak response formats
     */
    public String getToken() {
        if (accessToken != null) return accessToken;
        if (accessTokenCamel != null) return accessTokenCamel;
        return null;
    }
}