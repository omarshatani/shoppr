package com.shoppr.app.data.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreDatasource {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference reference;

    public FirestoreDatasource() {
        this.initialise();
    }

    private void initialise() {
        this.reference = db.collection("listings").document("SO");
    }

    public Task<Void> add(Object data) {
        return reference.set(data);
    }

    public Task<DocumentSnapshot> get() {
        return reference.get();
    }


}
