package org.barrak.immocrawler.backend.model;

import org.barrak.immocrawler.database.document.ProviderEnum;
import org.barrak.immocrawler.database.document.RealEstateTypeEnum;
import org.barrak.immocrawler.database.model.ArticleDocument;

import java.util.Set;

public class Article {

    private ProviderEnum provider;
    private String id;

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

    public Article() {
    }

    public Article(ArticleDocument articleDocument) {
        this.provider = articleDocument.getId().getProvider();
        this.id = articleDocument.getId().getId();

        this.url = articleDocument.getUrl();
        this.title = articleDocument.getTitle();
        this.city = articleDocument.getCity();

        this.price = articleDocument.getPrice();

        this.landSurface = articleDocument.getLandSurface();
        this.homeSurface = articleDocument.getHomeSurface();

        this.sold = articleDocument.isSold();
    }

    public ProviderEnum getProvider() {
        return provider;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public RealEstateTypeEnum getRealEstateTypeEnum() {
        return realEstateTypeEnum;
    }

    public String getTitle() {
        return title;
    }

    public String getCity() {
        return city;
    }

    public int getPrice() {
        return price;
    }

    public int getNbRooms() {
        return nbRooms;
    }

    public int getLandSurface() {
        return landSurface;
    }

    public int getHomeSurface() {
        return homeSurface;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Set<String> getImageUrls() {
        return imageUrls;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSold() {
        return sold;
    }

    public boolean isError() {
        return error;
    }

    public boolean isMoved() {
        return moved;
    }

    public boolean isDetailsParsed() {
        return detailsParsed;
    }

    public String getExternalProvider() {
        return externalProvider;
    }

    public String getInternalReference() {
        return internalReference;
    }

    public String getExternalReference() {
        return externalReference;
    }
}
