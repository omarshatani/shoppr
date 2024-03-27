package com.shoppr.app.ui.map;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.map.MapRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<GoogleMap> map = new MutableLiveData<>(null);
    private final MutableLiveData<ArrayList<Listing>> listings = new MutableLiveData<>(null);
    private boolean hasInitialised = false;
    private final MapRepository mapRepository;

    MapViewModel(MapRepository repository) {
        this.mapRepository = repository;
    }

    public LiveData<GoogleMap> getMap() {
        return map;
    }

    public LiveData<ArrayList<Listing>> getListings() {
        return listings;
    }

    public void setMap(GoogleMap googleMap) {
        map.postValue(googleMap);
    }

    public boolean getHasInitialised() {
        return hasInitialised;
    }

    public void setHasInitialised(boolean value) {
        this.hasInitialised = value;
    }

    public void addListings(ArrayList<Listing> listings) {
        for (Listing listing : listings) {
            Map<String, Object> listingsData = new HashMap<>();
            listingsData.put("name", listing.getName());
            listingsData.put("description", listing.getDescription());
            listingsData.put("userId", listing.getUserId());
            listingsData.put("imageUrls", listing.getImageUrls());
            listingsData.put("price", listing.getPrice());
            listingsData.put("currency", listing.getCurrency());

            this.addListing(listingsData, unused -> {
                Log.d("SUCCESS", "ADD");
            });
        }
    }

    private void addListing(Object data, Callback<Void> callback) {
        mapRepository.addListing(data, callback);
    }

    public void retrieveListings() {
        mapRepository.getListings(result -> {
            assert result != null;
            listings.postValue(result.getData());
        }, error -> {
        });
    }

}