package com.shoppr.app.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.shoppr.app.data.login.LoginRepository;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.R;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void loginOrRegister(String username, String password) throws ExecutionException, InterruptedException {
        // can be launched in a separate asynchronous job
        AuthResult authResult = loginRepository.login(username, password);

        FirebaseUser user = authResult.getUser();

        if (user != null) {
            loginResult.setValue(new LoginResult(new LoggedInUserView(user.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }

//        loginTask
//                .addOnSuccessListener(authResult -> {
//                    FirebaseUser user = authResult.getUser();
//                    assert user != null;
//                    Log.d("USER", String.valueOf(user.getDisplayName()));
//                    loginResult.setValue(new LoginResult(new LoggedInUserView(user.getDisplayName())));
//                })
//                .addOnFailureListener(exception -> {
//                    loginResult.setValue(new LoginResult(R.string.login_failed));
//                    Log.e("LOGIN FAIL", "");
//                });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}