package com.cineast.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ── Getters & Setters ──────────────────────────────────────────────────

    public Long getId()                  { return id; }
    public Long getMovieId()             { return movieId; }
    public void setMovieId(Long v)       { this.movieId = v; }
    public User getUser()                { return user; }
    public void setUser(User v)          { this.user = v; }
    public String getContent()           { return content; }
    public void setContent(String v)     { this.content = v; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    public String getFormattedDate() {
        if (createdAt == null) return "";
        return createdAt.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
    }
}
