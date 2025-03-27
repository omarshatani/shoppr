package com.shoppr.app.domain.user;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.shoppr.app.data.login.AuthenticationDataSource;
import com.shoppr.app.data.user.UserDatabase;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.domain.user.model.IUserRepository;

import java.util.Map;

public class UserRepository implements IUserRepository {
    private static volatile UserRepository instance;
    private final AuthenticationDataSource authenticationDataSource;
    private final UserDatabase userDatabase;

    public UserRepository(AuthenticationDataSource authenticationDataSource, UserDatabase userDatabase) {
        this.authenticationDataSource = authenticationDataSource;
        this.userDatabase = userDatabase;
    }

    public static UserRepository getInstance(AuthenticationDataSource dataSource, UserDatabase userDatabase) {
        if (instance == null) {
            instance = new UserRepository(dataSource, userDatabase);
        }
        return instance;
    }

    @Override
    public User getCurrentUser() {
        User user = authenticationDataSource.getUser();
        Task<DocumentSnapshot> userTask = userDatabase.get(user.getUuid());

        userTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot snapshot = task.getResult();
                user.setEmail(snapshot.getString("email"));
                user.setLatitude(snapshot.getDouble("latitude"));
                user.setLongitude(snapshot.getDouble("longitude"));
            }
        });

        return user;
    }

    @Override
    public void updateCurrentUser(Map<String, Object> data) {
        userDatabase.update(data);
    }
}
