package com.shoppr.app.domain.authentication;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.login.AuthenticationDataSource;
import com.shoppr.app.data.user.UserDatabase;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.domain.authentication.model.IAuthenticationRepository;
import com.shoppr.app.domain.authentication.model.LoginResult;
import com.shoppr.app.domain.storage.CredentialStorage;

import javax.annotation.Nullable;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class AuthenticationRepository implements IAuthenticationRepository {

    private static volatile AuthenticationRepository instance;
    private final AuthenticationDataSource dataSource;
    private final UserDatabase userDatabase;
    private final CredentialStorage credentialStorage;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore

    // private constructor : singleton access

    private AuthenticationRepository(AuthenticationDataSource dataSource, UserDatabase userDatabase, CredentialStorage credentialStorage) {
        this.dataSource = dataSource;
        this.userDatabase = userDatabase;
        this.credentialStorage = credentialStorage;
    }

    public static AuthenticationRepository getInstance(AuthenticationDataSource dataSource, UserDatabase userDatabase, CredentialStorage credentialStorage) {
        if (instance == null) {
            instance = new AuthenticationRepository(dataSource, userDatabase, credentialStorage);
        }
        return instance;
    }

    @Override
    public void logout(@Nullable Callback<Void> callback) {
        dataSource.logout();
    }

    public void login(final String username, final String password, final Callback<User> signInCallback, Callback<LoginResult> signupCallback) {
        Task<User> loginTask = dataSource.login(username, password);
        loginTask
                .addOnSuccessListener(user -> signInCallback.onSuccess(new Result.Success<>(user)))
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

    public void loginWithGoogle(Intent data, Callback<User> callback) {
        try {
            Task<User> loginTask = dataSource.loginWithGoogle(data);
            loginTask.addOnCompleteListener(result -> {
                if (result.isSuccessful()) {
                    User user = result.getResult();

                    callback.onSuccess(new Result.Success<>(user));
                } else {
                    callback.onError(result.getException());
                }
            });
        } catch (ApiException exception) {
            Log.e("ERROR SOCIAL SIGN IN", "DAMN");
            callback.onError(exception);
        }
    }

    @Override
    public User getCurrentUser() {
        AuthCredential credentials = this.credentialStorage.getCredentials();

        if (credentials == null) {
            return null;
        }

        dataSource.reauthenticate(credentials);

        return dataSource.getUser();
    }

    @Override
    public void signup(String username, String password, Callback<LoginResult> callback) {
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

    @Override
    public Boolean checkUserLoginStatus() {
        User user = dataSource.getUser();
        return user != null;
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