package com.campuscompanion.cc.client.view;

import java.util.Map;
import com.campuscompanion.cc.client.EventHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/event-details")
public class EventPage {

    private final EventHandler eventHandler;

    public EventPage() {
        this.eventHandler = new EventHandler();
    }

    @GetMapping("/{eventID}")
    public String getEventDetails(@PathVariable int eventID, Model model) {
        // Fetch event details using EventHandler
        @SuppressWarnings("unchecked")
        Map<String, Object> eventDetails = (Map<String, Object>) eventHandler.handleCall("getEventByID", eventID);

        if (eventDetails == null) {
            // Handle case where the event is not found
            return "redirect:/events?error=notFound";
        }

        // Add event details to the model
        model.addAttribute("event", eventDetails);

        // Fetch coordinates for the event's location
        String coordinates = (String) eventHandler.handleCall("getEventCoordinates", eventID);

        if (coordinates == null) {
            model.addAttribute("coordinatesError", "Coordinates not available for this event.");
        } else {
            model.addAttribute("coordinates", coordinates);
        }

        return "event-details"; // Return the event details view
    }

    @PostMapping("/rsvp")
    public String rsvpToEvent(@RequestParam int eventID) {
        // Handle RSVP using EventHandler
        eventHandler.handleCall("rsvp", eventID);

        return "redirect:/event-details/" + eventID; // Redirect back to the event details page
    }
}
