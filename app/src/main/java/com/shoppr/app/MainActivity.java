package com.shoppr.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MainActivityViewModel viewModel = new ViewModelProvider(this, new MainActivityViewModelFactory(this)).get(MainActivityViewModel.class);
		viewModel.checkUserLoginStatus();
		viewModel.isLoggedIn().observe(this, isLogged -> {
			if (isLogged) {
				navigateToMapFragment();
			} else {
				navigateToLoginFragment();
			}
		});
	}

	private void navigateToLoginFragment() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		navController.navigate(R.id.action_main_to_login);
	}

	private void navigateToMapFragment() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		navController.navigate(R.id.action_main_to_map);
	}
}