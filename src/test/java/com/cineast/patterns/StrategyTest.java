package com.cineast.patterns;

import com.cineast.model.Movie;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * STRATEGY PATTERN tests
 * Verifies each concrete sort strategy produces the correct ordering.
 */
class StrategyTest {

    private List<Movie> sampleMovies() {
        List<Movie> list = new ArrayList<>();
        list.add(new Movie(1, "Zodiac",   "desc", "", "", "2007-03-02", 7.6, 45.5, 157));
        list.add(new Movie(2, "Alien",    "desc", "", "", "1979-05-25", 8.4, 90.2, 117));
        list.add(new Movie(3, "Midsommar","desc", "", "", "2019-07-03", 7.1, 60.1, 148));
        return list;
    }

    @Test
    void trendingStrategy_sortsByPopularityDescending() {
        List<Movie> movies = sampleMovies();
        MovieSortingContext ctx = new MovieSortingContext(new MovieSortingContext.TrendingStrategy());
        ctx.executeSort(movies);
        assertEquals("Alien", movies.get(0).getTitle(), "Highest popularity must be first");
        assertTrue(movies.get(0).getPopularity() >= movies.get(1).getPopularity());
    }

    @Test
    void ratingStrategy_sortsByRatingDescending() {
        List<Movie> movies = sampleMovies();
        MovieSortingContext ctx = new MovieSortingContext(new MovieSortingContext.RatingStrategy());
        ctx.executeSort(movies);
        assertEquals("Alien", movies.get(0).getTitle(), "Highest rated must be first");
        assertTrue(movies.get(0).getRating() >= movies.get(1).getRating());
    }

    @Test
    void releaseDateStrategy_sortsByNewestFirst() {
        List<Movie> movies = sampleMovies();
        MovieSortingContext ctx = new MovieSortingContext(new MovieSortingContext.ReleaseDateStrategy());
        ctx.executeSort(movies);
        assertEquals("Midsommar", movies.get(0).getTitle(), "Newest release must be first");
        assertEquals("Alien",     movies.get(2).getTitle(), "Oldest release must be last");
    }

    @Test
    void titleStrategy_sortsAlphabetically() {
        List<Movie> movies = sampleMovies();
        MovieSortingContext ctx = new MovieSortingContext(new MovieSortingContext.TitleStrategy());
        ctx.executeSort(movies);
        assertEquals("Alien",    movies.get(0).getTitle());
        assertEquals("Midsommar",movies.get(1).getTitle());
        assertEquals("Zodiac",   movies.get(2).getTitle());
    }

    @Test
    void fromParam_nullDefaultsToTrending() {
        MovieSortingContext ctx = MovieSortingContext.fromParam(null);
        assertEquals("trending", ctx.getStrategyName());
    }

    @Test
    void fromParam_unknownDefaultsToTrending() {
        MovieSortingContext ctx = MovieSortingContext.fromParam("banana");
        assertEquals("trending", ctx.getStrategyName());
    }

    @Test
    void fromParam_ratingResolvesCorrectly() {
        assertEquals("rating", MovieSortingContext.fromParam("rating").getStrategyName());
    }

    @Test
    void fromParam_releaseDateResolvesCorrectly() {
        assertEquals("release_date", MovieSortingContext.fromParam("release_date").getStrategyName());
    }

    @Test
    void fromParam_titleResolvesCorrectly() {
        assertEquals("title", MovieSortingContext.fromParam("title").getStrategyName());
    }

    @Test
    void strategyCanBeSwappedAtRuntime() {
        List<Movie> movies = sampleMovies();
        // 1) Sort by Trending -> Alien is first (Popularity 90.2)
        MovieSortingContext ctx = new MovieSortingContext(new MovieSortingContext.TrendingStrategy());
        ctx.executeSort(movies);
        String firstByTrending = movies.get(0).getTitle(); // "Alien"

        // 2) Swap strategy to Release Date -> Midsommar is first (Released 2019)
        ctx.setStrategy(new MovieSortingContext.ReleaseDateStrategy());
        ctx.executeSort(movies);
        String firstByReleaseDate = movies.get(0).getTitle(); // "Midsommar"

        assertNotEquals(firstByTrending, firstByReleaseDate, "Swapping strategy must change sort order");
        assertEquals("Midsommar", firstByReleaseDate);
    }
}
