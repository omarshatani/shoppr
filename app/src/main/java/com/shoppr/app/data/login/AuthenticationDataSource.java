package com.shoppr.app.data.login;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shoppr.app.data.login.adapter.UserAdapter;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.domain.storage.CredentialStorage;

import java.util.HashMap;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class AuthenticationDataSource {
    private final FirebaseAuth firebaseService;
    private final UserAdapter userAdapter;
    private final CredentialStorage credentialStorage;

    public AuthenticationDataSource(FirebaseAuth firebaseService, UserAdapter userAdapter, CredentialStorage credentialStorage) {
        this.firebaseService = firebaseService;
        this.userAdapter = userAdapter;
        this.credentialStorage = credentialStorage;
    }

    private FirebaseUser getFirebaseUser() {
        return firebaseService.getCurrentUser();
    }

    public Task<User> login(String username, String password) {
        return firebaseService.signInWithEmailAndPassword(username, password).continueWith(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = task.getResult().getUser();
                assert firebaseUser != null;
                if (!firebaseUser.isEmailVerified()) {
                    throw new FirebaseAuthException("EMAIL_NOT_VALID", "EMAIL_NOT_VALID");
                }
                return userAdapter.adaptUserFromFirebaseUser(firebaseUser);
            } else {
                throw task.getException();
            }
        });
    }

    public Task<User> loginWithGoogle(Intent data) throws ApiException {
        GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
        AuthCredential credentials = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        HashMap<String, String> credentialsMap = new HashMap<>();
        credentialsMap.put("idToken", account.getIdToken());
        this.credentialStorage.saveCredentials(credentialsMap);
        return firebaseService.signInWithCredential(credentials)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = task.getResult().getUser();

                        assert firebaseUser != null;
                        return userAdapter.adaptUserFromFirebaseUser(firebaseUser);
                    } else {
                        throw task.getException();
                    }
                });
    }

    public Task<AuthResult> signup(String username, String password) {
        return firebaseService.createUserWithEmailAndPassword(username, password);
    }

    public void logout() {
        firebaseService.signOut();

    }

    public User getUser() {
        FirebaseUser firebaseUser = this.getFirebaseUser();
        return userAdapter.adaptUserFromFirebaseUser(firebaseUser);
    }

    public void reauthenticate(AuthCredential authCredential) {
        FirebaseUser firebaseUser = this.getFirebaseUser();
        if (firebaseUser != null) {
            firebaseUser.reauthenticate(authCredential);
        }
    }
}