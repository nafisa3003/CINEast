package com.cineast.service;

import com.cineast.model.User;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository    repo;
    private final PasswordEncoder   encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo    = repo;
        this.encoder = encoder;
    }

    // ── Spring Security: load user for authentication ──────────────────────

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), user.getPassword(), Collections.emptyList()
        );
    }

    // ── Registration ───────────────────────────────────────────────────────

    public enum RegisterResult { OK, USERNAME_TAKEN, EMAIL_TAKEN }

    public RegisterResult register(String username, String email, String password) {
        if (repo.existsByUsername(username)) return RegisterResult.USERNAME_TAKEN;
        if (repo.existsByEmail(email))       return RegisterResult.EMAIL_TAKEN;
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(encoder.encode(password));   // BCrypt hash
        repo.save(u);
        return RegisterResult.OK;
    }

    // ── Profile ────────────────────────────────────────────────────────────

    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public void updateProfile(String username, String bio, String avatarUrl) {
        repo.findByUsername(username).ifPresent(u -> {
            u.setBio(bio);
            u.setAvatarUrl(avatarUrl != null && !avatarUrl.trim().isEmpty() ? avatarUrl.trim() : null);
            repo.save(u);
        });
    }

    public enum PasswordResult { OK, WRONG_CURRENT, MISMATCH, TOO_SHORT }

    public PasswordResult changePassword(String username,
                                         String current, String newPw, String confirm) {
        if (!newPw.equals(confirm))    return PasswordResult.MISMATCH;
        if (newPw.length() < 6)        return PasswordResult.TOO_SHORT;
        User u = repo.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!encoder.matches(current, u.getPassword())) return PasswordResult.WRONG_CURRENT;
        u.setPassword(encoder.encode(newPw));
        repo.save(u);
        return PasswordResult.OK;
    }
}
