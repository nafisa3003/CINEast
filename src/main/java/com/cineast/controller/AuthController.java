package com.cineast.controller;

import com.cineast.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // ── Login ──────────────────────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout,
            @RequestParam(required = false) String registered,
            Model model) {
        if (error     != null) model.addAttribute("error",      "Invalid username or password.");
        if (logout    != null) model.addAttribute("message",    "You've been signed out.");
        if (registered != null) model.addAttribute("success",   "Account created! Please sign in.");
        return "login";
    }

    // ── Register ───────────────────────────────────────────────────────────

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes ra) {

        if (password.length() < 6) {
            ra.addFlashAttribute("error", "Password must be at least 6 characters.");
            return "redirect:/register";
        }

        UserService.RegisterResult result = userService.register(username, email, password);
        return switch (result) {
            case OK             -> "redirect:/login?registered=true";
            case USERNAME_TAKEN -> { ra.addFlashAttribute("error", "Username already taken."); yield "redirect:/register"; }
            case EMAIL_TAKEN    -> { ra.addFlashAttribute("error", "Email already registered."); yield "redirect:/register"; }
        };
    }
}
