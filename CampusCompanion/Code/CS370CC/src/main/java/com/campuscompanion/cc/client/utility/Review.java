package com.campuscompanion.cc.client.utility;

import java.time.LocalDate;

public class Review {
    private int reviewID;
    private String name; // Name of the user being reviewed
    private String reviewer; // Name of the reviewer
    private LocalDate date; // Date of the review
    private int rating; // Rating (e.g., 1-5 scale)
    private String content; // Review content

    // Constructor
    public Review(int reviewID, String name, String reviewer, LocalDate date, int rating, String content) {
        this.reviewID = reviewID;
        this.name = name;
        this.reviewer = reviewer;
        this.date = date;
        this.rating = rating;
        this.content = content;
    }

    // Getters and Setters
    public int getReviewID() {
        return reviewID;
    }

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

}