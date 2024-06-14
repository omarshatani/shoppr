package com.shoppr.app.data.database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shoppr.app.data.common.Callback;

import java.util.ArrayList;

public abstract class Database<T> {
	public abstract Task<DocumentReference> add(Object data);

	public abstract Task<DocumentSnapshot> get(String id);

	public abstract void add(String id, Object data);

	public abstract void updateField(String id, String field, Object value);

	public abstract void updateArrayField(String id, String field, Object value);

	public abstract void getAll(Callback<ArrayList<T>> callback);

	public abstract Task<QuerySnapshot> getAll();
}