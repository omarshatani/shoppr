package com.shoppr.app.domain.login.model;

import androidx.annotation.Nullable;

import com.shoppr.app.data.user.model.User;


/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private User user;


    @Nullable
    private String error;

    public LoginResult(@Nullable User user) {
        this.user = user;
    }

    public LoginResult(@Nullable String error) {
        this.error = error;
    }

    @Nullable
    public User getUser() {
        return user;
    }

    @Nullable
    public String getError() {
        return error;
    }
}