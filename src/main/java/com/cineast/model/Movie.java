package com.cineast.model;

/**
 * Plain movie object used throughout the app.
 * Populated by TMDbMovieAdapter from raw API JSON.
 */
public class Movie {

    private final long   id;
    private final String title;
    private final String overview;
    private final String posterPath;
    private final String backdropPath;
    private final String releaseDate;
    private final double rating;
    private final double popularity;
    private final int    runtime;

    public Movie(long id, String title, String overview, String posterPath,
                 String backdropPath, String releaseDate,
                 double rating, double popularity, int runtime) {
        this.id           = id;
        this.title        = title;
        this.overview     = overview;
        this.posterPath   = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate  = releaseDate;
        this.rating       = rating;
        this.popularity   = popularity;
        this.runtime      = runtime;
    }

    public long   getId()           { return id; }
    public String getTitle()        { return title; }
    public String getOverview()     { return overview; }
    public String getPosterPath()   { return posterPath; }
    public String getBackdropPath() { return backdropPath; }
    public String getReleaseDate()  { return releaseDate; }
    public double getRating()       { return rating; }
    public double getPopularity()   { return popularity; }
    public int    getRuntime()      { return runtime; }

    public String getPosterUrl() {
        return (posterPath != null && !posterPath.isEmpty())
            ? "https://image.tmdb.org/t/p/w500" + posterPath : "";
    }
    public String getBackdropUrl() {
        return (backdropPath != null && !backdropPath.isEmpty())
            ? "https://image.tmdb.org/t/p/original" + backdropPath : "";
    }
    public String getReleaseYear() {
        return (releaseDate != null && releaseDate.length() >= 4)
            ? releaseDate.substring(0, 4) : "N/A";
    }
    public String getRatingFormatted() {
        return String.format("%.1f", rating);
    }
    public boolean hasPoster() {
        return posterPath != null && !posterPath.isEmpty();
    }
    public boolean hasBackdrop() {
        return backdropPath != null && !backdropPath.isEmpty();
    }
}
