package com.shoppr.app.data.listing.model;

import com.shoppr.app.data.request.model.Request;
import com.shoppr.app.data.user.model.User;

import java.util.ArrayList;

public class ListingItem extends Listing {
	private User user;

	public ListingItem(String id, User user, String name, String description, ListingType type, String userId, ArrayList<String> imageUrls, double offer, String currency, double latitude, double longitude, ListingState state, ArrayList<Request> requests) {
		super(id, name, description, type, userId, imageUrls, offer, currency, latitude, longitude, state, requests);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


}
