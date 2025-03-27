package com.shoppr.app.domain.listing;

import com.shoppr.app.data.listing.ListingBuilder;
import com.shoppr.app.data.listing.ListingDatabase;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.listing.model.ListingState;
import com.shoppr.app.data.listing.model.ListingType;

import java.util.ArrayList;

public class PostListingUseCase {

    private final ListingDatabase listingDatabase;

    public PostListingUseCase(ListingDatabase listingDatabase) {
        this.listingDatabase = listingDatabase;
    }

    public void execute(String userId, ListingType type, String title, String description, String price, double latitude, double longitude) {
        Listing listing = new ListingBuilder.
                Builder()
                .userId(userId)
                .type(type)
                .name(title)
                .description(description)
                .offer(Double.parseDouble(price))
                .currency("EUR")
                .state(ListingState.NEW)
                .requests(new ArrayList<>())
                .latitude(latitude)
                .longitude(longitude)
                .build();
        listingDatabase.add(listing);
    }
}
