package com.shoppr.app.data.database;

import com.google.firebase.firestore.DocumentSnapshot;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.firebase.FirestoreDatasource;

public class Database {
    private static volatile Database instance;
    private final FirestoreDatasource firestore;

    private Database(
            final FirestoreDatasource firestoreDatasource
    ) {
        this.firestore = firestoreDatasource;
    }

    public static Database getInstance(final FirestoreDatasource firestoreDatasource) {
        if (instance == null) {
            instance = new Database(firestoreDatasource);
        }
        return instance;
    }

    public void add(Object data, Callback<Void> callback) {
        firestore.add(data).addOnCompleteListener(result -> callback.onComplete(new Result.Success<>(result.getResult())));
    }

    public <T> void get(Callback<T> callback) {
        firestore.get().addOnCompleteListener(result -> {
            if (result.isSuccessful()) {
                DocumentSnapshot documentSnapshot = result.getResult();
                if (documentSnapshot.exists()) {
                    callback.onSuccess((Result<T>) new Result.Success<>(documentSnapshot.getData()));
                } else {
                    callback.onError(result.getException());
                }
            }
        });
    }


}
