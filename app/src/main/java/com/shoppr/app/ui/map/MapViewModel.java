package com.shoppr.app.ui.map;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.map.MapRepository;

import java.util.ArrayList;

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

    public void addListings() {
        mapRepository.addListings(new Listing("Item", "Description", "anId", new ArrayList<>(), 10.2, "EUR"), result -> Log.d("ADDED", "YES"));
    }

    public void retrieveListings() {
        mapRepository.getListings(result -> {
            assert result != null;
            listings.postValue(result.getData());
        }, error -> {
        });
    }

}