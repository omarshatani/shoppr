package com.shoppr.app.data.listing;

import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.listing.model.ListingState;
import com.shoppr.app.data.listing.model.ListingType;
import com.shoppr.app.data.request.model.Request;

import java.util.ArrayList;

public class ListingBuilder {

    private ListingBuilder() {
    }

    public static class Builder {
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

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(ListingType type) {
            this.type = type;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder imageUrls(ArrayList<String> imageUrls) {
            this.imageUrls = imageUrls;
            return this;
        }

        public Builder offer(double offer) {
            this.offer = offer;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder state(ListingState state) {
            this.state = state;
            return this;
        }

        public Builder requests(ArrayList<Request> requests) {
            this.requests = requests;
            return this;
        }

        public Listing build() {
            Listing listing = new Listing();

            listing.setId(this.id);
            listing.setName(this.name);
            listing.setDescription(this.description);
            listing.setType(this.type);
            listing.setUserId(this.userId);
            listing.setImageUrls(this.imageUrls);
            listing.setOffer(this.offer);
            listing.setCurrency(this.currency);
            listing.setLatitude(this.latitude);
            listing.setLongitude(this.longitude);
            listing.setState(this.state);
            listing.setRequests(this.requests);

            return listing;
        }

    }

}
