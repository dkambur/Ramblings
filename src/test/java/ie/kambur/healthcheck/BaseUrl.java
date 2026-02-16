package ie.kambur.healthcheck;

import java.util.Optional;

/**
 * Provides the base URL for integration tests.
 * Reads from environment variable DECK_API_URL or uses default.
 */
public class BaseUrl {

    private static final String ENV_VAR = "DECK_API_URL";
    private static final String DEFAULT_URL = "http://localhost:8080/deck-api";

    public static String get() {
        return Optional.ofNullable(System.getenv(ENV_VAR))
            .orElse(DEFAULT_URL);
    }
}
