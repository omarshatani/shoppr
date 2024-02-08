package com.shoppr.app.data.login;

import android.content.Intent;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.shoppr.app.data.login.adapter.UserAdapter;
import com.shoppr.app.data.login.model.LoggedInUser;
import com.shoppr.app.domain.login.model.LoggedInUserView;
import com.shoppr.app.domain.login.model.LoginResult;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;
    private final LoginDataSource dataSource;
    private final UserAdapter userAdapter;
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoggedInUser user;

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

    public void logout() {
        setLoggedInUser(null);
        dataSource.logout();
    }

    private void setLoggedInUser(FirebaseUser firebaseUser) {
        LoggedInUser user = userAdapter.adaptUserFromFirebaseUser(firebaseUser);
        this.user = user;
        if (this.user != null) {
            loginResult.postValue(new LoginResult(new LoggedInUserView(user.getDisplayName())));
        } else {
            loginResult.postValue(null);
        }
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LoggedInUser getCurrentUser() {
        return user;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        Task<AuthResult> loginTask = dataSource.login(username, password);

        loginTask.addOnCompleteListener(result -> {
            if (result.isSuccessful()) {
                FirebaseUser firebaseUser = result.getResult().getUser();
                assert firebaseUser != null;
                if (firebaseUser.isEmailVerified()) {
                    setLoggedInUser(firebaseUser);
                } else {
                    loginResult.postValue(new LoginResult("Email not verified"));
                }
            } else {
                this.signup(username, password);
            }
        });
    }

    public void loginWithGoogle(Intent data) {
        try {
            Task<AuthResult> loginTask = dataSource.loginWithGoogle(data);

            loginTask.addOnCompleteListener(result -> {
                if (result.isSuccessful()) {
                    FirebaseUser firebaseUser = result.getResult().getUser();
                    setLoggedInUser(firebaseUser);
                } else {
                    loginResult.postValue(null);
                }
            });
        } catch (ApiException exception) {
            Log.e("ERROR SOCIAL SIGN IN", "DAMN");
            loginResult.postValue(null);
        }

    }

    private void signup(String username, String password) {
        Task<AuthResult> signupTask = dataSource.signup(username, password);

        signupTask.addOnCompleteListener(result -> {
            if (result.isSuccessful()) {
                FirebaseUser firebaseUser = result.getResult().getUser();
                if (firebaseUser != null) {
                    firebaseUser.sendEmailVerification();
                    loginResult.postValue(new LoginResult("An email verification has been sent"));
                }
            } else {
                setLoggedInUser(null);
            }
        });
    }


}