package com.campuscompanion.cc.dao;

import com.campuscompanion.cc.client.utility.Credentials;

import java.sql.SQLException;

public class UserAuthenticationDAO {

    private final CampusCompanionDAO campusCompanionDAO;

    public UserAuthenticationDAO() {
        campusCompanionDAO = new CampusCompanionDAO();
    }

    public boolean checkCredentials(Credentials credentials) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ? AND Password = ?";
        final boolean[] isValid = {false};

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                if (rs.next()) {
                    int count = rs.getInt(1); // Get the count from the first column
                    isValid[0] = count > 0; // Valid if count > 0
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, credentials.getEmail(), credentials.getPassword());

        return isValid[0];
    }

    public void resetPW(String email, String password) {
        String sql = "UPDATE Users SET Password = ? WHERE Email = ?";
        campusCompanionDAO.updateDB(sql, password, email);
    }

    public void registerUser(String email, String fullName, String password, String role) {
        String sql = "INSERT INTO Users (Email, FullName, Password, Role) VALUES (?, ?, ?, ?)";
        campusCompanionDAO.updateDB(sql, email, fullName, password, role);
    }

    public boolean isEmailRegistered(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
        final boolean[] isRegistered = {false};

        campusCompanionDAO.processQueryDB(sql, rs -> {
            try {
                if (rs.next()) {
                    isRegistered[0] = rs.getInt(1) > 0; // If count > 0, email exists
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, email);

        return isRegistered[0];
    }
}
