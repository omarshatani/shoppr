package com.shoppr.app.data.login;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shoppr.app.data.common.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private final FirebaseAuth service;

    public LoginDataSource(FirebaseAuth service) {
        this.service = service;
    }

    public Task<AuthResult> login(String username, String password) throws ExecutionException, InterruptedException {
       return service.signInWithEmailAndPassword(username, password);
    }

    private Result<AuthResult> signup(String username, String password) {
        Task<AuthResult> signupTask = service.createUserWithEmailAndPassword(username, password);
        try {
            Tasks.await(signupTask);
            if (signupTask.isSuccessful()) {
                Log.d("SIGNUP", "SUCCESS");
                return new Result.Success(signupTask.getResult());
            }
        } catch (Exception exception) {
            Log.e("SIGNUP", "ERROR");
            return new Result.Error(exception);
        }
        return null;
    }


    public void logout() {
        // TODO: revoke authentication
        service.signOut();
    }
}