package com.shoppr.app.data.listing.model;

import com.shoppr.app.data.request.model.Request;

import java.util.ArrayList;

public class Listing {

	private String id;
	private String name;
	private String description;
	private ListingType type;
	private String userId;
	private ArrayList<String> imageUrls;
	private double offer;
	private String currency;
	private double latitude;
	private double longitude;
	private ListingState state;
	private ArrayList<Request> requests;

	public Listing() {
	}

	public Listing(String name, String description, ListingType type, String userId, ArrayList<String> imageUrls, double offer, String currency, double latitude, double longitude, ListingState state, ArrayList<Request> requests) {
		this.name = name;
		this.description = description;
		this.type = type;
		this.userId = userId;
		this.imageUrls = imageUrls;
		this.offer = offer;
		this.currency = currency;
		this.latitude = latitude;
		this.longitude = longitude;
		this.state = state;
		this.requests = requests;
	}

	public Listing(String id, String name, String description, ListingType type, String userId, ArrayList<String> imageUrls, double offer, String currency, double latitude, double longitude, ListingState state, ArrayList<Request> requests) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.userId = userId;
		this.imageUrls = imageUrls;
		this.offer = offer;
		this.currency = currency;
		this.latitude = latitude;
		this.longitude = longitude;
		this.state = state;
		this.requests = requests;
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

	public ListingType getType() {
		return type;
	}

	public void setType(ListingType type) {
		this.type = type;
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

	public double getOffer() {
		return offer;
	}

	public void setOffer(double offer) {
		this.offer = offer;
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

	public ListingState getState() {
		return state;
	}

	public void setState(ListingState state) {
		this.state = state;
	}

	public ArrayList<Request> getRequests() {
		return requests;
	}

	public void setRequests(ArrayList<Request> requests) {
		this.requests = requests;
	}
}
