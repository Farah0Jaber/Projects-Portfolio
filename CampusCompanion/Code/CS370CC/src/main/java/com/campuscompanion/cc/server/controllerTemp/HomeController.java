package com.campuscompanion.cc.server.controllerTemp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    // Mapping for the root URL ("/") - Redirects to the home page
    @GetMapping("/")
    public String index() {
        return "redirect:/home"; // Redirect to the main home endpoint
    }

    // Mapping for Login page
    @GetMapping("/login")
    public String login() {
        return "login"; // This will return login.html
    }

    // Mapping for Signup page
    @GetMapping("/signup")
    public String signup() {
        return "signup"; // This will return signup.html
    }

    // Mapping for Home page
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        // Retrieve the email from the session
        String email = (String) session.getAttribute("email");

        // Debugging: Check if the email is being retrieved correctly
        System.out.println("Email from session: " + email);

        // If email is null, redirect to login (user is not logged in)
        if (email == null) {
            return "redirect:/login?error=Please log in first";
        }

        // Add the email to the model for display
        model.addAttribute("userEmail", email);

        return "home"; // Ensure home.html can display the user's email
    }
}
