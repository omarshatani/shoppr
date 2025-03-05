package com.shoppr.app.domain.user;

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
        return authenticationDataSource.getUser();
    }

    @Override
    public void updateCurrentUser(Map<String, Object> data) {
        userDatabase.update(data);
    }
}
