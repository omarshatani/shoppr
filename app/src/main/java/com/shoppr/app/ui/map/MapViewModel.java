package com.shoppr.app.ui.map;

import android.database.Observable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.shoppr.app.data.common.Resource;
import com.shoppr.app.data.map.MapRepository;

public class MapViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<GoogleMap> map = new MutableLiveData<>(null);
    private final MutableLiveData<Boolean> permission = new MutableLiveData<>(false);
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

    public LiveData<Boolean> getPermission() {
        return permission;
    }

    public void setPermission(boolean value) {
        permission.postValue(value);
    }

}