package com.shoppr.app.domain.authentication.model;

import android.content.Intent;

import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.user.model.User;

public interface IAuthenticationRepository {
    void login(final String username, final String password, final Callback<User> signInCallback, Callback<LoginResult> signupCallback);

    void loginWithGoogle(Intent data, Callback<User> callback);

    User getCurrentUser();

    void logout(Callback<Void> callback);


    void signup(String username, String password, Callback<LoginResult> callback);

    Boolean checkUserLoginStatus();

}
