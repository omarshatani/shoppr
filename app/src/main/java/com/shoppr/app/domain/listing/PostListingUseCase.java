package com.shoppr.app.domain.listing;

import com.shoppr.app.data.listing.ListingBuilder;
import com.shoppr.app.data.listing.ListingDatabase;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.listing.model.ListingType;

public class PostListingUseCase {

    private final ListingDatabase listingDatabase;

    public PostListingUseCase(ListingDatabase listingDatabase) {
        this.listingDatabase = listingDatabase;
    }

    public void execute(ListingType type, String title, String description, String price) {
        Listing listing = new ListingBuilder.Builder().type(type).name(title).description(description).offer(Double.parseDouble(price)).build();
        listingDatabase.add(listing);
    }
}
