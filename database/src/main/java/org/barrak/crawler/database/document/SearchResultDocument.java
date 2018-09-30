package org.barrak.crawler.database.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import java.time.LocalDateTime;

@Document(collection = "searchResults")
public class SearchResultDocument extends AbstractMongoEventListener {

    @Id
    private String url;

    private String internalProvider;
    private String externalProvider;

    private String title;

    private String city;

    private int price;

    private int nbRooms;
    private int landSurface;
    private int homeSurface;

    private String imageUrl;

    private LocalDateTime created;

    private boolean error;
    private boolean moved;

    public SearchResultDocument() {
    }

    public SearchResultDocument(String url, String provider, String city, int price) {
        this.url = url;
        this.internalProvider = provider;
        this.city = city;
        this.price = price;
    }

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

    public String getInternalProvider() {
        return internalProvider;
    }

    public void setInternalProvider(String internalProvider) {
        this.internalProvider = internalProvider;
    }

    public String getExternalProvider() {
        return externalProvider;
    }

    public void setExternalProvider(String externalProvider) {
        this.externalProvider = externalProvider;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    @Override
    public String toString() {
        return "SearchResultDocument{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", city='" + city + '\'' +
                ", price=" + price + '\'' +
                ", imageUrl=" + imageUrl +
                '}';
    }
}
