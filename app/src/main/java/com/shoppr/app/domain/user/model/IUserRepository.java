package com.shoppr.app.domain.user.model;

import com.shoppr.app.data.user.model.User;

import java.util.Map;

public interface IUserRepository {
    User getCurrentUser();

    void updateCurrentUser(Map<String, Object> data);
}
