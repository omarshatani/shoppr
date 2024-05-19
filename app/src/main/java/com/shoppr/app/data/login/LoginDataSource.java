package com.shoppr.app.data.login;

import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private final FirebaseAuth firebaseService;

    public LoginDataSource(FirebaseAuth firebaseService) {
        this.firebaseService = firebaseService;
    }

    public Task<AuthResult> login(String username, String password) {
        return firebaseService.signInWithEmailAndPassword(username, password);
    }

	public Task<AuthResult> loginWithGoogle(Intent data, SharedPreferences preferences) throws ApiException {
        GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
		AuthCredential credentials = GoogleAuthProvider.getCredential(account.getIdToken(), null);

		this.saveCredentials(account, preferences);

		return firebaseService.signInWithCredential(credentials);
    }

    public Task<AuthResult> signup(String username, String password) {
        return firebaseService.createUserWithEmailAndPassword(username, password);
    }

    public void logout() {
        firebaseService.signOut();
    }

    public FirebaseUser getUser() {
        return firebaseService.getCurrentUser();
    }

	private void saveCredentials(GoogleSignInAccount account, SharedPreferences preferences) {
		Gson gson = new GsonBuilder().create();
		HashMap<String, String> credentialsMap = new HashMap<>();
		credentialsMap.put("idToken", account.getIdToken());
		String json = gson.toJson(credentialsMap);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("credentials", json);
		editor.apply();
	}
}