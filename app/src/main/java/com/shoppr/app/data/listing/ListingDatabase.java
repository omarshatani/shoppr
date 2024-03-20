package com.shoppr.app.data.listing;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.database.Database;
import com.shoppr.app.data.firebase.FirestoreDatasource;
import com.shoppr.app.data.listing.model.Listing;

import java.util.ArrayList;

public class ListingDatabase extends Database<Listing> {
    private static final String DOCUMENT = "listings";
    private static volatile ListingDatabase instance;
    private final FirestoreDatasource dataSource;

    private ListingDatabase(FirestoreDatasource dataSource) {
        this.dataSource = dataSource;
    }

    public static ListingDatabase getInstance() {
        if (instance == null) {
            instance = new ListingDatabase(new FirestoreDatasource(DOCUMENT));
        }
        return instance;
    }

    @Override
    public Task<Void> add(Object data) {
        return dataSource.add(data, DOCUMENT);
    }

    @Override
    public void add(String id, Object data) {
    }

    @Override
    public void getAll(Callback<ArrayList<Listing>> callback) {
        dataSource.getAll().addOnCompleteListener(task -> {
            ArrayList<Listing> elements = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.exists()) {
                        Listing element = document.toObject(Listing.class);
                        elements.add(element);
                    }
                }
                callback.onSuccess(new Result.Success<>(elements));
            } else {
                callback.onError(task.getException());
            }
        });
    }
}
