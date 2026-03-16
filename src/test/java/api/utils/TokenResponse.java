package api.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {
    // dummyjson returns "accessToken" (JWT)
    private String accessToken;
    // dummyjson returns "refreshToken"
    private String refreshToken;
    // Returned if login fails
    private String message;
}