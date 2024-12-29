package com.campuscompanion.cc.client;

import com.campuscompanion.cc.server.RequestManager;
import com.campuscompanion.cc.client.utility.Credentials;
import com.campuscompanion.cc.client.utility.Review;

import java.util.List;

public class EventHandler {
    private final RequestManager requestManager;

    public EventHandler() {
        this.requestManager = new RequestManager();
    }

    public Object handleCall(String requestType, Object... params) {
        switch (requestType) {
            // Event Management Cases
            case "addEvent":
                // Add an event
                requestManager.getEventManagement().addEvent((String) params[0], (String) params[1], (String) params[2],
                        (String) params[3]);
                return null;
            case "modifyEvent":
                // Modify an event
                requestManager.getEventManagement().modifyEvent((int) params[0], (String) params[1], (String) params[2],
                        (String) params[3]);
                return null;
            case "removeEvent":
                // Remove an event
                requestManager.getEventManagement().removeEvent((int) params[0]);
                return null;
            case "getEventsByLocation":
                // Get events by location
                return requestManager.getEventManagement().getEventsByLocation((String) params[0]);
            case "rsvp":
                // RSVP to an event
                requestManager.getEventManagement().rsvp((int) params[0]);
                return null;
            case "getRSVPCount":
                // Get RSVP count for an event
                return requestManager.getEventManagement().getRSVPCount((int) params[0]);
            case "cancelRSVP":
                // Cancel RSVP for an event
                requestManager.getEventManagement().cancelRSVP((int) params[0]);
                return null;
            case "getEventsByClub":
                // Get events for a specific club
                String clubName = (String) params[0];
                return requestManager.getEventManagement().getEventsByClub(clubName);

            // Bathroom Management Cases
            case "getAllBathrooms":
                // Get all bathrooms
                return requestManager.getBathroomManagement().getBathrooms();
            case "getBathroomByName":
                // Get a bathroom by name
                return requestManager.getBathroomManagement().getBathroom((String) params[0]);
            case "updateRestroomStatus":
                // Update restroom status (open/closed)
                requestManager.getBathroomManagement().updateRestroomStatus((String) params[0], (Boolean) params[1]);
                return null;
            case "updateBathroomRating":
                // Debug log to ensure this method is triggered
                System.out.println("Updating bathroom rating in EventHandler: bathroomName="
                        + params[0] + ", rating=" + params[1]);
                // Call the actual method to update the bathroom rating
                return requestManager.getBathroomManagement().addRating((String) params[0],
                        (double) params[1], "test@example.com"); // Replace email with actual user email if available.

            // User Management Cases
            case "authenticateUser":
                // Authenticate a user
                Credentials credentials = (Credentials) params[0];
                return requestManager.getUserManagement().authenticateUser(credentials);
            case "authorizeUser":
                // Authorize a user for a specific role
                return requestManager.getUserManagement().authorizeUser((String) params[0], (String) params[1]);
            case "resetPassword":
                // Reset a user's password
                requestManager.getUserManagement().resetPassword((String) params[0], (String) params[1]);
                return null;
            case "getUserRole":
                // Get a user's role by email
                return requestManager.getUserManagement().getUserRole((String) params[0]);
            case "getUserReviews":
                // Get a user's reviews by email
                return requestManager.getUserManagement().getReviews((String) params[0]);

            default:
                throw new IllegalArgumentException("Unknown request type: " + requestType);
        }
    }
}