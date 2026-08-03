package com.cineast.patterns;

/**
 * SINGLETON PATTERN
 * A single shared configuration object for the TMDb API client.
 * Ensures only one instance exists across the entire app — thread-safe
 * via double-checked locking.
 *
 * In Spring Boot we get the API key from application.properties,
 * but the Singleton pattern is demonstrated explicitly here for the
 * academic requirement.
 */
public class TMDbClientConfig {

    private static volatile TMDbClientConfig instance;

    private final String apiKey;
    private final String baseUrl;
    private final int    timeoutSeconds;

    private TMDbClientConfig(String apiKey, String baseUrl, int timeoutSeconds) {
        this.apiKey          = apiKey;
        this.baseUrl         = baseUrl;
        this.timeoutSeconds  = timeoutSeconds;
    }

    /**
     * Returns the one shared instance, creating it on first call.
     * Thread-safe — multiple concurrent requests always get the same object.
     */
    public static TMDbClientConfig getInstance(String apiKey, String baseUrl) {
        if (instance == null) {
            synchronized (TMDbClientConfig.class) {
                if (instance == null) {
                    instance = new TMDbClientConfig(apiKey, baseUrl, 10);
                }
            }
        }
        return instance;
    }

    /** For testing only — resets the singleton so tests start fresh. */
    public static void resetInstance() {
        instance = null;
    }

    public String getApiKey()        { return apiKey; }
    public String getBaseUrl()       { return baseUrl; }
    public int    getTimeoutSeconds(){ return timeoutSeconds; }

    public String buildUrl(String endpoint) {
        return baseUrl + endpoint + "?api_key=" + apiKey;
    }
}
