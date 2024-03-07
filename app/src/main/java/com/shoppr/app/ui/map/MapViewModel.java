package com.shoppr.app.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.shoppr.app.data.map.MapRepository;

public class MapViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<GoogleMap> map = new MutableLiveData<>(null);
    private boolean hasInitialised = false;
    private MapRepository mapRepository;


    MapViewModel(MapRepository repository) {
        this.mapRepository = repository;
    }

    public LiveData<GoogleMap> getMap() {
        return map;
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

}