package com.shoppr.app.data.map;

import com.google.firebase.firestore.FirebaseFirestore;

public class MapDataSource {
    private FirebaseFirestore database;

    public MapDataSource(FirebaseFirestore database) {
        this.database = database;
    }
}
