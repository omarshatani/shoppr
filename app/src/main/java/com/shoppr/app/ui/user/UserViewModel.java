package com.shoppr.app.ui.user;

import androidx.lifecycle.ViewModel;

import com.shoppr.app.data.user.UserDatabase;

import java.util.Map;

public class UserViewModel extends ViewModel {
    private final UserDatabase database;

    public UserViewModel(UserDatabase database) {
        this.database = database;
    }

    public void updateUser(Map<String, Object> data, String id) {
        database.update(data);
    }
}
