package com.campuscompanion.cc.server.controllers.userManagement;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/signup")
public class SignupManagement {

    private final UserManagement userManagement;

    @Autowired
    public SignupManagement(UserManagement userManagement) {
        this.userManagement = userManagement;
    }

    @PostMapping
    public String handleSignup(
            @RequestParam("email") String email,
            @RequestParam("full-name") String fullName,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam(value = "club", required = false) String club, // Optional for "event_manager"
            Model model) {

        String message;

        // Validate Email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            message = "Invalid email format.";
            return "redirect:/signup?error=" + message;
        }

        // Validate Password
        if (password.length() < 8) {
            message = "Password must be at least 8 characters long.";
            return "redirect:/signup?error=" + message;
        }

        // Validate Role
        if (!role.equals("student") && !role.equals("maintenance")) {
            message = "Invalid role. Must be 'student' or 'maintenance'.";
            return "redirect:/signup?error=" + message;
        }

        // Check if email is already registered
        if (userManagement.isEmailRegistered(email)) {
            message = "Email is already registered.";
            return "redirect:/signup?error=" + message;
        }

        // Save User to Database
        try {
            userManagement.registerUser(email, fullName, password, role);
            message = "User registered successfully.";
            return "redirect:/signup?success=" + message;
        } catch (Exception e) {
            e.printStackTrace();
            message = "Error occurred while saving user.";
            return "redirect:/signup?error=" + message;
        }
    }
}
