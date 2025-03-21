package com.shoppr.app.ui.login;

import android.content.Intent;
import android.util.Patterns;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shoppr.app.R;
import com.shoppr.app.data.common.Callback;
import com.shoppr.app.data.common.Result;
import com.shoppr.app.data.user.model.User;
import com.shoppr.app.domain.authentication.AuthenticationRepository;
import com.shoppr.app.domain.authentication.model.LoginFormState;
import com.shoppr.app.domain.authentication.model.LoginResult;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final AuthenticationRepository authenticationRepository;

    LoginViewModel(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void loginOrRegister(String username, String password) {
        authenticationRepository.login(username, password, new Callback<User>() {
            @Override
            public void onSuccess(Result.Success<User> result) {
                User loggedInUser = result.getData();
                loginResult.postValue(new LoginResult(loggedInUser));
            }

            @Override
            public void onError(Exception exception) {
                loginResult.postValue(new LoginResult(exception.getMessage()));
            }
        }, new Callback<LoginResult>() {
            @Override
            public void onSuccess(@Nullable Result.Success<LoginResult> result) {
                assert result != null;
                loginResult.postValue(result.getData());
            }

            @Override
            public void onError(Exception exception) {
                loginResult.postValue(new LoginResult(exception.getMessage()));
            }
        });
    }

    public void loginWithGoogle(Intent data) {
        authenticationRepository.loginWithGoogle(data, new Callback<User>() {
            @Override
            public void onSuccess(Result.Success<User> result) {
                User loggedInUser = result.getData();
                loginResult.setValue(new LoginResult(loggedInUser));
            }

            @Override
            public void onError(Exception exception) {
                loginResult.postValue(new LoginResult(exception.getMessage()));
            }
        });
    }

    public void logout() {
        authenticationRepository.logout(null);
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

    public String getUserId() {
        return authenticationRepository.getCurrentUser().getUuid();
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