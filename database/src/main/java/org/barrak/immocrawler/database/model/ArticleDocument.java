package org.barrak.immocrawler.database.model;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.RealEstateTypeEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.StringJoiner;

@Document(collection = "articles")
public class ArticleDocument {

    @Id
    private ArticleDocumentKey id;

    private String url;

    private RealEstateTypeEnum realEstateTypeEnum;

    private String title;

    private String city;

    private int price = -1;

    private int nbRooms = -1;
    private int landSurface = -1;
    private int homeSurface = -1;

    private String imageUrl;

    private Set<String> imageUrls;
    private String description;

    private boolean sold;

    private boolean error;
    private boolean moved;
    private boolean detailsParsed;

    private String externalProvider;

    private String internalReference;
    private String externalReference;

    @CreatedDate
    private LocalDateTime created;

    public ArticleDocument() {
    }

    public ArticleDocument(String id, String url, ProviderEnum provider, RealEstateTypeEnum realEstateTypeEnum, String city, int price) {
        this.id = new ArticleDocumentKey(provider, id);
        this.url = url;
        this.realEstateTypeEnum = realEstateTypeEnum;
        this.city = city;
        this.price = price;
    }

    public ArticleDocumentKey getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RealEstateTypeEnum getRealEstateTypeEnum() {
        return realEstateTypeEnum;
    }

    public void setRealEstateTypeEnum(RealEstateTypeEnum realEstateTypeEnum) {
        this.realEstateTypeEnum = realEstateTypeEnum;
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
        return id.getProvider();
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

    @Override
    public String toString() {
        return new StringJoiner(", ", "[", "]")
                .add("title='" + title + "'")
                .add("city='" + city + "'")
                .add("landSurface='" + landSurface + "'")
                .add("price=" + price)
                .add("url='" + url + "'")
                .toString();
    }
}
