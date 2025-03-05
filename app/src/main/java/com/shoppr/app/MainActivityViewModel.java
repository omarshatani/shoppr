package com.shoppr.app;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoppr.app.domain.authentication.AuthenticationRepository;

public class MainActivityViewModel extends ViewModel {
    private final AuthenticationRepository authenticationRepository;
    private final MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();

    public MainActivityViewModel(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    public LiveData<Boolean> isLoggedIn() {
        return isLoggedIn;
    }

    public void checkUserLoginStatus() {
        isLoggedIn.postValue(this.authenticationRepository.checkUserLoginStatus());
    }
}
