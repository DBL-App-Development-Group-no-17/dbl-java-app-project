package com.example.tutorhub;

public class LastLocation {
    public double latitude;
    public double longitude;

    LastLocation(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    LastLocation() {
        this.latitude = 0.0;
        this.longitude = 0.0;
    }
}
