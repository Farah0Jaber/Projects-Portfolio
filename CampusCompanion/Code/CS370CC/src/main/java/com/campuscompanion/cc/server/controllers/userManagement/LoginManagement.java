package com.campuscompanion.cc.server.controllers.userManagement;

import com.campuscompanion.cc.client.utility.Credentials;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginManagement {
    private final UserManagement userManagement;

    @Autowired
    public LoginManagement(UserManagement userManagement) {
        this.userManagement = userManagement;
    }

    // Handle login form submission
    @PostMapping("/login")
    public String handleLogin(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session) {
        Credentials credentials = new Credentials(email, password);
        boolean isAuthenticated = userManagement.authenticateUser(credentials);

        System.out.println("Authentication result for " + email + ": " + isAuthenticated);

        if (isAuthenticated) {
            session.setAttribute("email", email); // Store user email in the session
            return "redirect:/home"; // Redirect to the home page after successful login
        } else {
            return "redirect:/login?error=Invalid credentials"; // Redirect back to login with error
        }
    }

    // Handle logout
    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate(); // Clear the session
        return "redirect:/login?message=Logged out successfully"; // Redirect to login page
    }
}
