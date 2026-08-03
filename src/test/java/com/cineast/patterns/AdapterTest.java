package com.cineast.patterns;

import com.cineast.model.Movie;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ADAPTER PATTERN tests
 * Feeds mock raw JSON payloads into TMDbMovieAdapter and verifies
 * that every field maps correctly into the Movie model.
 */
class AdapterTest {

    private JSONObject fullJson() {
        return new JSONObject()
            .put("id", 550)
            .put("title", "Fight Club")
            .put("overview", "An insomniac office worker forms an underground fight club.")
            .put("poster_path", "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg")
            .put("backdrop_path", "/fCayJrkfRaCRCTh8GqN30f8oyQF.jpg")
            .put("release_date", "1999-10-15")
            .put("vote_average", 8.4)
            .put("popularity", 78.3)
            .put("runtime", 139);
    }

    @Test
    void adapt_mapsIdCorrectly() {
        assertEquals(550, TMDbMovieAdapter.adapt(fullJson()).getId());
    }

    @Test
    void adapt_mapsTitleCorrectly() {
        assertEquals("Fight Club", TMDbMovieAdapter.adapt(fullJson()).getTitle());
    }

    @Test
    void adapt_mapsOverviewCorrectly() {
        assertTrue(TMDbMovieAdapter.adapt(fullJson()).getOverview().contains("insomniac"));
    }

    @Test
    void adapt_mapsRatingCorrectly() {
        assertEquals(8.4, TMDbMovieAdapter.adapt(fullJson()).getRating(), 0.001);
    }

    @Test
    void adapt_mapsPopularityCorrectly() {
        assertEquals(78.3, TMDbMovieAdapter.adapt(fullJson()).getPopularity(), 0.001);
    }

    @Test
    void adapt_mapsRuntimeCorrectly() {
        assertEquals(139, TMDbMovieAdapter.adapt(fullJson()).getRuntime());
    }

    @Test
    void adapt_mapsReleaseDateCorrectly() {
        assertEquals("1999-10-15", TMDbMovieAdapter.adapt(fullJson()).getReleaseDate());
    }

    @Test
    void adapt_getReleaseYear_extractsCorrectYear() {
        assertEquals("1999", TMDbMovieAdapter.adapt(fullJson()).getReleaseYear());
    }

    @Test
    void adapt_getRatingFormatted_returnsOneDecimal() {
        assertEquals("8.4", TMDbMovieAdapter.adapt(fullJson()).getRatingFormatted());
    }

    @Test
    void adapt_getPosterUrl_buildsCdnUrl() {
        Movie m = TMDbMovieAdapter.adapt(fullJson());
        assertTrue(m.getPosterUrl().startsWith("https://image.tmdb.org/t/p/w500"));
        assertTrue(m.getPosterUrl().contains("/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg"));
    }

    @Test
    void adapt_getBackdropUrl_buildsOriginalUrl() {
        Movie m = TMDbMovieAdapter.adapt(fullJson());
        assertTrue(m.getBackdropUrl().startsWith("https://image.tmdb.org/t/p/original"));
    }

    @Test
    void adapt_emptyJson_usesDefaults() {
        Movie m = TMDbMovieAdapter.adapt(new JSONObject());
        assertEquals(0,         m.getId());
        assertEquals("Unknown", m.getTitle());
        assertEquals(0.0,       m.getRating(), 0.001);
        assertEquals("N/A",     m.getReleaseYear());
        assertFalse(m.hasPoster());
        assertFalse(m.hasBackdrop());
    }

    @Test
    void adapt_emptyReleaseDate_returnsNA() {
        Movie m = TMDbMovieAdapter.adapt(new JSONObject().put("release_date", ""));
        assertEquals("N/A", m.getReleaseYear());
    }

    @Test
    void adapt_hasPoster_trueWhenPathPresent() {
        assertTrue(TMDbMovieAdapter.adapt(fullJson()).hasPoster());
    }

    @Test
    void adapt_hasPoster_falseWhenPathEmpty() {
        Movie m = TMDbMovieAdapter.adapt(new JSONObject().put("poster_path", ""));
        assertFalse(m.hasPoster());
    }
}
