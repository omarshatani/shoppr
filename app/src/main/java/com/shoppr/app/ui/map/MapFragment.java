package com.shoppr.app.ui.map;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.clustering.ClusterManager;
import com.shoppr.app.R;
import com.shoppr.app.data.listing.model.ListingItem;
import com.shoppr.app.data.listing.model.ListingType;
import com.shoppr.app.data.request.model.Request;
import com.shoppr.app.data.request.model.RequestState;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.databinding.FragmentMapBinding;
import com.shoppr.app.domain.map.ClusterMarkerItem;
import com.shoppr.app.domain.map.CustomClusterRenderer;
import com.shoppr.app.domain.utils.DeviceUtils;
import com.shoppr.app.domain.utils.ImageAdapter;
import com.shoppr.app.domain.utils.JsonUtils;
import com.shoppr.app.ui.carousel.ImageViewActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment {

	private final String[] permissions = new String[]{
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
	};
	Location userLocation;
	SupportMapFragment mapFragment;
	BottomSheetBehavior<LinearLayout> dialog;
	RecyclerView recyclerView;
	ActivityResultLauncher<String[]> locationPermissionRequest =
			registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
			});
	private MapViewModel mapViewModel;
	private ClusterManager<ClusterMarkerItem> clusterManager;
	private GoogleMap map;
	private FragmentMapBinding binding;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
													 @Nullable Bundle savedInstanceState) {
		binding = FragmentMapBinding.inflate(inflater, container, false);
		mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

		assert mapFragment != null;

		if (!mapFragment.isAdded()) {
			requireActivity().getSupportFragmentManager()
					.beginTransaction()
					.add(R.id.map, mapFragment)
					.commit();
		}

		mapFragment.getMapAsync(googleMap -> mapViewModel.setMap(googleMap));

		return binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mapViewModel = new ViewModelProvider(this, new MapViewModelFactory(requireActivity()))
				.get(MapViewModel.class);
		final Button logoutCta = binding.logoutCta;

		final int screenHeight = DeviceUtils.getScreenHeight(requireContext());

		dialog = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet_dialog));
		dialog.setState(STATE_HIDDEN);
		dialog.setHideable(true);
		dialog.setDraggable(true);
		dialog.setMaxHeight((int) (screenHeight / 1.5));
		dialog.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(@NonNull View view, int i) {
				if (i == BottomSheetBehavior.STATE_COLLAPSED || i == BottomSheetBehavior.STATE_HIDDEN) {
					binding.logoutCta.setVisibility(View.VISIBLE);
					binding.actionsCtaContainer.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onSlide(@NonNull View view, float v) {

			}
		});

		recyclerView = view.findViewById(R.id.recycler);

		mapViewModel.getMap().observe(getViewLifecycleOwner(), googleMap -> {
			if (googleMap != null) {
				this.map = googleMap;
				if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					locationPermissionRequest.launch(permissions);
				} else {
					initialiseMap();
					mapViewModel.retrieveListings();
				}
			}
		});

		mapViewModel.getListings().observe(getViewLifecycleOwner(), listings -> {
			if (listings == null) {
				return;
			}
			if (listings.isEmpty()) {
				mapViewModel.addListings(JsonUtils.convertJsonMockToListings(requireActivity()));
			} else {
//				this.listings = listings;
				if (this.map != null) {
					populateMarkerClusters(listings);
				}
			}
		});

		logoutCta.setOnClickListener(v -> {
			mapViewModel.logout();
			navigateToLoginFragment();
		});

	}

	private void navigateToLoginFragment() {
		NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
		navController.navigate(R.id.action_main_to_login);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!mapViewModel.getHasInitialised()) {
			initialiseMap();
		}
		mapViewModel.retrieveListings();
	}

	private void initialiseMap() {
		if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}

		if (map == null) {
			return;
		}

		map.setMyLocationEnabled(true);
		map.getUiSettings().setMyLocationButtonEnabled(true);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.getUiSettings().setZoomGesturesEnabled(true);

		LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
		userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (userLocation == null) {
			return;
		}

		LatLng currentUserLocation = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserLocation, 18f));

		this.mapViewModel.setHasInitialised(true);
	}

	private void populateMarkerClusters(List<ListingItem> items) {
		Map<String, ListingItem> listingsMap = new HashMap<>();

		for (ListingItem listingItem : items) {
			listingsMap.put(listingItem.getId(), listingItem);
		}

		if (clusterManager == null) {
			setUpClusterer();
		}

		if (items.isEmpty()) {
			return;
		}

		items.forEach(listing -> {
			ClusterMarkerItem offsetItem = new ClusterMarkerItem(
					listing.getId(),
					listing.getLatitude(),
					listing.getLongitude(),
					listing.getName(),
					listing.getDescription(),
					listing.getType()
			);
			clusterManager.addItem(offsetItem);
		});

		clusterManager.setOnClusterItemClickListener(item -> {
			ListingItem currentListingItem = listingsMap.get(item.getId());

			if (currentListingItem == null) {
				return false;
			}

			User listingUser = currentListingItem.getUser();

			map.animateCamera(CameraUpdateFactory.newLatLngZoom(item.getPosition(), 18f));
			dialog.setState(BottomSheetBehavior.STATE_EXPANDED);
			binding.logoutCta.setVisibility(View.INVISIBLE);
			binding.actionsCtaContainer.setVisibility(View.INVISIBLE);

			ArrayList<String> arrayList = new ArrayList<>(currentListingItem.getImageUrls());
			ImageAdapter adapter = new ImageAdapter(requireContext(), arrayList);
			adapter.setOnItemClickListener((imageView, path) -> startActivity(new Intent(requireActivity(), ImageViewActivity.class).putExtra("image", path),
					ActivityOptions.makeSceneTransitionAnimation(requireActivity(), imageView, "image").toBundle()));
			recyclerView.setAdapter(adapter);

			TextView title = binding.getRoot().findViewById(R.id.title);
			TextView description = binding.getRoot().findViewById(R.id.description);
			TextView price = binding.getRoot().findViewById(R.id.price);
			TextView userName = binding.getRoot().findViewById(R.id.usernameText);
//			TextView userAddress = binding.getRoot().findViewById(R.id.userAddress);
			Button buyOrCellCta = binding.getRoot().findViewById(R.id.buyOrSellCta);
			CardView buyOrSellChip = binding.getRoot().findViewById(R.id.buyOrSellChip);
			TextView buyOrSellText = binding.getRoot().findViewById(R.id.buyOrSellChipText);

			buyOrCellCta.setText(currentListingItem.getType() == ListingType.BUY ? R.string.buy : R.string.sell);
			title.setText(currentListingItem.getName());
			description.setText(currentListingItem.getDescription());
			userName.setText(listingUser.getName());

			if (currentListingItem.getType() == ListingType.BUY) {
				buyOrSellText.setText(R.string.wants_to_sell);
				buyOrSellChip.setCardBackgroundColor(getResources().getColor(R.color.light_blue_600, null));
			} else {
				buyOrSellText.setText(R.string.wants_to_buy);
				buyOrSellChip.setCardBackgroundColor(getResources().getColor(R.color.light_blue_900, null));
			}

//			userAddress.setText(listingUser.getAddress() != null ? listingUser.getAddress() : "");

			if (currentListingItem.getType() == ListingType.BUY) {
				price.setVisibility(View.VISIBLE);
				String formattedPrice = String.format(getResources().getString(R.string.price_format), currentListingItem.getCurrency(), currentListingItem.getOffer());
				price.setText(formattedPrice);
			} else {
				price.setVisibility(View.GONE);
			}

			buyOrCellCta.setOnClickListener(v -> {
				dialog.setState(STATE_HIDDEN);
//				NavController navController = Navigation.findNavController(v);
//
//				Bundle bundle = new Bundle();
//				bundle.putString("itemName", currentListingItem.getName());
//				bundle.putString("username", listingUser.getName());
//				bundle.putString("price", String.valueOf(currentListingItem.getOffer()));
//				bundle.putString("currency", currentListingItem.getCurrency());
//				bundle.putString("sellerId", currentListingItem.getUserId());
//				bundle.putString("listingId", currentListingItem.getId());
//
//				navController.navigate(R.id.action_mapFragment_to_checkoutFragment, bundle);

				Request request = new Request();
				String uuid = mapViewModel.getCurrentUser().getUuid();

				if (currentListingItem.getType() == ListingType.BUY) {
					request.setBuyerId(currentListingItem.getUserId());
					request.setSellerId(uuid);
				} else {
					request.setBuyerId(uuid);
					request.setSellerId(currentListingItem.getUserId());
				}

				request.setOffer(currentListingItem.getOffer());
				request.setCreationDate(new Date());
				request.setState(RequestState.PENDING);

				mapViewModel.addRequest(request, currentListingItem.getId());

				Toast.makeText(requireContext(), "Your requested has been created", Toast.LENGTH_SHORT).show();
			});

			return true;
		});
	}

	@SuppressLint("PotentialBehaviorOverride")
	private void setUpClusterer() {
		// Initialize the manager with the context and the map.
		clusterManager = new ClusterManager<>(requireContext(), map);
		CustomClusterRenderer renderer = new CustomClusterRenderer(requireContext(), map, clusterManager);
		clusterManager.setRenderer(renderer);
		// Point the map's listeners at the listeners implemented by the cluster manager.
		map.setOnCameraIdleListener(clusterManager);
		map.setOnMarkerClickListener(clusterManager);
	}

}