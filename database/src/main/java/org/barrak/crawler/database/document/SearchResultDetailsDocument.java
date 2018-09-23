package org.barrak.crawler.database.document;


import org.springframework.data.annotation.Id;

import java.util.List;

public class SearchResultDetailsDocument {

    @Id
    private String url;

    private String description;

    private String city;

    private int price;

    private int nbRooms;
    private int landSurface;
    private int homeSurface;

    private List<String> imageUrls;

}
