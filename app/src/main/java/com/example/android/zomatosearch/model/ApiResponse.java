package com.example.android.zomatosearch.model;

import java.util.List;

public class ApiResponse {
    private String results_found;
    private String  results_start;
    private String results_shown;
    private List<RestaurantsItem> restaurants;

    public ApiResponse(String results_found, String results_start, String results_shown, List<RestaurantsItem> restaurants) {
        this.results_found = results_found;
        this.results_start = results_start;
        this.results_shown = results_shown;
        this.restaurants = restaurants;
    }

    public String getResults_found() {
        return results_found;
    }

    public String getResults_start() {
        return results_start;
    }

    public String getResults_shown() {
        return results_shown;
    }

    public List<RestaurantsItem> getRestaurants() {
        return restaurants;
    }
}