package com.campuscompanion.cc.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClubsDAO {
    private final CampusCompanionDAO campusCompanionDAO; // Instance of CampusCompanionDAO

    public ClubsDAO() {
        campusCompanionDAO = new CampusCompanionDAO();
    }

    // Method to fetch all clubs
    public List<String> getAllClubs() {
        List<String> clubs = new ArrayList<>();
        String sql = "SELECT Name FROM Clubs";

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                while (rs.next()) {
                    clubs.add(rs.getString("Name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        return clubs;
    }

    // Method to fetch club details by name
    public String getClubPresident(String clubName) {
        String sql = "SELECT President FROM Clubs WHERE LOWER(Name) = LOWER(?)";
        final String[] president = {null};

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                if (rs.next()) {
                    president[0] = rs.getString("President");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, clubName);

        return president[0]; // Return the president if found
    }

    public static void main(String[] args) {
        ClubsDAO clubsDAO = new ClubsDAO();

        // Test: Fetch all clubs
        List<String> allClubs = clubsDAO.getAllClubs();
        System.out.println("Clubs:");
        allClubs.forEach(System.out::println);

        // Test: Fetch a specific club's president
        String president = clubsDAO.getClubPresident("Esports");
        System.out.println("\nPresident of Esports:");
        System.out.println(president != null ? president : "President not found.");
    }
}
