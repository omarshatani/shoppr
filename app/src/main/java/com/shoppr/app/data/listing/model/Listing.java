package com.shoppr.app.data.listing.model;

import java.util.ArrayList;

public class Listing {
    private String name;
    private String description;
    private String userId;
    private ArrayList<String> imageUrls;
    private double price;
    private String currency;

    public Listing() {
    }

    public Listing(String name, String description, String userId, ArrayList<String> imageUrls, double price, String currency) {
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.imageUrls = imageUrls;
        this.price = price;
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
