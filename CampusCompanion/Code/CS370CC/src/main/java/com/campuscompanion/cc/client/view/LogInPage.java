package com.campuscompanion.cc.client.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogInPage {
    // Display the login page
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Returns login.html
    }
}
