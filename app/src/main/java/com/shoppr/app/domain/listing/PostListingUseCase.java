package com.shoppr.app.domain.listing;

import com.shoppr.app.data.listing.ListingBuilder;
import com.shoppr.app.data.listing.ListingDatabase;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.listing.model.ListingState;
import com.shoppr.app.data.listing.model.ListingType;
import com.shoppr.app.domain.storage.CredentialStorage;

import java.util.ArrayList;

public class PostListingUseCase {

    private final ListingDatabase listingDatabase;

    public PostListingUseCase(ListingDatabase listingDatabase) {
        this.listingDatabase = listingDatabase;
		}

    public void execute(ListingType type, String title, String description, String price) {
        Listing listing = new ListingBuilder.
            Builder()
            .type(type)
            .name(title)
            .description(description)
            .offer(Double.parseDouble(price))
            .currency("EUR")
            .state(ListingState.NEW)
            .requests(new ArrayList<>())
            .build();
        listingDatabase.add(listing);
    }
}
