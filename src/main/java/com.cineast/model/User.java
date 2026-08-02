package com.cineast.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;   // BCrypt hashed — Spring Security handles this

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    // ── Getters & Setters ──────────────────────────────────────────────────

    public Long getId()                    { return id; }
    public String getUsername()            { return username; }
    public void setUsername(String v)      { this.username = v; }
    public String getEmail()               { return email; }
    public void setEmail(String v)         { this.email = v; }
    public String getPassword()            { return password; }
    public void setPassword(String v)      { this.password = v; }
    public String getBio()                 { return bio != null ? bio : ""; }
    public void setBio(String v)           { this.bio = v; }
    public String getAvatarUrl()           { return avatarUrl; }
    public void setAvatarUrl(String v)     { this.avatarUrl = v; }
    public LocalDateTime getCreatedAt()    { return createdAt; }
    public List<Review> getReviews()       { return reviews; }

    public String getInitial() {
        return (username != null && !username.isEmpty())
            ? String.valueOf(username.charAt(0)).toUpperCase() : "?";
    }
    public boolean hasAvatar() {
        return avatarUrl != null && !avatarUrl.trim().isEmpty();
    }
    public String getJoinYear() {
        return createdAt != null ? String.valueOf(createdAt.getYear()) : "2025";
    }
}
