package com.shoppr.app.data.map;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.listing.ListingDatabase;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.listing.model.ListingItem;
import com.shoppr.app.data.request.RequestDatabase;
import com.shoppr.app.data.user.UserDatabase;
import com.shoppr.app.domain.listing.GetListingsWithUsersUseCase;

import java.util.ArrayList;
import java.util.List;

public class MapDataSource {
	private final ListingDatabase listingDatabase;
	private final UserDatabase userDatabase;
	private final RequestDatabase requestDatabase;

	public MapDataSource(ListingDatabase listingDatabase, UserDatabase userDatabase, RequestDatabase requestDatabase) {
		this.listingDatabase = listingDatabase;
		this.userDatabase = userDatabase;
		this.requestDatabase = requestDatabase;
	}

	public Task<List<ListingItem>> getListings() {
		GetListingsWithUsersUseCase useCase = new GetListingsWithUsersUseCase(listingDatabase, userDatabase);
		return useCase.execute();
	}

	public void getListings(Callback<ArrayList<Listing>> successCallback, Callback<Exception> errorCallback) {
		listingDatabase.getAll(new Callback<ArrayList<Listing>>() {
			@Override
			public void onSuccess(@Nullable Result.Success<ArrayList<Listing>> result) {
				successCallback.onSuccess(result);
			}

			@Override
			public void onError(@Nullable Exception exception) {
				errorCallback.onError(exception);
			}
		});
	}

	public Task<DocumentReference> addListing(Object data) {
		return listingDatabase.add(data);
	}

	public void addListing(Object data, Callback<Void> callback) {
		listingDatabase.add(data).addOnSuccessListener(unused -> callback.onSuccess(null));
	}

	public Task<DocumentReference> addRequest(Object data, String listingId) {
		Task<DocumentReference> requestAddTask = requestDatabase.add(data);

		requestAddTask.addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				listingDatabase.updateArrayField(listingId, "requests", data);
			}
		});

		return requestAddTask;
	}

}
