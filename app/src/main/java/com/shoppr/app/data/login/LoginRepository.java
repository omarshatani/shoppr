package com.shoppr.app.data.login;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.login.model.LoggedInUser;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private final LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private FirebaseUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(FirebaseUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public AuthResult login(String username, String password) throws ExecutionException, InterruptedException {
        CompletableFuture<AuthResult> task = new CompletableFuture<>();

        CompletableFuture.supplyAsync(() -> {
            try {
                Task<AuthResult> loginTask = dataSource.login(username, password);
                Tasks.await(loginTask);
                if (loginTask.isSuccessful()) {
                    task.complete(loginTask.getResult());
                } else {
                    task.complete(null);
                }
            } catch (ExecutionException | InterruptedException e) {
                task.completeExceptionally(e);
            }
            return null;
        });

        AuthResult result = task.get();

        return result;
    }
}