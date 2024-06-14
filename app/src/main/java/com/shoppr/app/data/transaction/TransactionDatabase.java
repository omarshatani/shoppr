package com.shoppr.app.data.transaction;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.database.Database;
import com.shoppr.app.data.firebase.FirestoreDatasource;
import com.shoppr.app.data.transaction.model.Transaction;

import java.util.ArrayList;

public class TransactionDatabase extends Database<Transaction> {
	private static final String DOCUMENT = "transactions";
	private static volatile TransactionDatabase instance;
	private final FirestoreDatasource dataSource;

	private TransactionDatabase(FirestoreDatasource dataSource) {
		this.dataSource = dataSource;
	}

	public static TransactionDatabase getInstance() {
		synchronized (TransactionDatabase.class) {
			if (instance == null) {
				instance = new TransactionDatabase(new FirestoreDatasource(DOCUMENT));
			}
		}
		return instance;
	}


	@Override
	public Task<DocumentReference> add(Object data) {
		return dataSource.add(data);
	}


	@Override
	public void add(String id, Object data) {
		dataSource.add(data, id);
	}

	@Override
	public void getAll(Callback<ArrayList<Transaction>> callback) {
		dataSource.getAll().addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				ArrayList<Transaction> elements = new ArrayList<>();
				for (QueryDocumentSnapshot document : task.getResult()) {
					if (document.exists()) {
						elements.add(document.toObject(Transaction.class));
					}
				}
				callback.onSuccess(new Result.Success<>(elements));
			} else {
				callback.onError(task.getException());
			}
		});
	}

	@Override
	public Task<QuerySnapshot> getAll() {
		return dataSource.getAll();
	}

	@Override
	public Task<DocumentSnapshot> get(String id) {
		return null;
	}

	@Override
	public void updateField(String id, String field, Object value) {

	}

	@Override
	public void updateArrayField(String id, String field, Object value) {

	}
}
