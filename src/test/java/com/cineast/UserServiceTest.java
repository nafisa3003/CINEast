package com.cineast;

import com.cineast.model.User;
import com.cineast.service.UserRepository;
import com.cineast.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests for UserService — registration, BCrypt password logic, profile update.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository repo;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(repo, encoder);
    }

    // ── Registration ───────────────────────────────────────────────────────

    @Test
    void register_success_returnsOk() {
        when(repo.existsByUsername("alice")).thenReturn(false);
        when(repo.existsByEmail("alice@test.com")).thenReturn(false);
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        assertEquals(UserService.RegisterResult.OK,
                service.register("alice", "alice@test.com", "password123"));
    }

    @Test
    void register_duplicateUsername_returnsUsernameTaken() {
        when(repo.existsByUsername("alice")).thenReturn(true);
        assertEquals(UserService.RegisterResult.USERNAME_TAKEN,
                service.register("alice", "alice@test.com", "password123"));
    }

    @Test
    void register_duplicateEmail_returnsEmailTaken() {
        when(repo.existsByUsername("bob")).thenReturn(false);
        when(repo.existsByEmail("taken@test.com")).thenReturn(true);
        assertEquals(UserService.RegisterResult.EMAIL_TAKEN,
                service.register("bob", "taken@test.com", "password123"));
    }

    @Test
    void register_passwordIsHashed() {
        when(repo.existsByUsername(any())).thenReturn(false);
        when(repo.existsByEmail(any())).thenReturn(false);
        User saved[] = new User[1];
        when(repo.save(any())).thenAnswer(i -> { saved[0] = i.getArgument(0); return saved[0]; });

        service.register("carol", "carol@test.com", "plaintext");

        assertNotNull(saved[0]);
        assertNotEquals("plaintext", saved[0].getPassword());
        assertTrue(encoder.matches("plaintext", saved[0].getPassword()));
    }

    // ── BCrypt ─────────────────────────────────────────────────────────────

    @Test
    void bcrypt_matchesCorrectPassword() {
        String hash = encoder.encode("mypassword");
        assertTrue(encoder.matches("mypassword", hash));
    }

    @Test
    void bcrypt_rejectsWrongPassword() {
        String hash = encoder.encode("correct");
        assertFalse(encoder.matches("wrong", hash));
    }

    @Test
    void bcrypt_twoHashesSameInput_areDifferent() {
        String h1 = encoder.encode("same");
        String h2 = encoder.encode("same");
        assertNotEquals(h1, h2, "BCrypt uses random salts so hashes are never identical");
        assertTrue(encoder.matches("same", h1));
        assertTrue(encoder.matches("same", h2));
    }

    // ── Change password ────────────────────────────────────────────────────

    @Test
    void changePassword_mismatch_returnsMismatch() {
        assertEquals(UserService.PasswordResult.MISMATCH,
                service.changePassword("dave", "old", "newpw1", "newpw2"));
    }

    @Test
    void changePassword_tooShort_returnsTooShort() {
        assertEquals(UserService.PasswordResult.TOO_SHORT,
                service.changePassword("dave", "old", "abc", "abc"));
    }

    @Test
    void changePassword_wrongCurrent_returnsWrongCurrent() {
        User u = new User(); u.setUsername("dave"); u.setPassword(encoder.encode("correct"));
        when(repo.findByUsername("dave")).thenReturn(Optional.of(u));
        assertEquals(UserService.PasswordResult.WRONG_CURRENT,
                service.changePassword("dave", "wrong", "newpass1", "newpass1"));
    }

    @Test
    void changePassword_success_returnsOk() {
        User u = new User(); u.setUsername("dave"); u.setPassword(encoder.encode("oldpass"));
        when(repo.findByUsername("dave")).thenReturn(Optional.of(u));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        assertEquals(UserService.PasswordResult.OK,
                service.changePassword("dave", "oldpass", "newpass1", "newpass1"));
    }
}