package com.shoppr.app.data.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.firebase.AuthCredentialDeserializer;
import com.shoppr.app.data.login.adapter.UserAdapter;
import com.shoppr.app.data.user.UserDatabase;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.domain.login.model.LoginResult;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

	private static volatile LoginRepository instance;
	private final LoginDataSource dataSource;
	private final UserAdapter userAdapter;
	private final UserDatabase userDatabase;

	// If user credentials will be cached in local storage, it is recommended it be encrypted
	// @see https://developer.android.com/training/articles/keystore

	// private constructor : singleton access
	private LoginRepository(LoginDataSource dataSource, UserAdapter userAdapter, UserDatabase userDatabase) {
		this.dataSource = dataSource;
		this.userAdapter = userAdapter;
		this.userDatabase = userDatabase;
	}

	public static LoginRepository getInstance(LoginDataSource dataSource, UserAdapter userAdapter, UserDatabase userDatabase) {
		if (instance == null) {
			instance = new LoginRepository(dataSource, userAdapter, userDatabase);
		}
		return instance;
	}

	public void logout(Callback<Void> callback) {
		dataSource.logout();
		callback.onSuccess(null);
	}

	public void login(final String username, final String password, final Callback<User> signInCallback, Callback<LoginResult> signupCallback) {
		Task<AuthResult> loginTask = dataSource.login(username, password);
		loginTask
				.addOnSuccessListener(result -> {
					FirebaseUser firebaseUser = result.getUser();
					assert firebaseUser != null;
					if (firebaseUser.isEmailVerified()) {
						signInCallback.onSuccess(new Result.Success<>(userAdapter.adaptUserFromFirebaseUser(firebaseUser)));
					} else {
						signInCallback.onError(new Exception("Email not validated"));
					}
				})
				.addOnFailureListener(exception -> {
					if (exception instanceof FirebaseAuthException) {
						String errorCode = ((FirebaseAuthException) exception).getErrorCode();

						if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
							this.signup(username, password, signupCallback);
						}

						this.handleAuthenticationException(errorCode, signInCallback);
					}
				});

	}

	public void loginWithGoogle(Intent data, SharedPreferences preferences, Callback<User> callback) {
		try {
			Task<AuthResult> loginTask = dataSource.loginWithGoogle(data, preferences);
			loginTask.addOnCompleteListener(result -> {
				if (result.isSuccessful()) {
					FirebaseUser firebaseUser = result.getResult().getUser();
					assert firebaseUser != null;
					userDatabase.add(firebaseUser.getUid(), new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhoneNumber()));
					callback.onSuccess(new Result.Success<>(userAdapter.adaptUserFromFirebaseUser(firebaseUser)));
				} else {
					callback.onError(result.getException());
				}
			});
		} catch (ApiException exception) {
			Log.e("ERROR SOCIAL SIGN IN", "DAMN");
			callback.onError(exception);
		}
	}

	public User getCurrentUser(SharedPreferences preferences) {
		FirebaseUser firebaseUser = dataSource.getUser();
		Gson gson = new GsonBuilder().registerTypeAdapter(AuthCredential.class, new AuthCredentialDeserializer()).create();

		if (firebaseUser == null) {
			return null;
		}

		AuthCredential credentials = gson.fromJson(preferences.getString("credentials", null), AuthCredential.class);
		if (credentials != null) {
			firebaseUser.reauthenticate(credentials);
		}

		return userAdapter.adaptUserFromFirebaseUser(firebaseUser);
	}

	private void signup(String username, String password, Callback<LoginResult> callback) {
		Task<AuthResult> signupTask = dataSource.signup(username, password);

		signupTask.addOnCompleteListener(result -> {
			if (result.isSuccessful()) {
				FirebaseUser firebaseUser = result.getResult().getUser();
				if (firebaseUser != null) {
					firebaseUser.sendEmailVerification();
					userDatabase.add(firebaseUser.getUid(), new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), firebaseUser.getPhoneNumber()));
					callback.onComplete(new Result.Success<>(new LoginResult("An email verification has been sent")));
				}
			} else {
				callback.onComplete(new Result.Error<>(result.getException()));
			}
		});
	}

	private void handleAuthenticationException(String errorCode, Callback<User> callback) {
		switch (errorCode) {
			case "ERROR_INVALID_CREDENTIAL":
				// Invalid password
				callback.onError(new Exception("Wrong credentials."));
				// Show an error message to the user
				break;
			case "ERROR_NETWORK_REQUEST_FAILED":
				// Network error
				Log.e("SignIn", "Network error occurred.");
				// Provide instructions to retry when network is available
				break;
			default:
				// Handle other unexpected exceptions
				Log.e("SignIn", "An error occurred: " + errorCode);
				break;
		}
	}

}