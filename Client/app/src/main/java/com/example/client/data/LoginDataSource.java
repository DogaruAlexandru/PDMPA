package com.example.client.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.client.data.api.UserLoginAPI;
import com.example.client.data.model.LoggedInUser;

import java.io.IOException;


/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private static final String TAG = "Login";
    public Result<LoggedInUser> login(String email, String password) {
        Log.d(TAG, "login was called in logindatasource " );
        try {
            Log.d(TAG, "enter try " );
            // TODO: handle loggedInUser authentication
            LoggedInUser user = UserLoginAPI.login(new UserLoginAPI.UserLogging(email, password));
            Log.d(TAG, "user " + user);
            return new Result.Success<>(user);

        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

    public Result<LoggedInUser> register(String email, String password) {
        return getResult(email, password);
    }

    @NonNull
    private static Result getResult(String email, String password) {
        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser user = UserLoginAPI.register(new UserLoginAPI.UserLogging(email, password));

            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error registering", e));
        }
    }
}