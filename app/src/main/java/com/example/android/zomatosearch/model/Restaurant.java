package com.example.android.zomatosearch.model;


public class Restaurant {
    private String id;
    private String name;
    private Location location;
    private String cuisines;
    private int average_cost_for_two;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getCuisines() {
        return cuisines;
    }

    public int getAverage_cost_for_two() {
        return average_cost_for_two;
    }

    public Restaurant(String id, String name, Location location, String cuisines, int average_cost_for_two) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.cuisines=cuisines;
        this.average_cost_for_two=average_cost_for_two;
    }
}
