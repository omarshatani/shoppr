package com.shoppr.app.data.login.adapter;

import com.google.firebase.auth.FirebaseUser;
import com.shoppr.app.data.login.model.LoggedInUser;

public class UserAdapter {

    public UserAdapter() {
    }

    public LoggedInUser adaptUserFromFirebaseUser(FirebaseUser user) {
        if (user == null) {
            return null;
        }
        return new LoggedInUser(user.getUid(), user.getDisplayName());
    }
}
