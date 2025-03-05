package com.shoppr.app.ui.map;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.shoppr.app.data.listing.ListingDatabase;
import com.shoppr.app.data.login.AuthenticationDataSource;
import com.shoppr.app.data.login.adapter.UserAdapter;
import com.shoppr.app.data.map.MapDataSource;
import com.shoppr.app.data.map.MapRepository;
import com.shoppr.app.data.request.RequestDatabase;
import com.shoppr.app.data.user.UserDatabase;
import com.shoppr.app.domain.authentication.AuthenticationRepository;
import com.shoppr.app.domain.storage.CredentialStorage;

public class MapViewModelFactory implements ViewModelProvider.Factory {
    private final Context context; // Add the Context

    public MapViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(
                    MapRepository.getInstance(
                            new MapDataSource(
                                    ListingDatabase.getInstance(),
                                    UserDatabase.getInstance(),
                                    RequestDatabase.getInstance()
                            )
                    ),
                    AuthenticationRepository.getInstance(
                            new AuthenticationDataSource(
                                    FirebaseAuth.getInstance(),
                                    new UserAdapter(),
                                    new CredentialStorage(context)),
                            UserDatabase.getInstance(),
                            new CredentialStorage(context))
            );
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
