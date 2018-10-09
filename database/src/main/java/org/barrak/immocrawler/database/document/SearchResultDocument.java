package org.barrak.immocrawler.database.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "searchResults")
public class SearchResultDocument {

    @Id
    private String url;

    private RealEstateType realEstateType;

    private String title;

    private String city;

    private int price;

    private int nbRooms;
    private int landSurface;
    private int homeSurface;

    private String imageUrl;

    private Set<String> imageUrls;
    private String description;

    private boolean favorite;
    private boolean sold;

    private boolean error;
    private boolean moved;
    private boolean detailsParsed;

    private ProviderEnum internalProvider;
    private String externalProvider;

    private String internalReference;
    private String externalReference;

    private LocalDateTime created;

    public SearchResultDocument() {
    }

    public SearchResultDocument(String url, ProviderEnum provider, RealEstateType realEstateType, String city, int price) {
        this.url = url;
        this.internalProvider = provider;
        this.realEstateType = realEstateType;
        this.city = city;
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RealEstateType getRealEstateType() {
        return realEstateType;
    }

    public void setRealEstateType(RealEstateType realEstateType) {
        this.realEstateType = realEstateType;
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

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(Set<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
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

    public boolean isDetailsParsed() {
        return detailsParsed;
    }

    public void setDetailsParsed(boolean detailsParsed) {
        this.detailsParsed = detailsParsed;
    }


    public ProviderEnum getInternalProvider() {
        return internalProvider;
    }

    public void setInternalProvider(ProviderEnum internalProvider) {
        this.internalProvider = internalProvider;
    }

    public String getExternalProvider() {
        return externalProvider;
    }

    public void setExternalProvider(String externalProvider) {
        this.externalProvider = externalProvider;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
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
