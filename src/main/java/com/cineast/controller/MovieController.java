package com.cineast.controller;

import com.cineast.model.Movie;
import com.cineast.patterns.MovieExplorationFacade;
import com.cineast.patterns.MovieSortingContext;
import com.cineast.service.ReviewService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
public class MovieController {

    private final MovieExplorationFacade facade;
    private final ReviewService          reviewService;

    public MovieController(MovieExplorationFacade facade, ReviewService reviewService) {
        this.facade        = facade;
        this.reviewService = reviewService;
    }

    // ── Home ───────────────────────────────────────────────────────────────

    @GetMapping("/")
    public String home(Model model) throws IOException {
        MovieExplorationFacade.HomePageData data = facade.getHomePageData();
        model.addAttribute("trendingMovies",  data.trending());
        model.addAttribute("actionMovies",    data.action());
        model.addAttribute("comedyMovies",    data.comedy());
        model.addAttribute("thrillerMovies",  data.thriller());
        model.addAttribute("scifiMovies",     data.scifi());
        return "home";
    }

    // ── Movie detail ───────────────────────────────────────────────────────

    @GetMapping("/movie/{id}")
    public String movieDetail(@PathVariable long id, Model model) throws IOException {
        Movie movie = facade.getMovieDetail(id);
        model.addAttribute("movie",   movie);
        model.addAttribute("reviews", reviewService.getReviewsForMovie(id));
        return "movieDetail";
    }

    // ── Search ─────────────────────────────────────────────────────────────

    @GetMapping("/search")
    public String search(
            @RequestParam String q,
            @RequestParam(required = false) String sort,
            Model model) throws IOException {

        List<Movie> results = facade.search(q);

        // STRATEGY PATTERN: pick sort algorithm from URL param
        MovieSortingContext ctx = MovieSortingContext.fromParam(sort);
        ctx.executeSort(results);

        model.addAttribute("results",      results);
        model.addAttribute("query",        q);
        model.addAttribute("currentSort",  ctx.getStrategyName());
        return "searchResults";
    }

    // ── Post review ────────────────────────────────────────────────────────

    @PostMapping("/movie/{id}/review")
    public String postReview(
            @PathVariable long id,
            @RequestParam String content,
            @AuthenticationPrincipal UserDetails user) {
        if (content != null && !content.trim().isEmpty()) {
            reviewService.addReview(id, user.getUsername(), content);
        }
        return "redirect:/movie/" + id;
    }
}
