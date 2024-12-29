package com.campuscompanion.cc.dao;

import com.campuscompanion.cc.client.utility.Event;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    private final CampusCompanionDAO campusCompanionDAO = new CampusCompanionDAO();

    // Add event to the database with auto-generated EventID
    public void addEvent(Event event) {
        String sql = "INSERT INTO Events (Club, Name, Date, Location, RSVPCount) VALUES (?, ?, ?, ?, ?)";
        campusCompanionDAO.updateDB(sql, event.getClub(), event.getName(), event.getDate(), event.getLocation(),
                event.getRSVPCount());
    }

    // Modify event in the database
    public void modifyEvent(Event event) {
        String sql = "UPDATE Events SET Name = ?, Date = ?, Location = ? WHERE EventID = ?";
        campusCompanionDAO.updateDB(sql, event.getName(), event.getDate(), event.getLocation(), event.getEventID());
    }

    // Remove event from the database
    public void removeEvent(int eventID) {
        String sql = "DELETE FROM Events WHERE EventID = ?";
        campusCompanionDAO.updateDB(sql, eventID);
    }

    // Get events by location
    public List<Event> getEventsByLocation(String location) {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM Events WHERE location = ?";

        campusCompanionDAO.processQueryDB(query, rs -> {
            try {
                while (rs.next()) {
                    Event event = new Event();
                    event.setEventID(rs.getInt("EventID"));
                    event.setLocation(rs.getString("Location"));
                    event.setName(rs.getString("Name"));
                    event.setDate(rs.getString("Date")); // Directly retrieve the date as a String
                    event.setRSVPCount(rs.getInt("RSVPCount"));
                    events.add(event);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, location);

        return events;
    }

    public Event getEventByID(int eventID) {
        final Event[] event = {null};
        String sql = "SELECT * FROM events WHERE EventID = ?"; // Replace 'events' with your actual table name

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                if (rs.next()) {
                    String club = rs.getString("club");
                    String name = rs.getString("name");
                    String date = rs.getString("date");
                    String location = rs.getString("location");
                    int rsvpCount = rs.getInt("RSVPCount"); // Assuming this column exists

                    event[0] = new Event(eventID, club, name, date, location, rsvpCount);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, eventID);

        return event[0]; // Return null if no event found
    }

    // Increment RSVP count
    public void rsvpEvent(int eventID) {
        String sql = "UPDATE Events SET RSVPCount = RSVPCount + 1 WHERE EventID = ?";
        campusCompanionDAO.updateDB(sql, eventID);
    }

    // Get RSVP count
    public int getRSVPCount(int eventID) {
        final int[] rsvpCount = {0};
        String sql = "SELECT RSVPCount FROM Events WHERE EventID = ?";

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                if (rs.next()) {
                    rsvpCount[0] = rs.getInt("RSVPCount");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, eventID);

        return rsvpCount[0];
    }

    // Cancel RSVP (decrement count)
    public void cancelRSVP(int eventID) {
        String sql = "UPDATE Events SET RSVPCount = RSVPCount - 1 WHERE EventID = ?";
        campusCompanionDAO.updateDB(sql, eventID);
    }

    // Get event by club
    public List<Event> getEventsByClub(String clubName) {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM Events WHERE Club = ?";

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                while (rs.next()) {
                    events.add(new Event(
                            rs.getInt("EventID"),
                            rs.getString("Club"),
                            rs.getString("Name"),
                            rs.getString("Date"),
                            rs.getString("Location"),
                            rs.getInt("RSVPCount")));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, clubName);

        return events;
    }

    // Method to fetch event coordinates based on location
    public String getEventCoordinates(int eventID) {
        final String[] coordinates = {null};
        String sql = "SELECT b.Coordinates AS BuildingCoordinates " +
                "FROM Events e " +
                "JOIN Buildings b ON e.Location = b.Name " +
                "WHERE e.EventID = ?";

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                if (rs.next()) {
                    coordinates[0] = rs.getString("BuildingCoordinates"); // Return the coordinates
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, eventID);

        return coordinates[0]; // Return null if no coordinates are found
    }

    // For debugging and testing
    public static void main(String[] args) {
        EventDAO dao = new EventDAO();
        String coordinates = dao.getEventCoordinates(1); // Replace 1 with an actual EventID
        if (coordinates != null) {
            System.out.println("Coordinates: " + coordinates);
        } else {
            System.out.println("Coordinates not found!");
        }
    }
}
