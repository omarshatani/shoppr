package com.shoppr.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.shoppr.app.data.user.model.User;
import com.shoppr.app.ui.login.LoginViewModel;
import com.shoppr.app.ui.login.LoginViewModelFactory;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MainActivityViewModel viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
		LoginViewModel loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

		User currentUser = loginViewModel.getCurrentUser(this);
		viewModel.setUser(currentUser);

		viewModel.user().observe(this, user -> {
			if (user == null) {
				navigateToLoginFragment();
			} else {
				navigateToMapFragment();
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