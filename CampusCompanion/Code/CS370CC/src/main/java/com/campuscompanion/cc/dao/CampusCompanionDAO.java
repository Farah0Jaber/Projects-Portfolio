package com.campuscompanion.cc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class CampusCompanionDAO {

    public void processQueryDB(String sqlStatement, Consumer<ResultSet> resultProcessor, Object... params) {
        String url = getDatabaseUrl();

        if (url == null) {
            throw new IllegalStateException("Database URL is null. Please verify the path.");
        }

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sqlStatement)) {

            // Set the parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // Execute the query and process the ResultSet
            try (ResultSet rs = pstmt.executeQuery()) {
                resultProcessor.accept(rs); // Pass the ResultSet to the processor
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet queryDB(String sqlStatement, Object... params) {
        String url = getDatabaseUrl();

        if (url == null) {
            return null; // Return null if the database URL is not available
        }

        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Establish connection and prepare the statement
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sqlStatement);

            // Set the parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            // Return the ResultSet for processing
            return pstmt.executeQuery();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void updateDB(String sql, Object... params) {
        String url = getDatabaseUrl();

        if (url == null) {
            return;
        }
        try (Connection conn = DriverManager.getConnection(url);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    // Method to get the database URL
    private String getDatabaseUrl() {
        // Replace with absolute path of CCDB.sqlite
        String DBPath = "PASTE PATH HERE";
        // String DBPath = "/Users/dyerjo/Downloads/CS370-Fall2024-Team10-CampusCompanion-main 6/Code/CS370CC/CCDB.sqlite";
        // Return the database URL with the absolute path
        return "jdbc:sqlite:" + DBPath;
    }

}