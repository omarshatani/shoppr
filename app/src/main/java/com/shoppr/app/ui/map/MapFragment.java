package com.shoppr.app.ui.map;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoppr.app.R;
import com.shoppr.app.databinding.FragmentLoginBinding;
import com.shoppr.app.databinding.FragmentMapBinding;
import com.shoppr.app.ui.login.LoginViewModel;
import com.shoppr.app.ui.login.LoginViewModelFactory;

public class MapFragment extends Fragment {

    private MapViewModel mapViewModel;
    private FragmentMapBinding binding;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapViewModel = new ViewModelProvider(this, new MapViewModelFactory())
                .get(MapViewModel.class);
        // TODO: Use the ViewModel
    }

}