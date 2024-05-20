package com.shoppr.app.data.transaction;

import android.os.Handler;

import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.transaction.model.Transaction;

public class TransactionRepository {
	private static volatile TransactionRepository instance;
	private final TransactionDatabase database;

	public TransactionRepository(TransactionDatabase database) {
		this.database = database;
	}

	public static TransactionRepository getInstance(TransactionDatabase database) {
		synchronized (TransactionRepository.class) {
			if (instance == null) {
				instance = new TransactionRepository(database);
			}
		}
		return instance;
	}

	public void saveTransaction(Transaction transaction, Callback<Boolean> callback) {
		database.add(transaction).addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				Handler handler = new Handler();
				handler.postDelayed(() -> callback.onSuccess(new Result.Success<>(true)), 3000);
			} else {
				callback.onError(task.getException());
			}
		});
	}
}
