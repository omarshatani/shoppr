package com.shoppr.app.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.map.MapRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MapViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private static final double MAX_RADIUS = 100; // meters
    private final MutableLiveData<GoogleMap> map = new MutableLiveData<>(null);
    private final MutableLiveData<ArrayList<Listing>> listings = new MutableLiveData<>(null);
    private final MutableLiveData<ArrayList<MarkerOptions>> markers = new MutableLiveData<>(null);
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

    public void addListings(ArrayList<Listing> listingsArray) {
        GoogleMap googleMap = map.getValue();
        for (Listing listing : listingsArray) {
            Map<String, Object> listingsData = new HashMap<>();
            listingsData.put("name", listing.getName());
            listingsData.put("description", listing.getDescription());
            listingsData.put("userId", listing.getUserId());
            listingsData.put("imageUrls", listing.getImageUrls());
            listingsData.put("price", listing.getPrice());
            listingsData.put("currency", listing.getCurrency());

            if (googleMap != null) {
                LatLng randomCoordinate = generateRandomPointNearCoordinates(googleMap.getProjection().getVisibleRegion().latLngBounds);
                listingsData.put("latitude", randomCoordinate.latitude);
                listingsData.put("longitude", randomCoordinate.longitude);
            }

            this.addListing(listingsData, unused -> mapRepository.getListings(result -> {
                assert result != null;
                listings.postValue(result.getData());
            }, error -> {
            }));
        }
    }

    private void addListing(Object data, Callback<Void> callback) {
        mapRepository.addListing(data, callback);
    }

    public void retrieveListings() {
        if (listings.getValue() == null || listings.getValue().isEmpty()) {
            mapRepository.getListings(result -> {
                assert result != null;
                listings.postValue(result.getData());
            }, error -> {
            });
        }
    }

    public LatLng generateRandomPointNearCoordinates(LatLngBounds visibleRegion) {
        Random random = new Random();

        double north = visibleRegion.northeast.latitude;
        double south = visibleRegion.southwest.latitude;
        double east = visibleRegion.northeast.longitude;
        double west = visibleRegion.southwest.longitude;

        // Generate a random offset within the radius
        double randomLat = south + (north - south) * random.nextDouble();
        double randomLng = west + (east - west) * random.nextDouble();
        double randomRadius = MAX_RADIUS * Math.sqrt(random.nextDouble());
        double randomAngle = random.nextDouble() * 2 * Math.PI;

        double lat = randomLat + randomRadius * Math.cos(randomAngle) / 111111;
        double lng = randomLng + randomRadius * Math.sin(randomAngle) / (111111 * Math.cos(randomLat));

        return new LatLng(lat, lng);
    }

}