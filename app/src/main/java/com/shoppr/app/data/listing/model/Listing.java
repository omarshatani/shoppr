package com.shoppr.app.data.listing.model;

import java.util.ArrayList;

public class Listing {
	private String id;
    private String name;
    private String description;
    private String category;
    private String userId;
    private ArrayList<String> imageUrls;
    private double price;
    private String currency;
    private double latitude;
    private double longitude;

	public Listing() {
	}

    public Listing(String name, String description, String category, String userId, ArrayList<String> imageUrls, double price, String currency, double latitude, double longitude) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.userId = userId;
        this.imageUrls = imageUrls;
        this.price = price;
        this.currency = currency;
        this.latitude = latitude;
        this.longitude = longitude;
    }

	public Listing(String id, String name, String description, String category, String userId, ArrayList<String> imageUrls, double price, String currency, double latitude, double longitude) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.category = category;
		this.userId = userId;
		this.imageUrls = imageUrls;
		this.price = price;
		this.currency = currency;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
