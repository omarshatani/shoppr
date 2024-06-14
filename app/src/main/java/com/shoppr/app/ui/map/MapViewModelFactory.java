package com.shoppr.app.ui.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.shoppr.app.data.listing.ListingDatabase;
import com.shoppr.app.data.map.MapDataSource;
import com.shoppr.app.data.map.MapRepository;
import com.shoppr.app.data.request.RequestDatabase;
import com.shoppr.app.data.user.UserDatabase;

public class MapViewModelFactory implements ViewModelProvider.Factory {
	@NonNull
	@Override
	@SuppressWarnings("unchecked")
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		if (modelClass.isAssignableFrom(MapViewModel.class)) {
			return (T) new MapViewModel(MapRepository.getInstance(new MapDataSource(ListingDatabase.getInstance(), UserDatabase.getInstance(), RequestDatabase.getInstance())));
		} else {
			throw new IllegalArgumentException("Unknown ViewModel class");
		}
	}
}
