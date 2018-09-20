package org.barrak.immocrawler.crawler.criterias;

import java.util.ArrayList;
import java.util.List;

public class SearchCriteria {

    private List<String> cities = new ArrayList<>();
    private int around;

    private int minPrice;
    private int maxPrice;

    private int minLandSurface;
    private int maxLandSurface;

    private int minHouseSurface;
    private int maxHouseSurface;

    public SearchCriteria(String city, int around) {
        this.cities.add(city);
        this.around = around;
    }

    public SearchCriteria(List<String> cities, int around) {
        this.cities = cities;
        this.around = around;
    }

    public String getCity() {
        return cities.size() > 0 ? cities.get(0) : null;
    }

    public List<String> getCities() {
        return cities;
    }

    public int getAround() {
        return around;
    }

    public void setAround(int around) {
        this.around = around;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getMinLandSurface() {
        return minLandSurface;
    }

    public void setMinLandSurface(int minLandSurface) {
        this.minLandSurface = minLandSurface;
    }

    public int getMaxLandSurface() {
        return maxLandSurface;
    }

    public void setMaxLandSurface(int maxLandSurface) {
        this.maxLandSurface = maxLandSurface;
    }

    public int getMinHouseSurface() {
        return minHouseSurface;
    }

    public void setMinHouseSurface(int minHouseSurface) {
        this.minHouseSurface = minHouseSurface;
    }

    public int getMaxHouseSurface() {
        return maxHouseSurface;
    }

    public void setMaxHouseSurface(int maxHouseSurface) {
        this.maxHouseSurface = maxHouseSurface;
    }
}
