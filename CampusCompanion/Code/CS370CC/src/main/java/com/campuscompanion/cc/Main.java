package com.campuscompanion.cc;

import java.util.List;
import java.util.Scanner;
import com.campuscompanion.cc.client.EventHandler;
import com.campuscompanion.cc.client.utility.Credentials;
import com.campuscompanion.cc.client.utility.Review;
import com.campuscompanion.cc.client.utility.Event;

public class Main {
    public static void main(String[] args) {
        EventHandler eventHandler = new EventHandler();
        Scanner scanner = new Scanner(System.in);

        // Test user authentication
        System.out.println("Testing authenticateUser...");
        Credentials credentials = new Credentials("user@example.com", "password123");
        boolean isAuthenticated = (boolean) eventHandler.handleCall("authenticateUser", credentials);
        System.out.println("User authenticated: " + isAuthenticated);
        waitForEnter(scanner);

        // Test user authorization
        System.out.println("Testing authorizeUser...");
        boolean isAuthorized = (boolean) eventHandler.handleCall("authorizeUser", "user@example.com", "admin");
        System.out.println("User authorized: " + isAuthorized);
        waitForEnter(scanner);

        // Test password reset
        System.out.println("Testing resetPassword...");
        eventHandler.handleCall("resetPassword", "user@example.com", "newpassword123");
        System.out.println("Password reset successful.");
        waitForEnter(scanner);

        // Test getting user role
        System.out.println("Testing getUserRole...");
        String role = (String) eventHandler.handleCall("getUserRole", "user@example.com");
        System.out.println("User role: " + role);
        waitForEnter(scanner);

        // Test getting user reviews
        System.out.println("Testing getUserReviews...");
        List<Review> reviews = (List<Review>) eventHandler.handleCall("getUserReviews", "user@example.com");
        for (Review review : reviews) {
            System.out.println("Review: ");
        }
        waitForEnter(scanner);

        // Test fetching events for a specific club
        System.out.println("Testing getEventsByClub...");
        String clubName = "Esports"; // Replace with the name of a club in your database
        List<Event> events = (List<Event>) eventHandler.handleCall("getEventsByClub", clubName);

        if (events != null && !events.isEmpty()) {
            System.out.println("Events for " + clubName + ":");
            for (Event event : events) {
                System.out.println(event.getName() + " on " + event.getDate() + " at " + event.getLocation() +
                        " (RSVP Count: " + event.getRSVPCount() + ")");
            }
        } else {
            System.out.println("No events found for " + clubName);
        }
    }

    // Method to wait for the Enter key before proceeding
    private static void waitForEnter(Scanner scanner) {
        System.out.println("Press Enter to continue...");
        scanner.nextLine(); // Waits for the user to press Enter
    }
}
