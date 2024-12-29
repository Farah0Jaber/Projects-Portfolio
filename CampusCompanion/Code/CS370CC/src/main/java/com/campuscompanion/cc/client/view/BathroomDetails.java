package com.campuscompanion.cc.client.view;

import com.campuscompanion.cc.client.EventHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/bathroom-details")
public class BathroomDetails {

    private final EventHandler eventHandler;

    public BathroomDetails() {
        this.eventHandler = new EventHandler();
    }

    @GetMapping("/{bathroomName}")
    public String getBathroomDetails(@PathVariable String bathroomName, Model model, HttpSession session) {
        // Fetch bathroom details using EventHandler
        @SuppressWarnings("unchecked")
        Map<String, Object> bathroomDetails = (Map<String, Object>) eventHandler.handleCall("getBathroomByName",
                bathroomName);

        if (bathroomDetails == null) {
            // Handle case where bathroom is not found
            return "redirect:/bathrooms?error=notFound";
        }

        model.addAttribute("bathroom", bathroomDetails);

        // Check if the user has already rated this bathroom
        @SuppressWarnings("unchecked")
        Map<String, Boolean> ratedBathrooms = (Map<String, Boolean>) session.getAttribute("ratedBathrooms");
        if (ratedBathrooms != null && Boolean.TRUE.equals(ratedBathrooms.get(bathroomName))) {
            model.addAttribute("alreadyRated", true);
        } else {
            model.addAttribute("alreadyRated", false);
        }

        return "bathroom-details";
    }

    @PostMapping("/rate")
    public String rateBathroom(@RequestParam String bathroomName, @RequestParam double rating, HttpSession session) {
        // Check if the user has already rated this bathroom
        System.out.println(
                "POST /bathroom-details/rate triggered with bathroomName: " + bathroomName + " and rating: " + rating);
        @SuppressWarnings("unchecked")
        Map<String, Boolean> ratedBathrooms = (Map<String, Boolean>) session.getAttribute("ratedBathrooms");
        if (ratedBathrooms == null) {
            ratedBathrooms = new HashMap<>();
        }

        if (Boolean.TRUE.equals(ratedBathrooms.get(bathroomName))) {
            return "redirect:/bathroom-details/" + bathroomName + "?error=alreadyRated";
        }

        // Update bathroom rating using EventHandler
        boolean updateSuccess = (boolean) eventHandler.handleCall("updateBathroomRating", bathroomName, rating);

        if (!updateSuccess) {
            return "redirect:/bathroom-details/" + bathroomName + "?error=updateFailed";
        }

        // Mark this bathroom as rated by the user
        ratedBathrooms.put(bathroomName, true);
        session.setAttribute("ratedBathrooms", ratedBathrooms);

        return "redirect:/bathroom-details/" + bathroomName;
    }

    @PostMapping("/test")
    public void testPostEndpoint() {
        System.out.println("POST /bathroom-details/test triggered successfully!");
    }

}