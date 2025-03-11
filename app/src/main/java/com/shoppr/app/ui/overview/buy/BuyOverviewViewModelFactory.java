package com.shoppr.app.ui.overview.buy;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BuyOverviewViewModelFactory implements ViewModelProvider.Factory {
	private final Context context;

	public BuyOverviewViewModelFactory(Context context) {
		this.context = context;
	}

	@NonNull
	@SuppressWarnings("unchecked")
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		if (modelClass.isAssignableFrom(BuyOverviewViewModel.class)) {
			return (T) new BuyOverviewViewModel();
		} else {
			throw new IllegalArgumentException("Unknown ViewModel class");
		}
	}
}
