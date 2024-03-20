package com.shoppr.app.data.user;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.database.Database;
import com.shoppr.app.data.firebase.FirestoreDatasource;
import com.shoppr.app.data.user.model.User;

import java.util.ArrayList;
import java.util.Map;

public class UserDatabase extends Database<User> {
    private static final String DOCUMENT = "users";
    private static volatile UserDatabase instance;
    private final FirestoreDatasource dataSource;

    private UserDatabase(FirestoreDatasource dataSource) {
        this.dataSource = dataSource;
    }

    public static UserDatabase getInstance() {
        if (instance == null) {
            instance = new UserDatabase(new FirestoreDatasource(DOCUMENT));
        }
        return instance;
    }

    @Override
    public Task<Void> add(Object data) {
        return dataSource.add(data, DOCUMENT);
    }

    @Override
    public void add(String id, Object data) {
        dataSource.add(data, id);
    }

    @Override
    public void getAll(Callback<ArrayList<User>> callback) {
        dataSource.getAll().addOnCompleteListener(task -> {
            ArrayList<User> users = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        users.add(user);
                    }
                }
                callback.onSuccess(new Result.Success<>(users));
            } else {
                callback.onError(task.getException());
            }
        });
    }

    public void update(Map<String, Object> data, String document) {
        dataSource.update(data, document);
    }
}
