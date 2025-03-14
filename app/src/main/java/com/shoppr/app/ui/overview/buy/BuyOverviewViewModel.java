package com.shoppr.app.ui.overview.buy;

import androidx.lifecycle.ViewModel;

import com.shoppr.app.data.listing.model.ListingType;
import com.shoppr.app.domain.listing.PostListingUseCase;

public class BuyOverviewViewModel extends ViewModel {
	// TODO: Implement the ViewModel

    private PostListingUseCase postListingUseCase;

    public BuyOverviewViewModel(PostListingUseCase postListingUseCase) {
        this.postListingUseCase = postListingUseCase;
    }

    public void onPost(ListingType type, String title, String description, String price) {
        postListingUseCase.execute(type, title, description, price);
    }
}