package com.shoppr.app.ui.login;

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

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class LoginViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public LoginViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(
                    AuthenticationRepository.getInstance(
                            new AuthenticationDataSource(
                                    FirebaseAuth.getInstance(),
                                    new UserAdapter(),
                                    new CredentialStorage(context)),
                            UserDatabase.getInstance(),
                            new CredentialStorage(context)
                    )
            );
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}