package com.shoppr.app.data.login;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

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

    public Task<AuthResult> loginWithGoogle(Intent data) throws ApiException {
        GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        return firebaseService.signInWithCredential(credential);
    }

    public Task<AuthResult> signup(String username, String password) {
        return firebaseService.createUserWithEmailAndPassword(username, password);
    }

    public void logout() {
        firebaseService.signOut();
    }
}