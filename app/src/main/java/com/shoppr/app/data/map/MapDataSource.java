package com.shoppr.app.data.map;

import androidx.annotation.Nullable;

import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.listing.ListingDatabase;
import com.shoppr.app.data.listing.model.Listing;

import java.util.ArrayList;

public class MapDataSource {
    private final ListingDatabase database;

    public MapDataSource(ListingDatabase database) {
        this.database = database;
    }

    public void getListings(Callback<ArrayList<Listing>> successCallback, Callback<Exception> errorCallback) {
        database.getAll(new Callback<ArrayList<Listing>>() {
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

    public void addListing(Object data, Callback<Void> callback) {
        database.add(data).addOnSuccessListener(unused -> callback.onSuccess(null));
    }
}
