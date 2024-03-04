package com.shoppr.app.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoppr.app.data.user.model.User;

public class UserViewModel extends ViewModel {

    MutableLiveData<User> user = new MutableLiveData<>(null);

    public UserViewModel() {
    }

    public LiveData<User> user() {
        return user;
    }

    public void setUser(User value) {
        user.setValue(value);
    }
}
