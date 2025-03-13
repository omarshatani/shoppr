package com.shoppr.app.ui.overview.buy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.shoppr.app.R;
import com.shoppr.app.databinding.FragmentBuyOverviewBinding;

import java.util.ArrayList;
import java.util.List;

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

		TextInputLayout textField = view.findViewById(R.id.buy_sell_menu); // Replace with your actual ID

		// Create the list of items
		List<String> items = new ArrayList<>();
		items.add("Item 1");
		items.add("Item 2");
		items.add("Item 3");
		items.add("Item 4");

		// Get the context
		Context context = requireContext();

		// Create the ArrayAdapter
		ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.list_item, items);

		// Get the EditText from TextInputLayout and set the adapter if it's an AutoCompleteTextView
		if (textField != null) {
			if (textField.getEditText() instanceof AutoCompleteTextView) {
				AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textField.getEditText();
				autoCompleteTextView.setAdapter(adapter);
			}
		}

	}
}