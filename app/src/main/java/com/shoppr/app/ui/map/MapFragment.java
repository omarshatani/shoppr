package com.shoppr.app.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.shoppr.app.MainActivityViewModel;
import com.shoppr.app.R;
import com.shoppr.app.data.listing.model.Listing;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.databinding.FragmentMapBinding;
import com.shoppr.app.ui.login.LoginViewModel;
import com.shoppr.app.ui.login.LoginViewModelFactory;
import com.shoppr.app.ui.user.UserViewModel;
import com.shoppr.app.ui.user.UserViewModelFactory;

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
    private FragmentMapBinding binding;
    SupportMapFragment mapFragment;

    ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts
                            .RequestMultiplePermissions(), result -> {
                    }
            );

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

        mapViewModel.getMap().observe(getViewLifecycleOwner(), googleMap -> {
            if (googleMap != null) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationPermissionRequest.launch(permissions);
                } else {
                    initialiseMap();
                }
            }
        });
        mapViewModel.getListings().observe(getViewLifecycleOwner(), listings -> {
            if (listings == null) {
                return;
            }
            for (Listing listing : listings) {
                Log.d("LISTING", listing.toString());
            }
        });

        logoutCta.setOnClickListener(v -> {
            loginViewModel.logout();
            mainActivityViewModel.setUser(null);
        });

//        mapViewModel.addListings();
//        mapViewModel.retrieveListings();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mapViewModel.getHasInitialised()) {
            initialiseMap();
        }
    }

    private void initialiseMap() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        GoogleMap googleMap = mapViewModel.getMap().getValue();

        if (googleMap == null) {
            return;
        }

        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            User currentUser = loginViewModel.getCurrentUser();
            Map<String, Object> map = new HashMap<>();
            map.put("latitude", userLocation.latitude);
            map.put("longitude", userLocation.longitude);
            userViewModel.updateUser(map, currentUser.getUuid());
        }

        this.mapViewModel.setHasInitialised(true);
    }


}