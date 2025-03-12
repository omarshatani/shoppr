package com.shoppr.app.ui.overview.buy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.shoppr.app.databinding.FragmentBuyOverviewBinding;

public class BuyOverviewFragment extends Fragment {

	private BuyOverviewViewModel mViewModel;
	private FragmentBuyOverviewBinding binding;

	public static BuyOverviewFragment newInstance() {
		return new BuyOverviewFragment();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
													 @Nullable Bundle savedInstanceState) {
		binding = FragmentBuyOverviewBinding.inflate(inflater, container, false);
		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mViewModel = new ViewModelProvider(this, new BuyOverviewViewModelFactory(requireActivity())).get(BuyOverviewViewModel.class);

		ImageView arrowDown = binding.buySellArrowDown;

	}
}