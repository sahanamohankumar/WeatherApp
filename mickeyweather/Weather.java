package com.example.mickeyweather;

public class Weather {
    private double minTemp;
    private double maxTemp;
    private String description;
    private double currentTemp;
    private double lat;
    private double lon;
    private String date;
    private String location;

    public Weather(double minTemp, double maxTemp, String description, double currentTemp, double lat, double lon, String date, String location) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.description = description;
        this.currentTemp = currentTemp;
        this.lat = lat;
        this.lon = lon;
        this.date = date;
        this.location = location;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public double getCurrentTemp() {

        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {

        this.currentTemp = currentTemp;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat) {

        this.lat = lat;
    }

    public double getLon() {

        return lon;
    }

    public void setLon(double lon) {

        this.lon = lon;
    }

    public String getDate() {

        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public String getLocation() {

        return location;
    }

    public void setLocation(String location) {

        this.location = location;
    }
}

