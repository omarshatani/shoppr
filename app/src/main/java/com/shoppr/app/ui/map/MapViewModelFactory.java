package com.shoppr.app.ui.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.shoppr.app.data.database.Database;
import com.shoppr.app.data.firebase.FirestoreDatasource;
import com.shoppr.app.data.map.MapDataSource;
import com.shoppr.app.data.map.MapRepository;

public class MapViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(MapRepository.getInstance(new MapDataSource(Database.getInstance(new FirestoreDatasource()))));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
