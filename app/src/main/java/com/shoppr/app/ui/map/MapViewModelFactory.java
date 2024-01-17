package com.shoppr.app.ui.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.firestore.FirebaseFirestore;
import com.shoppr.app.data.map.MapDataSource;
import com.shoppr.app.data.map.MapRepository;
import com.shoppr.app.ui.login.LoginViewModel;

public class MapViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new MapViewModel(MapRepository.getInstance(new MapDataSource(FirebaseFirestore.getInstance())));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
