package org.barrak.crawler.database.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import java.time.LocalDateTime;
import java.util.Set;

public class SearchResultDetailsDocument extends AbstractMongoEventListener {

    @Id
    private String url;

    private String description;

    private String city;

    private int price;

    private int nbRooms;
    private double landSurface;
    private int homeSurface;

    private Set<String> imageUrls;

    private int internalReference;
    private int externalReference;

    private LocalDateTime created;

    @Override
    public void onBeforeSave(BeforeSaveEvent event) {
        this.created = LocalDateTime.now();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getLandSurface() {
        return landSurface;
    }

    public void setLandSurface(double landSurface) {
        this.landSurface = landSurface;
    }

    public int getHomeSurface() {
        return homeSurface;
    }

    public void setHomeSurface(int homeSurface) {
        this.homeSurface = homeSurface;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
