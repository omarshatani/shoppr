package com.shoppr.app.ui.checkout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.shoppr.app.R;
import com.shoppr.app.databinding.FragmentCheckoutBinding;

public class CheckoutFragment extends Fragment {

    private CheckoutViewModel viewModel;
    private FragmentCheckoutBinding binding;

    public static CheckoutFragment newInstance() {
        return new CheckoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCheckoutBinding.inflate(inflater, container, false);

        binding.checkoutToolbar.setNavigationIcon(R.drawable.ic_back);
        binding.checkoutToolbar.setNavigationOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_checkoutFragment_to_mapFragment));

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CheckoutViewModel.class);
    }
}