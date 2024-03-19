package com.shoppr.app.data.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirestoreDatasource {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference;

    public FirestoreDatasource(String collection) {
        this.initialise(collection);
    }

    private void initialise(String collection) {
        this.reference = db.collection(collection);
    }

    public Task<Void> add(Object data, String document) {
        return reference.document(document).set(data);
    }

    public Task<QuerySnapshot> get() {
        return reference.get();
    }

}
