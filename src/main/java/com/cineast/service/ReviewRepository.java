package com.cineast.service;

import com.cineast.model.Review;
import com.cineast.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMovieIdOrderByCreatedAtDesc(Long movieId);
    List<Review> findByUserOrderByCreatedAtDesc(User user);
    int countByUser(User user);
}
