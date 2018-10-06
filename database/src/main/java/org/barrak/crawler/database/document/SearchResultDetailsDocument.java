package org.barrak.crawler.database.document;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Set;

public class SearchResultDetailsDocument {

    public SearchResultDetailsDocument() {
    }

    public SearchResultDetailsDocument(SearchResultDocument searchResultDocument) {
        setInternalReference(searchResultDocument.getInternalProvider());
        setUrl(searchResultDocument.getUrl());
        setCity(searchResultDocument.getCity());
        setPrice(searchResultDocument.getPrice());
        setHomeSurface(searchResultDocument.getHomeSurface());
        setLandSurface(searchResultDocument.getLandSurface());
        setNbRooms(searchResultDocument.getNbRooms());
    }

    @Id
    private String url;

    private String description;

    private String city;

    private int price;

    private int nbRooms;
    private double landSurface;
    private int homeSurface;

    private Set<String> imageUrls;

    private ProviderEnum internalReference;
    private int externalReference;

    private char dpe;
    private char ges;

    private LocalDateTime created;

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

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public ProviderEnum getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(ProviderEnum internalReference) {
        this.internalReference = internalReference;
    }

    public int getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(int externalReference) {
        this.externalReference = externalReference;
    }

    public char getDpe() {
        return dpe;
    }

    public void setDpe(char dpe) {
        this.dpe = dpe;
    }

    public char getGes() {
        return ges;
    }

    public void setGes(char ges) {
        this.ges = ges;
    }
}
