package com.campuscompanion.cc.server.controllers.bathroomManagement;

import com.campuscompanion.cc.client.utility.Bathroom;
import com.campuscompanion.cc.dao.BathroomDAO;
import com.campuscompanion.cc.dao.UserAuthorizationDAO;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
public class BathroomManagement {
    private final BathroomDAO bathroomDAO;

    public BathroomManagement() {
        bathroomDAO = new BathroomDAO();
    }

    @GetMapping("/bathrooms")
    public String getBathroomsPage(
            @RequestParam(defaultValue = "all") String building, // Filter by building
            @RequestParam(defaultValue = "alphabetical") String sortBy, // Sorting option
            HttpSession session, Model model) {

        // Retrieve the email from the session
        String email = (String) session.getAttribute("email");

        // If email is null, redirect to login (user is not logged in)
        if (email == null) {
            return "redirect:/login?error=Please log in first";
        }

        // Add the email to the model for display
        model.addAttribute("userEmail", email);

        // Fetch filtered and sorted bathrooms from DAO
        List<Bathroom> bathrooms = bathroomDAO.getFilteredAndSortedBathrooms(building, sortBy);

        // Fetch the list of unique buildings
        List<String> buildings = bathroomDAO.getAllBuildingNames();

        // Add data to the model
        model.addAttribute("bathrooms", bathrooms); // Pass bathrooms to the front-end
        model.addAttribute("buildings", buildings); // Pass list of buildings to the front-end
        model.addAttribute("selectedBuilding", building); // Preserve selected building in dropdown
        model.addAttribute("selectedSortBy", sortBy); // Preserve selected sort option

        return "bathrooms"; // Return the name of the HTML template
    }

    // Get a single bathroom's details by its name
    @GetMapping("/bathroom-details/{bathroom}")
    public String getBathroomDetails(@PathVariable String bathroom, HttpSession session, Model model) {
        // Retrieve the email from the session
        String email = (String) session.getAttribute("email");

        // If email is null, redirect to login (user is not logged in)
        if (email == null) {
            return "redirect:/login?error=Please log in first";
        }

        // Add the email to the model for display
        model.addAttribute("userEmail", email);

        // Check if the user is an maintenance
        UserAuthorizationDAO authorizationDAO = new UserAuthorizationDAO();
        String role = authorizationDAO.getRole(email);
        boolean isMaintenance = role != null && role.equalsIgnoreCase("maintenance");

        // Add isMaintenance to the model
        model.addAttribute("isMaintenance", isMaintenance);

        // Fetch bathroom details
        Bathroom bathroomDetails = bathroomDAO.getBathroomByName(bathroom);
        if (bathroomDetails == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bathroom not found");
        }

        // Fetch building coordinates using the bathroom's location
        String coordinates = bathroomDAO.getBathroomCoordinates(bathroom);

        // Add data to the model
        model.addAttribute("bathroom", bathroomDetails);
        model.addAttribute("coordinates", coordinates);

        return "bathroom-details"; // Return the name of the HTML template
    }

    // For EventHandler compatibility
    public List<Bathroom> getBathrooms() {
        return bathroomDAO.getAllBathrooms();
    }

    public Bathroom getBathroom(String bathroom) {
        return bathroomDAO.getBathroomByName(bathroom);
    }

    // Update the restroom status (used in EventHandler)
    public void updateRestroomStatus(String bathroom, Boolean status) {
        bathroomDAO.updateRestroomStatus(bathroom, status);
    }

    // Add a rating to a bathroom, return 1 if successful
    public int addRating(String bathroom, double rating, String email) {
        return bathroomDAO.addRating(bathroom, rating, email);
    }

    @PostMapping("/bathroom-details/rate")
    public String rateBathroom(@RequestParam String bathroom, @RequestParam double rating, HttpSession session) {
        // Retrieve the email from the session
        String email = (String) session.getAttribute("email");

        // If email is null, redirect to login (user is not logged in)
        if (email == null) {
            return "redirect:/login?error=Please log in first";
        }

        // Call the DAO to handle the rating
        int result = bathroomDAO.addRating(bathroom, rating, email);

        // Check the result to determine the next step
        if (result == 1) {
            return "redirect:/bathroom-details/" + bathroom + "?success=ratingAdded";
        } else {
            return "redirect:/bathroom-details/" + bathroom + "?error=ratingFailed";
        }
    }

    @PostMapping("/bathroom-details/updateStatus")
    public String updateBathroomStatus(
            @RequestParam String bathroom,
            @RequestParam boolean status,
            HttpSession session) {

        // Get the user's email from the session
        String email = (String) session.getAttribute("email");

        // Use UserAuthorizationDAO to check the user's role
        UserAuthorizationDAO authorizationDAO = new UserAuthorizationDAO();
        String role = authorizationDAO.getRole(email);

        // If the user is not an maintenance, deny the action
        if (role == null || !role.equalsIgnoreCase("maintenance")) {
            return "redirect:/bathroom-details/" + bathroom + "?error=Unauthorized";
        }

        // Update the bathroom status
        bathroomDAO.updateRestroomStatus(bathroom, status);

        // Redirect back to the bathroom details page with a success message
        return "redirect:/bathroom-details/" + bathroom + "?success=StatusUpdated";
    }

}
