package com.shoppr.app.ui.overview.buy;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoppr.app.R;

public class BuyOverviewFragment extends Fragment {

	private BuyOverviewViewModel mViewModel;

	public static BuyOverviewFragment newInstance() {
		return new BuyOverviewFragment();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
													 @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_buy_overview, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mViewModel = new ViewModelProvider(this, new BuyOverviewViewModelFactory(requireActivity())).get(BuyOverviewViewModel.class);
	}
}