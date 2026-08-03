package com.cineast.controller;

import com.cineast.model.User;
import com.cineast.service.ReviewService;
import com.cineast.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService   userService;
    private final ReviewService reviewService;

    public ProfileController(UserService userService, ReviewService reviewService) {
        this.userService   = userService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String profilePage(@AuthenticationPrincipal UserDetails auth, Model model) {
        User user = userService.findByUsername(auth.getUsername()).orElse(null);
        model.addAttribute("profile",   user);
        model.addAttribute("myReviews", reviewService.getReviewsByUser(auth.getUsername()));
        model.addAttribute("reviewCount", reviewService.getReviewCountByUser(auth.getUsername()));
        return "profile";
    }

    @GetMapping("/settings")
    public String settingsPage(@AuthenticationPrincipal UserDetails auth, Model model) {
        User user = userService.findByUsername(auth.getUsername()).orElse(null);
        model.addAttribute("profile", user);
        return "settings";
    }

    @PostMapping("/update")
    public String updateProfile(
            @AuthenticationPrincipal UserDetails auth,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String avatarUrl,
            RedirectAttributes ra) {
        userService.updateProfile(auth.getUsername(), bio, avatarUrl);
        ra.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/profile/settings";
    }

    @PostMapping("/password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails auth,
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes ra) {

        UserService.PasswordResult result = userService.changePassword(
            auth.getUsername(), currentPassword, newPassword, confirmPassword);

        String msg = switch (result) {
            case OK            -> null;
            case WRONG_CURRENT -> "Current password is incorrect.";
            case MISMATCH      -> "New passwords do not match.";
            case TOO_SHORT     -> "Password must be at least 6 characters.";
        };

        if (msg != null) ra.addFlashAttribute("error", msg);
        else             ra.addFlashAttribute("success", "Password changed successfully.");
        return "redirect:/profile/settings";
    }
}
