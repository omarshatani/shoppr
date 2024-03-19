package com.shoppr.app.data.map;

import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.listing.model.Listing;

import java.util.ArrayList;

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

    public void getListings(Callback<ArrayList<Listing>> successCallback, Callback<Exception> errorCallback) {
        dataSource.getListings(successCallback, errorCallback);
    }

    public void addListings(Object data, Callback<Void> callback) {
        dataSource.addListings(data, result -> callback.onSuccess(null));
    }

}
