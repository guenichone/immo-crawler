package org.barrak.immocrawler.crawler.results;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    private String city;

    private int price;
    private int nbRooms;
    private int landSurface;
    private int homeSurface;

    private List<String> images = new ArrayList<>();

    public SearchResult() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNbRooms() {
        return nbRooms;
    }

    public void setNbRooms(int nbRooms) {
        this.nbRooms = nbRooms;
    }

    public int getLandSurface() {
        return landSurface;
    }

    public void setLandSurface(int landSurface) {
        this.landSurface = landSurface;
    }

    public int getHomeSurface() {
        return homeSurface;
    }

    public void setHomeSurface(int homeSurface) {
        this.homeSurface = homeSurface;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
