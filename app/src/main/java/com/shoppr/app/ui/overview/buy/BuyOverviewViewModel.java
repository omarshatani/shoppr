package com.shoppr.app.ui.overview.buy;

import androidx.lifecycle.ViewModel;

import com.shoppr.app.data.listing.model.ListingType;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.domain.listing.PostListingUseCase;
import com.shoppr.app.domain.user.UserRepository;

public class BuyOverviewViewModel extends ViewModel {
	// TODO: Implement the ViewModel

    private final PostListingUseCase postListingUseCase;
    private final UserRepository userRepository;

    public BuyOverviewViewModel(PostListingUseCase postListingUseCase, UserRepository userRepository) {
        this.postListingUseCase = postListingUseCase;
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public void onPost(String userId, ListingType type, String title, String description, String price, Double latitude, Double longitude) {
        postListingUseCase.execute(userId, type, title, description, price, latitude, longitude);
    }
}