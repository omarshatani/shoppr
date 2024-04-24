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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.shoppr.app.MainActivityViewModel;
import com.shoppr.app.R;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.map.ClusterMarkerItem;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.databinding.FragmentMapBinding;
import com.shoppr.app.domain.utils.DeviceUtils;
import com.shoppr.app.domain.utils.ImageAdapter;
import com.shoppr.app.domain.utils.JsonUtils;
import com.shoppr.app.ui.carousel.ImageViewActivity;
import com.shoppr.app.ui.login.LoginViewModel;
import com.shoppr.app.ui.login.LoginViewModelFactory;
import com.shoppr.app.ui.user.UserViewModel;
import com.shoppr.app.ui.user.UserViewModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment {

    private final String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private MapViewModel mapViewModel;
    private LoginViewModel loginViewModel;
    private MainActivityViewModel mainActivityViewModel;
    private UserViewModel userViewModel;
    private LocationManager locationManager;
    private ClusterManager<ClusterMarkerItem> clusterManager;
    private GoogleMap map;
    private FragmentMapBinding binding;
    SupportMapFragment mapFragment;
    BottomSheetBehavior<LinearLayout> dialog;
    RecyclerView recyclerView;
    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map, mapFragment)
                .commit();

        mapFragment.getMapAsync(googleMap -> mapViewModel.setMap(googleMap));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapViewModel = new ViewModelProvider(this, new MapViewModelFactory())
                .get(MapViewModel.class);
        loginViewModel = new ViewModelProvider(requireActivity(), new LoginViewModelFactory()).get(LoginViewModel.class);
        userViewModel = new ViewModelProvider(requireActivity(), new UserViewModelFactory()).get(UserViewModel.class);
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        final Button logoutCta = binding.logoutCta;
        final int screenHeight = DeviceUtils.getScreenHeight(requireContext());

        dialog = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet_dialog));
        dialog.setState(STATE_HIDDEN);
        dialog.setHideable(true);
        dialog.setDraggable(true);
        dialog.setMaxHeight((int) (screenHeight / 1.8));
        dialog.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

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
                populateMarkerClusters(listings);
            }
        });

        logoutCta.setOnClickListener(v -> {
            loginViewModel.logout();
            mainActivityViewModel.setUser(null);
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mapViewModel.getHasInitialised()) {
            initialiseMap();
            mapViewModel.retrieveListings();
        }
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

        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        Location userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (userLocation != null) {
            LatLng currentUserLocation = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            User currentUser = loginViewModel.getCurrentUser();
            Map<String, Object> geoMap = new HashMap<>();
            geoMap.put("latitude", currentUserLocation.latitude);
            geoMap.put("longitude", currentUserLocation.longitude);
            userViewModel.updateUser(geoMap, currentUser.getUuid());
            setUpClusterer(currentUserLocation);
        }

        this.mapViewModel.setHasInitialised(true);
    }

    @SuppressLint("PotentialBehaviorOverride")
    private void setUpClusterer(LatLng userLocation) {
        // Position the map.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12f));

        // Initialize the manager with the context and the map.
        clusterManager = new ClusterManager<>(requireContext(), map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
    }

    @SuppressLint("MissingPermission")
    private void populateMarkerClusters(ArrayList<Listing> listings) {
        Location userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (userLocation == null) {
            return;
        }

        GoogleMap googleMap = mapViewModel.getMap().getValue();

        if (googleMap == null) {
            return;
        }

        for (Listing listing : listings) {
            ClusterMarkerItem offsetItem = new ClusterMarkerItem(listing.getLatitude(), listing.getLongitude(), listing.getName(), listing.getDescription());
            clusterManager.addItem(offsetItem);
            clusterManager.setOnClusterItemClickListener(item -> {
                dialog.setState(BottomSheetBehavior.STATE_EXPANDED);

                Listing currentListing = listings.stream().filter(element -> {
                    assert item.getTitle() != null;
                    return item.getTitle().equals(element.getName());
                }).findFirst().orElse(null);

                assert currentListing != null;

                ArrayList<String> arrayList = new ArrayList<>(currentListing.getImageUrls());
                ImageAdapter adapter = new ImageAdapter(requireContext(), arrayList);
                adapter.setOnItemClickListener((imageView, path) -> startActivity(new Intent(requireActivity(), ImageViewActivity.class).putExtra("image", path),
                        ActivityOptions.makeSceneTransitionAnimation(requireActivity(), imageView, "image").toBundle()));
                recyclerView.setAdapter(adapter);

                TextView title = binding.getRoot().findViewById(R.id.title);
                TextView description = binding.getRoot().findViewById(R.id.description);
                TextView price = binding.getRoot().findViewById(R.id.price);
                Button buyNowCta = binding.getRoot().findViewById(R.id.buyNow);
                title.setText(currentListing.getName());
                description.setText(currentListing.getDescription());
                String formattedPrice = String.format(getResources().getString(R.string.price_format), currentListing.getCurrency(), currentListing.getPrice());
                price.setText(formattedPrice);

                buyNowCta.setOnClickListener(v -> {
                    dialog.setState(STATE_HIDDEN);
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_mapFragment_to_checkoutFragment);
                });

                return true;
            });
        }
    }

}