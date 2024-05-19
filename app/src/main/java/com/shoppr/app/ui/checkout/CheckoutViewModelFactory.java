package com.shoppr.app.ui.checkout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.shoppr.app.data.transaction.TransactionDatabase;
import com.shoppr.app.data.transaction.TransactionRepository;

public class CheckoutViewModelFactory implements ViewModelProvider.Factory {
	@NonNull
	@Override
	@SuppressWarnings("unchecked")
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		if (modelClass.isAssignableFrom(CheckoutViewModel.class)) {
			return (T) new CheckoutViewModel(TransactionRepository.getInstance(TransactionDatabase.getInstance()));
		} else {
			throw new IllegalArgumentException("Unknown ViewModel class");
		}
	}
}
