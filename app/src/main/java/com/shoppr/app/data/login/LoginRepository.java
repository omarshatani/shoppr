package com.shoppr.app.data.login;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.login.adapter.UserAdapter;
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

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource, UserAdapter userAdapter) {
        this.dataSource = dataSource;
        this.userAdapter = userAdapter;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource, UserAdapter userAdapter) {
        if (instance == null) {
            instance = new LoginRepository(dataSource, userAdapter);
        }
        return instance;
    }

    public void logout(Callback<Void> callback) {
        dataSource.logout();
        callback.onSuccess(null);
    }

    public void login(final String username, final String password, final Callback<User> signInCallback, Callback<LoginResult> signupCallback) {
        Task<AuthResult> loginTask = dataSource.login(username, password);
        loginTask.addOnCompleteListener(result -> {
            if (result.isSuccessful()) {
                FirebaseUser firebaseUser = result.getResult().getUser();
                assert firebaseUser != null;
                if (firebaseUser.isEmailVerified()) {
                    signInCallback.onSuccess(new Result.Success<>(userAdapter.adaptUserFromFirebaseUser(firebaseUser)));
                } else {
                    signInCallback.onError(new Exception("Email not validated"));
                }
            } else {
                this.signup(username, password, signupCallback);
            }
        });
    }

    public void loginWithGoogle(Intent data, Callback<User> callback) {
        try {
            Task<AuthResult> loginTask = dataSource.loginWithGoogle(data);
            loginTask.addOnCompleteListener(result -> {
                if (result.isSuccessful()) {
                    FirebaseUser firebaseUser = result.getResult().getUser();
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

    public User getCurrentUser() {
        FirebaseUser firebaseUser = dataSource.getUser();
        return userAdapter.adaptUserFromFirebaseUser(firebaseUser);
    }

    private void signup(String username, String password, Callback<LoginResult> callback) {
        Task<AuthResult> signupTask = dataSource.signup(username, password);

        signupTask.addOnCompleteListener(result -> {
            if (result.isSuccessful()) {
                FirebaseUser firebaseUser = result.getResult().getUser();
                if (firebaseUser != null) {
                    firebaseUser.sendEmailVerification();
                    callback.onComplete(new Result.Success<>(new LoginResult("An email verification has been sent")));
                }
            } else {
                callback.onComplete(new Result.Error<>(result.getException()));
            }
        });
    }

}