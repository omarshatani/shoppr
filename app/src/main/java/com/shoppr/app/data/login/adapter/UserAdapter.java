package com.shoppr.app.data.login.adapter;

import com.google.firebase.auth.FirebaseUser;
import com.shoppr.app.data.login.model.LoggedInUser;
import com.shoppr.app.data.user.model.User;

public class UserAdapter {

    public UserAdapter() {
    }

    public User adaptUserFromFirebaseUser(FirebaseUser user) {
        if (user == null) {
            return null;
        }
        return new User(user.getDisplayName(), user.getEmail(), user.getPhoneNumber());
    }
}
