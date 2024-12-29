package com.campuscompanion.cc.client.view;

import com.campuscompanion.cc.client.EventHandler;
import com.campuscompanion.cc.client.utility.Bathroom;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BathroomPage {

    private final EventHandler eventHandler;

    public BathroomPage() {
        this.eventHandler = new EventHandler();
    }

    @GetMapping("/bathrooms")
    public String bathroomsPage(Model model) {
        // Use EventHandler to get all bathrooms
        List<Bathroom> bathrooms = (List<Bathroom>) eventHandler.handleCall("getAllBathrooms");

        // Add the bathrooms to the model
        model.addAttribute("bathrooms", bathrooms);

        return "bathrooms"; // Name of the view (e.g., `bathrooms.html` in templates)
    }
}