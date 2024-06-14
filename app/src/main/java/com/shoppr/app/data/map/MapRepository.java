package com.shoppr.app.data.map;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.listing.model.ListingItem;

import java.util.ArrayList;
import java.util.List;

public class MapRepository {

    private static volatile MapRepository instance;

    private final MapDataSource dataSource;

    private MapRepository(MapDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static MapRepository getInstance(MapDataSource dataSource) {
        if (instance == null) {
            instance = new MapRepository(dataSource);
        }
        return instance;
    }

    public Task<List<ListingItem>> getListings() {
        return dataSource.getListings();
    }

    public void getListings(Callback<ArrayList<Listing>> successCallback, Callback<Exception> errorCallback) {
        dataSource.getListings(successCallback, errorCallback);
    }

    public Task<DocumentReference> addListing(Object data) {
        return dataSource.addListing(data);
    }


    public void addListing(Object data, Callback<Void> callback) {
        dataSource.addListing(data, result -> callback.onSuccess(null));
    }

    public Task<DocumentReference> addRequest(Object data, String listingId) {
        return dataSource.addRequest(data, listingId);
    }

}
