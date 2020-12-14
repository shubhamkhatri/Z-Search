package com.example.android.zomatosearch.model;

public class Location {
    private String address;
    private String latitude;
    private String longitude;

    public Location(String address, String latitude, String longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}