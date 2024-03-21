package com.shoppr.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoppr.app.data.user.model.User;

public class MainActivityViewModel extends ViewModel {
    private final MutableLiveData<User> user = new MutableLiveData<>();

    public LiveData<User> user() {
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }
}
