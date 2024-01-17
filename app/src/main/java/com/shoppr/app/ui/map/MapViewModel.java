package com.shoppr.app.ui.map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoppr.app.data.map.MapRepository;

public class MapViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MapRepository repository;

    MapViewModel(MapRepository repository) {
        this.repository = repository;
    }

}