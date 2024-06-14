package com.shoppr.app.data.listing;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.database.Database;
import com.shoppr.app.data.firebase.FirestoreDatasource;
import com.shoppr.app.data.listing.model.Listing;

import java.util.ArrayList;

public class ListingDatabase extends Database<Listing> {
    private static final String DOCUMENT = "listings";
    private static volatile ListingDatabase instance;
    private final FirestoreDatasource dataSource;

    private ListingDatabase(FirestoreDatasource dataSource) {
        this.dataSource = dataSource;
    }

    public static ListingDatabase getInstance() {
        if (instance == null) {
					synchronized (ListingDatabase.class) {
						instance = new ListingDatabase(new FirestoreDatasource(DOCUMENT));
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
	public Task<DocumentSnapshot> get(String id) {
		return dataSource.get(id);
	}

    @Override
    public void getAll(Callback<ArrayList<Listing>> callback) {
        dataSource.getAll().addOnCompleteListener(task -> {
            ArrayList<Listing> elements = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.exists()) {
											Listing listing = document.toObject(Listing.class);
											listing.setId(document.getId());
											elements.add(listing);
                    }
                }
                callback.onSuccess(new Result.Success<>(elements));
            } else {
                callback.onError(task.getException());
            }
        });
    }

	@Override
	public void updateField(String id, String field, Object value) {
		dataSource.updateField(field, value, id);
	}

	@Override
	public void updateArrayField(String id, String field, Object value) {
		dataSource.updateArrayField(field, value, id);
	}

	@Override
	public Task<QuerySnapshot> getAll() {
		return dataSource.getAll();
	}


}
