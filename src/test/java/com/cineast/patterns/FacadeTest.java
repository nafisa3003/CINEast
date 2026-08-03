package com.cineast.patterns;

import com.cineast.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * FACADE PATTERN tests
 * Uses Mockito @Spy to intercept the HTTP layer so no real network is needed.
 * Verifies that the Facade correctly orchestrates API calls and returns typed Movie objects.
 */
@ExtendWith(MockitoExtension.class)
class FacadeTest {

    @Spy
    private MovieExplorationFacade facade = new MovieExplorationFacade(
            "test_key", "https://api.themoviedb.org/3");

    private JSONObject singleMovie() {
        return new JSONObject()
                .put("id", 101)
                .put("title", "Interstellar")
                .put("overview", "A team travels through a wormhole.")
                .put("poster_path", "/test.jpg")
                .put("backdrop_path", "/bg.jpg")
                .put("release_date", "2014-11-05")
                .put("vote_average", 8.6)
                .put("popularity", 82.0)
                .put("runtime", 169);
    }

    private JSONObject listResponse() {
        return new JSONObject().put("results", new JSONArray().put(singleMovie()));
    }

    @BeforeEach
    void stubHttp() throws IOException {
        lenient().doReturn(listResponse()).when(facade).fetchJson(anyString());
    }

    @Test
    void getTrending_returnsNonEmptyList() throws IOException {
        List<Movie> movies = facade.getTrending();
        assertFalse(movies.isEmpty());
    }

    @Test
    void getTrending_firstMovieHasCorrectTitle() throws IOException {
        assertEquals("Interstellar", facade.getTrending().get(0).getTitle());
    }

    @Test
    void search_returnsMatchingResults() throws IOException {
        List<Movie> results = facade.search("Interstellar");
        assertEquals(1, results.size());
        assertEquals("Interstellar", results.get(0).getTitle());
    }

    @Test
    void getMovieDetail_returnsCorrectMovie() throws IOException {
        doReturn(singleMovie()).when(facade).fetchJson(anyString());
        Movie m = facade.getMovieDetail(101);
        assertEquals(101,          m.getId());
        assertEquals("Interstellar", m.getTitle());
        assertEquals(8.6,          m.getRating(), 0.001);
    }

    @Test
    void getByGenre_callsApi() throws IOException {
        facade.getByGenre(28);
        verify(facade, atLeastOnce()).fetchJson(anyString());
    }

    @Test
    void getHomePageData_returnsAllSections() throws IOException {
        MovieExplorationFacade.HomePageData data = facade.getHomePageData();
        assertNotNull(data.trending());
        assertNotNull(data.action());
        assertNotNull(data.comedy());
        assertNotNull(data.thriller());
        assertNotNull(data.scifi());
    }

    @Test
    void getHomePageData_trendingNotEmpty() throws IOException {
        assertFalse(facade.getHomePageData().trending().isEmpty());
    }
}