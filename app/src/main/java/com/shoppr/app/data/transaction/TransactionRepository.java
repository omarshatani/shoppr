package com.shoppr.app.data.transaction;

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

	public void saveTransaction(Transaction transaction) {
		database.add(transaction);
	}
}
