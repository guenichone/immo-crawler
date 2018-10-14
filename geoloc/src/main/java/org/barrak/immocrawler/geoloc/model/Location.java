package org.barrak.immocrawler.geoloc.model;

public class Location {

    private String city;
    private String postalCode;

    private double lat;
    private double lng;

    public Location(String city, String postalCode, double lat, double lng) {
        this.city = city;
        this.postalCode = postalCode;
        this.lat = lat;
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
