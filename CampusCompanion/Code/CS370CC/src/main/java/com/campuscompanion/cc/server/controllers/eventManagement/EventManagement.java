package com.campuscompanion.cc.server.controllers.eventManagement;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.campuscompanion.cc.dao.EventDAO;
import com.campuscompanion.cc.client.utility.Event;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class EventManagement {
    private final EventDAO eventDAO = new EventDAO();

    // Add an event
    public void addEvent(String club, String name, String date, String location) {
        // EventID will be generated automatically by the database, so pass 0 or leave
        // it out
        Event event = new Event(0, club, name, date, location, 0); // EventID will be auto-generated
        eventDAO.addEvent(event);
    }

    // Modify an event
    public void modifyEvent(int EventID, String name, String date, String location) {
        Event existingEvent = eventDAO.getEventByID(EventID); // Assuming this method exists in EventDAO

        if (existingEvent != null) {
            Event event = new Event(EventID, existingEvent.getClub(), name, date, location,
                    existingEvent.getRSVPCount());
            eventDAO.modifyEvent(event); // Modify the event in the database
        } else {
            System.out.println("Event with ID " + EventID + " not found.");
        }
    }

    // Remove an event
    public void removeEvent(int eventID) {
        eventDAO.removeEvent(eventID);
    }

    // Get events by location
    public List<Event> getEventsByLocation(String location) {
        return eventDAO.getEventsByLocation(location);
    }

    // RSVP to an event
    public void rsvp(int eventID) {
        eventDAO.rsvpEvent(eventID);
    }

    // Get RSVP count for an event
    public int getRSVPCount(int eventID) {
        return eventDAO.getRSVPCount(eventID);
    }

    // Cancel RSVP for an event
    public void cancelRSVP(int eventID) {
        eventDAO.cancelRSVP(eventID);
    }

    // Get event by club
    public List<Event> getEventsByClub(String clubName) {
        return eventDAO.getEventsByClub(clubName);
    }

    // Fetch event details along with building coordinates
    public String getEventDetails(int eventID, Model model) {
        // Fetch event details
        Event event = eventDAO.getEventByID(eventID);
        if (event == null) {
            return "error"; // Return an error page if the event is not found
        }

        // Fetch coordinates using the event's location
        String coordinates = eventDAO.getEventCoordinates(eventID);

        // Add data to the model
        model.addAttribute("event", event); // Pass event details to the view
        model.addAttribute("coordinates", coordinates); // Pass coordinates to the view

        return "club-detail"; // Return the name of the HTML template
    }

    @PostMapping("/events/rsvp")
    public String rsvpToEvent(@RequestParam int eventID, @RequestParam String clubName) {
        // Increment RSVP count for the event
        eventDAO.rsvpEvent(eventID);

        // Redirect back to the specific club's detail page
        return "redirect:/club-detail/" + clubName;
    }

}
