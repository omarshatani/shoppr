package com.shoppr.app.ui.overview.buy;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.shoppr.app.data.listing.ListingDatabase;
import com.shoppr.app.data.login.AuthenticationDataSource;
import com.shoppr.app.data.login.adapter.UserAdapter;
import com.shoppr.app.data.user.UserDatabase;
import com.shoppr.app.domain.listing.PostListingUseCase;
import com.shoppr.app.domain.storage.CredentialStorage;
import com.shoppr.app.domain.user.UserRepository;

public class BuyOverviewViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public BuyOverviewViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BuyOverviewViewModel.class)) {
            return (T) new BuyOverviewViewModel(new PostListingUseCase(ListingDatabase.getInstance()),
                    new UserRepository(
                            new AuthenticationDataSource(
                                    FirebaseAuth.getInstance(),
                                    new UserAdapter(),
                                    new CredentialStorage(context)),
                            UserDatabase.getInstance()
                    )
            );
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
