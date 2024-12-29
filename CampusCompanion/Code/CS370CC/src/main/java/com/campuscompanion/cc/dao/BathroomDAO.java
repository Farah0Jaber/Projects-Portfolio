package com.campuscompanion.cc.dao;

import com.campuscompanion.cc.client.utility.Bathroom;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BathroomDAO {

    private final CampusCompanionDAO campusCompanionDAO; // Instance of CampusCompanionDAO

    public BathroomDAO() {
        campusCompanionDAO = new CampusCompanionDAO();
    }

    // Method to get all bathrooms using CampusCompanionDAO
    public List<Bathroom> getAllBathrooms() {
        List<Bathroom> bathrooms = new ArrayList<>();
        String sql = "SELECT * FROM Bathroom";

        try {
            campusCompanionDAO.processQueryDB(sql, rs -> {
                while (true) {
                    try {
                        if (!rs.next()) break;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        bathrooms.add(new Bathroom(
                                rs.getString("Name"),
                                rs.getBoolean("Status"),
                                rs.getDouble("Rating"),
                                rs.getString("Location")
                        ));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error fetching all bathrooms: " + e.getMessage());
        }

        return bathrooms;
    }

    // Method to get restroom status by name
    public boolean getRestroomStatus(String name) {
        String sql = "SELECT Status FROM Bathroom WHERE Name = ?";
        final boolean[] status = {false};

        try {
            campusCompanionDAO.processQueryDB(sql, rs -> {
                try {
                    if (rs.next()) {
                        status[0] = rs.getBoolean("Status");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, name);
        } catch (Exception e) {
            System.err.println("Error fetching restroom status: " + e.getMessage());
        }

        return status[0];
    }

    // Method to update the restroom status using CampusCompanionDAO's updateDB
    public void updateRestroomStatus(String name, Boolean status) {
        String sql = "UPDATE Bathroom SET Status = ? WHERE Name = ?";
        try {
            campusCompanionDAO.updateDB(sql, status, name);
        } catch (Exception e) {
            System.err.println("Error updating restroom status: " + e.getMessage());
        }
    }

    // Method to get bathroom details by name
    public Bathroom getBathroomByName(String name) {
        String sql = "SELECT * FROM Bathroom WHERE Name = ?";
        final Bathroom[] bathroomResult = {null};

        try {
            campusCompanionDAO.processQueryDB(sql, rs -> {
                try {
                    if (rs.next()) {
                        int ratingCount = getRatingCountForBathroom(name); // Get associated rating count
                        bathroomResult[0] = new Bathroom(
                                rs.getString("Name"),
                                rs.getBoolean("Status"),
                                rs.getDouble("Rating"),
                                rs.getString("Location"),
                                ratingCount
                        );
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, name);
        } catch (Exception e) {
            System.err.println("Error fetching bathroom by name: " + e.getMessage());
        }

        return bathroomResult[0];
    }

    // Helper method to get the rating count from the Reviews table
    private int getRatingCountForBathroom(String bathroom) {
        String sql = "SELECT COUNT(*) AS RatingCount FROM Reviews WHERE Bathroom = ?";
        final int[] ratingCount = {0};

        try {
            campusCompanionDAO.processQueryDB(sql, rs -> {
                try {
                    if (rs.next()) {
                        ratingCount[0] = rs.getInt("RatingCount");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, bathroom);
        } catch (Exception e) {
            System.err.println("Error fetching rating count for bathroom: " + e.getMessage());
        }

        return ratingCount[0];
    }

    // Method to fetch bathroom details along with building coordinates
    public String getBathroomCoordinates(String bathroom) {
        String sql = "SELECT bl.Coordinates AS BuildingCoordinates " +
                "FROM Bathroom b " +
                "JOIN Buildings bl ON b.Location = bl.Name " +
                "WHERE b.Name = ?";
        final String[] coordinates = {null};

        try {
            campusCompanionDAO.processQueryDB(sql, rs -> {
                try {
                    if (rs.next()) {
                        coordinates[0] = rs.getString("BuildingCoordinates");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, bathroom);
        } catch (Exception e) {
            System.err.println("Error fetching bathroom coordinates: " + e.getMessage());
        }

        return coordinates[0];
    }

    // Method to get bathrooms with filtering and sorting
    public List<Bathroom> getFilteredAndSortedBathrooms(String building, String sortBy) {
        List<Bathroom> bathrooms = new ArrayList<>();
        String sql = "SELECT * FROM Bathroom"; // Base query

        // Add filtering condition if building is specified and not "all"
        if (!"all".equalsIgnoreCase(building)) {
            sql += " WHERE Location = ?";
        }

        // Add sorting condition
        if ("alphabetical".equalsIgnoreCase(sortBy)) {
            sql += " ORDER BY Name ASC";
        } else if ("cleanliest".equalsIgnoreCase(sortBy)) {
            sql += " ORDER BY Rating DESC";
        }

        try {
            campusCompanionDAO.processQueryDB(sql, rs -> {
                while (true) {
                    try {
                        if (!rs.next()) break;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        bathrooms.add(new Bathroom(
                                rs.getString("Name"),
                                rs.getBoolean("Status"),
                                rs.getDouble("Rating"),
                                rs.getString("Location")
                        ));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, !"all".equalsIgnoreCase(building) ? new Object[]{building} : new Object[0]);
        } catch (Exception e) {
            System.err.println("Error fetching filtered and sorted bathrooms: " + e.getMessage());
        }

        return bathrooms;
    }

    // Method to fetch all unique building names except specific ones
    public List<String> getAllBuildingNames() {
        List<String> buildingNames = new ArrayList<>();
        String sql = "SELECT Name FROM Buildings WHERE Name NOT IN (" +
                "'Forum Plaza', 'Palm Court', 'Student Health and Counseling', " +
                "'Track', 'Baseball Field', 'Gym', 'Sports Center')";

        try {
            campusCompanionDAO.processQueryDB(sql, rs -> {
                while (true) {
                    try {
                        if (!rs.next()) break;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        buildingNames.add(rs.getString("Name"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("Error fetching building names: " + e.getMessage());
        }

        return buildingNames;
    }

    // Method to add or update ratings for a bathroom
    public int addRating(String bathroom, double rating, String email) {
        System.out.println("Executing addRating for bathroom: " + bathroom + ", rating: " + rating + ", email: " + email);

        String checkQuery = "SELECT * FROM Reviews WHERE Bathroom = ? AND Reviewer = ?";
        String updateReviewQuery = "UPDATE Reviews SET Rating = ? WHERE Bathroom = ? AND Reviewer = ?";
        String insertReviewQuery = "INSERT INTO Reviews (Bathroom, Reviewer, Rating) VALUES (?, ?, ?)";
        String fetchRatingsQuery = "SELECT SUM(Rating) AS TotalRatings, COUNT(*) AS ReviewCount FROM Reviews WHERE Bathroom = ?";
        String updateBathroomQuery = "UPDATE Bathroom SET Rating = ? WHERE Name = ?";

        try {
            // Check if a review already exists for this bathroom by this user
            final boolean[] existingReview = {false};
            campusCompanionDAO.processQueryDB(checkQuery, rs -> {
                try {
                    if (rs.next()) {
                        existingReview[0] = true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, bathroom, email);

            // Update or insert the review
            if (existingReview[0]) {
                campusCompanionDAO.updateDB(updateReviewQuery, rating, bathroom, email);
            } else {
                campusCompanionDAO.updateDB(insertReviewQuery, bathroom, email, rating);
            }

            // Recalculate average rating
            final double[] totalRatings = {0};
            final int[] reviewCount = {0};
            campusCompanionDAO.processQueryDB(fetchRatingsQuery, rs -> {
                try {
                    if (rs.next()) {
                        totalRatings[0] = rs.getDouble("TotalRatings");
                        reviewCount[0] = rs.getInt("ReviewCount");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }, bathroom);

            double newAverageRating = reviewCount[0] > 0 ? totalRatings[0] / reviewCount[0] : 0.0;
            BigDecimal roundedRating = BigDecimal.valueOf(newAverageRating).setScale(2, RoundingMode.HALF_UP);

            // Update bathroom rating
            campusCompanionDAO.updateDB(updateBathroomQuery, roundedRating.doubleValue(), bathroom);

            return 1; // Success
        } catch (Exception e) {
            System.err.println("Error in addRating: " + e.getMessage());
            return -1; // Error
        }
    }
}
