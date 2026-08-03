package com.cineast.patterns;

import com.cineast.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * FACADE PATTERN
 * MovieExplorationFacade is the single class that controllers call.
 * It hides:
 *   - HTTP connections to the TMDb API
 *   - JSON parsing
 *   - The Adapter (TMDbMovieAdapter)
 *   - The Singleton config (TMDbClientConfig)
 *
 * Controllers never touch JSONObject or HttpURLConnection directly.
 */
@Component
public class MovieExplorationFacade {

    private final TMDbClientConfig config;

    public MovieExplorationFacade(
            @Value("${tmdb.api.key:258a95ca5535c2d4861066d14589a2d5}") String apiKey,
            @Value("${tmdb.base.url:https://api.themoviedb.org/3}") String baseUrl) {
        // Uses the Singleton to get one shared config instance
        this.config = TMDbClientConfig.getInstance(apiKey, baseUrl);
    }

    // ── Public facade methods (what controllers call) ──────────────────────

    public List<Movie> getTrending() {
        return fetchList("/trending/movie/week");
    }

    public List<Movie> getByGenre(int genreId) {
        return fetchList("/discover/movie", "with_genres=" + genreId);
    }

    public List<Movie> search(String query) {
        try {
            return fetchList("/search/movie", "query=" + URLEncoder.encode(query, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encoding error in search query: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public Movie getMovieDetail(long movieId) {
        try {
            String url = config.getBaseUrl() + "/movie/" + movieId + "?api_key=" + config.getApiKey();
            JSONObject json = fetchJson(url);
            return json != null ? TMDbMovieAdapter.adapt(json) : null;
        } catch (Exception e) {
            System.err.println("Failed to fetch movie detail for ID " + movieId + ": " + e.getMessage());
            return null;
        }
    }

    /** Single call that gathers everything the home page needs at once. */
    public HomePageData getHomePageData() {
        List<Movie> trending  = getTrending();
        List<Movie> action    = getByGenre(28);
        List<Movie> comedy    = getByGenre(35);
        List<Movie> thriller  = getByGenre(53);
        List<Movie> scifi     = getByGenre(878);
        return new HomePageData(trending, action, comedy, thriller, scifi);
    }

    // ── Inner data class ───────────────────────────────────────────────────

    public record HomePageData(
            List<Movie> trending,
            List<Movie> action,
            List<Movie> comedy,
            List<Movie> thriller,
            List<Movie> scifi
    ) {}

    // ── Private helpers ────────────────────────────────────────────────────

    private List<Movie> fetchList(String endpoint) {
        return fetchList(endpoint, null);
    }

    private List<Movie> fetchList(String endpoint, String extraParams) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(config.getBaseUrl())
                .append(endpoint)
                .append("?api_key=")
                .append(config.getApiKey());

        if (extraParams != null && !extraParams.trim().isEmpty()) {
            urlBuilder.append("&").append(extraParams);
        }

        try {
            JSONObject response = fetchJson(urlBuilder.toString());
            if (response == null) {
                return Collections.emptyList();
            }

            JSONArray results = response.optJSONArray("results");
            List<Movie> movies = new ArrayList<>();
            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    movies.add(TMDbMovieAdapter.adapt(results.getJSONObject(i)));
                }
            }
            return movies;
        } catch (Exception e) {
            System.err.println("API Request failed for endpoint [" + endpoint + "]: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // package-visible so tests can spy on it
    protected JSONObject fetchJson(String urlStr) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000); // 3 seconds timeout to connect
            conn.setReadTimeout(3000);    // 3 seconds timeout to read data

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.err.println("HTTP Error " + responseCode + " when connecting to TMDb.");
                return null;
            }

            try (BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    sb.append(line);
                }
                return new JSONObject(sb.toString());
            }
        } catch (SocketTimeoutException e) {
            System.err.println("TMDb API Connection Timed Out. Check your internet connection or proxy/VPN.");
            return null;
        } catch (Exception e) {
            System.err.println("Error fetching data from TMDb API: " + e.getMessage());
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}