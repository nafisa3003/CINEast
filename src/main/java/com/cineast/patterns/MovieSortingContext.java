package com.cineast.patterns;

import com.cineast.model.Movie;
import java.util.Comparator;
import java.util.List;

/**
 * STRATEGY PATTERN
 * MovieSortingContext holds a MovieSortStrategy and delegates sorting to it.
 * The controller picks a concrete strategy at runtime based on the URL param,
 * so the sort algorithm can be swapped without changing any other code.
 */
public class MovieSortingContext {

    // ── Strategy interface ─────────────────────────────────────────────────

    public interface MovieSortStrategy {
        void sort(List<Movie> movies);
        String getName();
    }

    // ── Concrete strategies ────────────────────────────────────────────────

    public static class TrendingStrategy implements MovieSortStrategy {
        @Override public void sort(List<Movie> movies) {
            movies.sort(Comparator.comparingDouble(Movie::getPopularity).reversed());
        }
        @Override public String getName() { return "trending"; }
    }

    public static class RatingStrategy implements MovieSortStrategy {
        @Override public void sort(List<Movie> movies) {
            movies.sort(Comparator.comparingDouble(Movie::getRating).reversed());
        }
        @Override public String getName() { return "rating"; }
    }

    public static class ReleaseDateStrategy implements MovieSortStrategy {
        @Override public void sort(List<Movie> movies) {
            movies.sort(Comparator.comparing(
                m -> m.getReleaseDate() != null ? m.getReleaseDate() : "",
                Comparator.reverseOrder()
            ));
        }
        @Override public String getName() { return "release_date"; }
    }

    public static class TitleStrategy implements MovieSortStrategy {
        @Override public void sort(List<Movie> movies) {
            movies.sort(Comparator.comparing(
                m -> m.getTitle() != null ? m.getTitle().toLowerCase() : ""
            ));
        }
        @Override public String getName() { return "title"; }
    }

    // ── Context ────────────────────────────────────────────────────────────

    private MovieSortStrategy strategy;

    public MovieSortingContext(MovieSortStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(MovieSortStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeSort(List<Movie> movies) {
        strategy.sort(movies);
    }

    public String getStrategyName() {
        return strategy.getName();
    }

    /** Factory: build a context from a request param string. */
    public static MovieSortingContext fromParam(String param) {
        if (param == null) return new MovieSortingContext(new TrendingStrategy());
        return switch (param) {
            case "rating"       -> new MovieSortingContext(new RatingStrategy());
            case "release_date" -> new MovieSortingContext(new ReleaseDateStrategy());
            case "title"        -> new MovieSortingContext(new TitleStrategy());
            default             -> new MovieSortingContext(new TrendingStrategy());
        };
    }
}
