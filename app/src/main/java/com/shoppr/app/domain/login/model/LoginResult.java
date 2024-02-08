package com.shoppr.app.domain.login.model;

import androidx.annotation.Nullable;


/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private String error;

    public LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    public LoginResult(@Nullable String error) {
        this.error = error;
    }

    @Nullable
    public LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    public String getError() {
        return error;
    }
}