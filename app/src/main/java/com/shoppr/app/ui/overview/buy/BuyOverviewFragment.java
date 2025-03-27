package com.shoppr.app.ui.overview.buy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.shoppr.app.R;
import com.shoppr.app.data.listing.model.ListingType;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.databinding.FragmentBuyOverviewBinding;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BuyOverviewFragment extends Fragment {
	// Registers a photo picker activity launcher in multi-select mode.
	// In this example, the app lets the user select up to 5 media files.
	ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
			registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
				// Callback is invoked after the user selects media items or closes the
				// photo picker.
				if (!uris.isEmpty()) {
					Log.d("PhotoPicker", "Number of items selected: " + uris.size());
				} else {
					Log.d("PhotoPicker", "No media selected");
				}
			});

	private BuyOverviewViewModel viewModel;
	private FragmentBuyOverviewBinding binding;

	public static BuyOverviewFragment newInstance() {
		return new BuyOverviewFragment();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
													 @Nullable Bundle savedInstanceState) {
		binding = FragmentBuyOverviewBinding.inflate(inflater, container, false);
		binding.requestToolbar.setNavigationIcon(R.drawable.ic_back);
		binding.requestToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigateUp());

		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = new ViewModelProvider(this, new BuyOverviewViewModelFactory(requireActivity())).get(BuyOverviewViewModel.class);

		binding.selectPhotosCta.setOnClickListener(v -> {
			pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
					.setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
					.build());
		});

		User currentUser = viewModel.getCurrentUser();

		// Create the list of items
		List<String> items = Arrays.stream(ListingType.values()).map(ListingType::getLabel).collect(Collectors.toList());
		
		// Create the ArrayAdapter
		ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, items);

		// Get the EditText from TextInputLayout and set the adapter if it's an AutoCompleteTextView
		binding.buySellMenu.setAdapter(adapter);
		binding.buySellMenu.setText(items.get(0), false);

		binding.postCta.setOnClickListener((v) -> {
			ListingType type = binding.buySellMenu.getText().toString().equals("buy") ? ListingType.BUY : ListingType.SELL;
			String title = Objects.requireNonNull(binding.itemTitle.getText()).toString();
			String description = Objects.requireNonNull(binding.itemDescription.getText()).toString();
			String price = Objects.requireNonNull(binding.itemOffer.getText()).toString();

			Log.d("DESCRIPTION", description);
			Log.d("TITLE", title);
			Log.d("UUID", currentUser.getUuid());

			viewModel.onPost(currentUser.getUuid(), type, title, description, price, currentUser.getLatitude(), currentUser.getLongitude());
		});

	}
}