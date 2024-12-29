package com.campuscompanion.cc.client.utility;

public class Bathroom {
    private String name;
    private boolean status;
    private double rating;
    private String location;
    private int ratingCount;

    public Bathroom(String name, boolean status, double rating, String location) {
        this.name = name;
        this.status = status;
        this.rating = rating;
        this.location = location;
        ratingCount = 0;
    }

    public Bathroom(String name, boolean status, double rating, String location, int ratingCount) {
        this.name = name;
        this.status = status;
        this.rating = rating;
        this.location = location;
        this.ratingCount = ratingCount;
    }

    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }

    public double getRating() {
        return rating;
    }

    public String getLocation() {
        return location;
    }

    public int getRatingCount() {
        return ratingCount;
    }
}