package com.shoppr.app.data.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class FirestoreDatasource {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference reference;

    public FirestoreDatasource(String collection) {
        this.initialise(collection);
    }

    private void initialise(String collection) {
        this.reference = db.collection(collection);
    }

    public void add(Object data, String document) {
        reference.document(document).set(data);
    }

    public Task<DocumentReference> add(Object data) {
        return reference.add(data);
    }

    public Task<QuerySnapshot> getAll() {
        return reference.get();
    }

    public Task<QuerySnapshot> get(String field, String value) {
        return reference.whereEqualTo(field, value).get();
    }

    public Task<Void> update(Map<String, Object> data, String document) {
        return reference.document(document).update(data);
    }

}
