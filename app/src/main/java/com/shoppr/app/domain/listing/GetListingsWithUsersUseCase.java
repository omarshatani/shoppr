package com.shoppr.app.domain.listing;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;
import com.shoppr.app.data.listing.ListingDatabase;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.listing.model.ListingItem;
import com.shoppr.app.data.user.UserDatabase;
import com.shoppr.app.data.user.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetListingsWithUsersUseCase {

	private final ListingDatabase listingDatabase;
	private final UserDatabase userDatabase;

	public GetListingsWithUsersUseCase(ListingDatabase listingDatabase, UserDatabase userDatabase) {
		this.listingDatabase = listingDatabase;
		this.userDatabase = userDatabase;
	}

	public Task<List<ListingItem>> execute() {
		// Get tasks for listings and users
		Task<QuerySnapshot> usersTask = userDatabase.getAll();
		Task<QuerySnapshot> listingsTask = listingDatabase.getAll();

		// Combine tasks and return a new task with combined data
		return Tasks.whenAll(listingsTask, usersTask)
				.continueWith(task -> {
					// Get results from tasks
					List<User> users = usersTask.getResult().getDocuments().stream().map(documentSnapshot -> {
						User user = documentSnapshot.toObject(User.class);
						if (user != null) {
							user.setUuid(documentSnapshot.getId());
						}
						return user;
					}).collect(Collectors.toList());

					List<Listing> listings = listingsTask.getResult().getDocuments().stream().map(documentSnapshot -> {
						Listing listing = documentSnapshot.toObject(Listing.class);
						if (listing != null) {
							listing.setId(documentSnapshot.getId());
						}
						return listing;
					}).collect(Collectors.toList());

					// Create a map of users for efficient lookup
					Map<String, User> userMap = users.stream()
							.collect(Collectors.toMap(User::getUuid, user -> user));

					// Create a list of ListingItem objects
					return listings.stream()
							.map(listing -> {
								// Get the corresponding user for the listing
								User user = userMap.get(listing.getUserId());
								// Create a ListingItem object with combined data
								return new ListingItem(listing.getId(), user, listing.getName(), listing.getDescription(), listing.getType(), listing.getUserId(), listing.getImageUrls(), listing.getOffer(), listing.getCurrency(), listing.getLatitude(), listing.getLongitude(), listing.getState(), listing.getRequests());
							})
							.collect(Collectors.toList());
				});
	}
}