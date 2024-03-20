package com.shoppr.app.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoppr.app.data.user.UserDatabase;
import com.shoppr.app.data.user.model.User;

import java.util.Map;

public class UserViewModel extends ViewModel {
    private final UserDatabase database;

    MutableLiveData<User> user = new MutableLiveData<>(null);

    public UserViewModel(UserDatabase database) {
        this.database = database;
    }

    public LiveData<User> user() {
        return user;
    }

    public void setUser(User value) {
        user.setValue(value);
    }

    public void updateUser(Map<String, Object> data, String id) {
        database.update(data, id);
    }
}
