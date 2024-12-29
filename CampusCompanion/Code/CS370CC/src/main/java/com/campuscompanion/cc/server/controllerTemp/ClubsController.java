package com.campuscompanion.cc.server.controllerTemp;

import com.campuscompanion.cc.dao.ClubsDAO;
import com.campuscompanion.cc.dao.EventDAO;
import com.campuscompanion.cc.client.utility.Event;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ClubsController {
    private final ClubsDAO clubsDAO;

    public ClubsController() {
        this.clubsDAO = new ClubsDAO();
    }

    // Route for the clubs page
    @GetMapping("/clubs")
    public String getClubsPage(HttpSession session, Model model) {
        // Retrieve the email from the session
        String email = (String) session.getAttribute("email");

        // If email is null, redirect to login (user is not logged in)
        if (email == null) {
            return "redirect:/login?error=Please log in first";
        }

        // Add the email to the model
        model.addAttribute("userEmail", email);

        // Fetch all clubs from the database
        List<String> clubs = clubsDAO.getAllClubs();

        // Add the clubs to the model
        model.addAttribute("clubs", clubs);

        return "clubs"; // This will return the clubs.html view
    }

    // Get events by club
    @GetMapping("/club-events/{clubName}")
    public String getClubEvents(@PathVariable String clubName, HttpSession session, Model model) {
        // Retrieve the email from the session
        String email = (String) session.getAttribute("email");

        // If email is null, redirect to login (user is not logged in)
        if (email == null) {
            return "redirect:/login?error=Please log in first";
        }

        // Add the email to the model
        model.addAttribute("userEmail", email);

        EventDAO eventDAO = new EventDAO(); // Instantiate EventDAO
        List<Event> events = eventDAO.getEventsByClub(clubName); // Fetch events for the club

        model.addAttribute("clubName", clubName); // Add club name to the model
        model.addAttribute("events", events); // Add events to the model

        return "club-events"; // Name of the Thymeleaf template
    }

    // Route for club detail page
    @GetMapping("/club-detail/{clubId}")
    public String getClubDetailPage(@PathVariable String clubId, HttpSession session, Model model) {
        // Retrieve the email from the session
        String email = (String) session.getAttribute("email");

        // If email is null, redirect to login (user is not logged in)
        if (email == null) {
            return "redirect:/login?error=Please log in first";
        }

        // Add the email to the model
        model.addAttribute("userEmail", email);

        // Fetch club details dynamically using DAO
        String president = clubsDAO.getClubPresident(clubId);

        // Check if club exists
        if (president == null) {
            return "error"; // Club not found, return error page
        }

        // Fetch events for the club
        EventDAO eventDAO = new EventDAO();
        List<Event> events = eventDAO.getEventsByClub(clubId);

        // Add club details to the model
        model.addAttribute("clubName", clubId); // Use the club name as the ID
        model.addAttribute("president", president);
        model.addAttribute("events", events); // Add events to the model

        return "club-detail"; // This will return the club-detail.html view
    }
}
