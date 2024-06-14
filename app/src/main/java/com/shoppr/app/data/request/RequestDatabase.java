package com.shoppr.app.data.request;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.database.Database;
import com.shoppr.app.data.firebase.FirestoreDatasource;
import com.shoppr.app.data.request.model.Request;

import java.util.ArrayList;

public class RequestDatabase extends Database<Request> {
	private static final String DOCUMENT = "requests";
	private static volatile RequestDatabase instance;
	private final FirestoreDatasource dataSource;

	public RequestDatabase(FirestoreDatasource dataSource) {
		this.dataSource = dataSource;
	}

	public static RequestDatabase getInstance() {
		if (instance == null) {
			synchronized (RequestDatabase.class) {
				instance = new RequestDatabase(new FirestoreDatasource(DOCUMENT));
			}
		}
		return instance;
	}

	@Override
	public Task<DocumentReference> add(Object data) {
		return dataSource.add(data);
	}

	@Override
	public Task<DocumentSnapshot> get(String id) {
		return null;
	}

	@Override
	public void add(String id, Object data) {
		dataSource.add(data, id);
	}

	@Override
	public void updateField(String id, String field, Object value) {

	}

	@Override
	public void updateArrayField(String id, String field, Object value) {

	}

	@Override
	public void getAll(Callback<ArrayList<Request>> callback) {
		// TODO: To be implemented
	}

	@Override
	public Task<QuerySnapshot> getAll() {
		return dataSource.getAll();
	}
}
