package com.shoppr.app.data.database;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreDatabase {
    private static volatile FirebaseFirestore instance;

    public static FirebaseFirestore getInstance() {
        if (instance == null) {
            instance = FirebaseFirestore.getInstance();
        }
        return instance;
    }
}
