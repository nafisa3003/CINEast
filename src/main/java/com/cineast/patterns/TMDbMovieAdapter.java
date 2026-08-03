package com.cineast.patterns;

import com.cineast.model.Movie;
import org.json.JSONObject;

/**
 * ADAPTER PATTERN
 * The TMDb API returns raw JSONObject payloads that our app doesn't want
 * to depend on directly. TMDbMovieAdapter translates that external format
 * into our own internal Movie model — decoupling the API from the rest of
 * the codebase.
 *
 * If the TMDb API ever changes its field names, we only update this class.
 */
public class TMDbMovieAdapter {

    /**
     * Adapt a raw TMDb API JSONObject → our internal Movie model.
     */
    public static Movie adapt(JSONObject json) {
        return new Movie(
            json.optLong("id", 0),
            json.optString("title", "Unknown"),
            json.optString("overview", ""),
            json.optString("poster_path", ""),
            json.optString("backdrop_path", ""),
            json.optString("release_date", ""),
            json.optDouble("vote_average", 0.0),
            json.optDouble("popularity", 0.0),
            json.optInt("runtime", 0)
        );
    }

    private TMDbMovieAdapter() {}   // utility class — not instantiable
}
