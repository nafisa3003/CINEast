package com.cineast.service;

import com.cineast.model.Review;
import com.cineast.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository repo;
    private final UserRepository   users;

    public ReviewService(ReviewRepository repo, UserRepository users) {
        this.repo  = repo;
        this.users = users;
    }

    public List<Review> getReviewsForMovie(Long movieId) {
        return repo.findByMovieIdOrderByCreatedAtDesc(movieId);
    }

    public List<Review> getReviewsByUser(String username) {
        User u = users.findByUsername(username).orElse(null);
        if (u == null) return List.of();
        return repo.findByUserOrderByCreatedAtDesc(u);
    }

    public int getReviewCountByUser(String username) {
        User u = users.findByUsername(username).orElse(null);
        if (u == null) return 0;
        return repo.countByUser(u);
    }

    public void addReview(Long movieId, String username, String content) {
        User u = users.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Review r = new Review();
        r.setMovieId(movieId);
        r.setUser(u);
        r.setContent(content.trim());
        repo.save(r);
    }
}
