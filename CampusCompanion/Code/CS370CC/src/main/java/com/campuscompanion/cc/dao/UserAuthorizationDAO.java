package com.campuscompanion.cc.dao;

import com.campuscompanion.cc.client.utility.Review;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class UserAuthorizationDAO {

    private final CampusCompanionDAO campusCompanionDAO;

    public UserAuthorizationDAO() {
        campusCompanionDAO = new CampusCompanionDAO();
    }

    public boolean checkAuthorization(String email, String role) {
        // SQL query to check if the user with the given email has the specified role
        String sql = "SELECT COUNT(*) AS count FROM Users WHERE Email = ? AND Role = ?";
        final boolean[] isAuthorized = {false};

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                if (rs.next()) {
                    int count = rs.getInt("count"); // Check if a matching user-role pair exists
                    isAuthorized[0] = count > 0; // Authorized if count > 0
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Log any SQL exceptions
            }
        }, email, role);

        return isAuthorized[0];
    }

    public List<Review> getUserReviews(String email) {
        String sql = "SELECT ReviewID, Name, Reviewer, Date, Rating, Content FROM Reviews WHERE Name = ?";
        List<Review> reviews = new ArrayList<>();

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                while (rs.next()) {
                    // Map the database row to a Review object
                    Review review = new Review(
                            rs.getInt("ReviewID"),
                            rs.getString("Name"),
                            rs.getString("Reviewer"),
                            rs.getDate("Date").toLocalDate(), // Convert SQL Date to LocalDate
                            rs.getInt("Rating"),
                            rs.getString("Content")
                    );
                    reviews.add(review);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, email);

        return reviews;
    }

    public String getRole(String email) {
        String sql = "SELECT Role FROM Users WHERE Email = ?";
        final String[] role = {null};

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                if (rs.next()) {
                    role[0] = rs.getString("Role"); // Retrieve the Role column value
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle SQL exceptions
            }
        }, email);

        return role[0];
    }
}
