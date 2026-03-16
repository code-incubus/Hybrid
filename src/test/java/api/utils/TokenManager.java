package api.utils;

import java.time.Instant;

public class TokenManager {

    private static String token;
    private static Instant expiry;
    private static final AuthService authService = new AuthService();

    // dummyjson token expires in 30min — we set 29min to avoid edge cases
    private static final int TOKEN_LIFETIME_SECONDS = 1740;

    // synchronized — only one thread can execute this at a time
    // prevents multiple threads from fetching token simultaneously
    public synchronized static String getToken() {
        if (token == null || isExpired()) {
            refresh();
        }
        return token;
    }

    private static boolean isExpired() {
        return expiry == null || Instant.now().isAfter(expiry);
    }

    private static void refresh() {
        TokenResponse response = authService.fetchToken();
        token = response.getAccessToken();
        expiry = Instant.now().plusSeconds(TOKEN_LIFETIME_SECONDS);
        System.out.println(">>> Token refreshed. Expires at: " + expiry);
    }

    // Force token refresh — useful in @BeforeSuite
    public synchronized static void forceRefresh() {
        token = null;
        expiry = null;
        refresh();
    }

    // Clear token — useful in @AfterSuite
    public synchronized static void clear() {
        token = null;
        expiry = null;
    }
}