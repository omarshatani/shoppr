package com.shoppr.app;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.shoppr.app.data.login.AuthenticationDataSource;
import com.shoppr.app.data.login.adapter.UserAdapter;
import com.shoppr.app.data.user.UserDatabase;
import com.shoppr.app.domain.authentication.AuthenticationRepository;
import com.shoppr.app.domain.storage.CredentialStorage;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public MainActivityViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(
                    AuthenticationRepository.getInstance(
                            new AuthenticationDataSource(
                                    FirebaseAuth.getInstance(),
                                    new UserAdapter(),
                                    new CredentialStorage(context)),
                            UserDatabase.getInstance(),
                            new CredentialStorage(context)));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
