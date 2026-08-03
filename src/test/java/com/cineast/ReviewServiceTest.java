package com.cineast;

import com.cineast.model.Review;
import com.cineast.model.User;
import com.cineast.service.ReviewRepository;
import com.cineast.service.ReviewService;
import com.cineast.service.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests for ReviewService — fetching and adding reviews using mocked repositories.
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock private ReviewRepository reviewRepo;
    @Mock private UserRepository   userRepo;
    private ReviewService service;

    private User testUser() {
        User u = new User();
        u.setUsername("alice");
        return u;
    }

    @BeforeEach
    void setUp() {
        service = new ReviewService(reviewRepo, userRepo);
    }

    @Test
    void getReviewsForMovie_returnsCorrectList() {
        Review r1 = new Review(); r1.setContent("Loved it!"); r1.setMovieId(42L);
        Review r2 = new Review(); r2.setContent("Great film."); r2.setMovieId(42L);
        when(reviewRepo.findByMovieIdOrderByCreatedAtDesc(42L)).thenReturn(List.of(r1, r2));

        List<Review> result = service.getReviewsForMovie(42L);

        assertEquals(2, result.size());
        assertEquals("Loved it!", result.get(0).getContent());
    }

    @Test
    void getReviewsForMovie_emptyMovieId_returnsEmptyList() {
        when(reviewRepo.findByMovieIdOrderByCreatedAtDesc(999L)).thenReturn(List.of());
        assertTrue(service.getReviewsForMovie(999L).isEmpty());
    }

    @Test
    void getReviewsByUser_userExists_returnsReviews() {
        User u = testUser();
        Review r = new Review(); r.setContent("Brilliant."); r.setUser(u);
        when(userRepo.findByUsername("alice")).thenReturn(Optional.of(u));
        when(reviewRepo.findByUserOrderByCreatedAtDesc(u)).thenReturn(List.of(r));

        List<Review> result = service.getReviewsByUser("alice");

        assertEquals(1, result.size());
        assertEquals("Brilliant.", result.get(0).getContent());
    }

    @Test
    void getReviewsByUser_userNotFound_returnsEmptyList() {
        when(userRepo.findByUsername("ghost")).thenReturn(Optional.empty());
        assertTrue(service.getReviewsByUser("ghost").isEmpty());
    }

    @Test
    void getReviewCountByUser_returnsCorrectCount() {
        User u = testUser();
        when(userRepo.findByUsername("alice")).thenReturn(Optional.of(u));
        when(reviewRepo.countByUser(u)).thenReturn(5);
        assertEquals(5, service.getReviewCountByUser("alice"));
    }

    @Test
    void addReview_savesCorrectly() {
        User u = testUser();
        when(userRepo.findByUsername("alice")).thenReturn(Optional.of(u));
        when(reviewRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        service.addReview(42L, "alice", "  Amazing cinematography.  ");

        verify(reviewRepo).save(argThat(r ->
            r.getContent().equals("Amazing cinematography.") &&
            r.getMovieId().equals(42L) &&
            r.getUser() == u
        ));
    }

    @Test
    void addReview_trimsWhitespace() {
        User u = testUser();
        when(userRepo.findByUsername("alice")).thenReturn(Optional.of(u));
        when(reviewRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        service.addReview(1L, "alice", "   spaced out   ");
        verify(reviewRepo).save(argThat(r -> r.getContent().equals("spaced out")));
    }

    @Test
    void addReview_unknownUser_throwsException() {
        when(userRepo.findByUsername("nobody")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class,
            () -> service.addReview(1L, "nobody", "test"));
    }

    @Test
    void review_getFormattedDate_nonNullForNewReview() {
        Review r = new Review();
        assertNotNull(r.getFormattedDate());
    }
}
