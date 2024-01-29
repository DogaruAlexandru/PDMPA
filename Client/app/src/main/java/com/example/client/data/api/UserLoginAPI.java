package com.example.client.data.api;

import static com.example.client.data.api.ServerCommunication.BASE_URL;
import static com.example.client.data.api.ServerCommunication.CLIENT;
import static com.example.client.data.api.ServerCommunication.JSON;
import static com.example.client.data.api.ServerCommunication.gson;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.client.data.model.LoggedInUser;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserLoginAPI {
    private static final String TAG = "Login";
    private static final String REGISTER_ENDPOINT = "/register";
    private static final String LOGIN_ENDPOINT = "/login";

    public record UserLogging(String username, String password) {
    }

    public static LoggedInUser login(UserLogging userLogging) throws Exception {
        return getLoggedInUser(userLogging, LOGIN_ENDPOINT);
    }

    public static LoggedInUser register(UserLogging userLogging) throws Exception {
        return getLoggedInUser(userLogging, REGISTER_ENDPOINT);
    }

    @NonNull
    private static LoggedInUser getLoggedInUser(UserLogging userLogging, String endpoint) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(userLogging), JSON);

        Log.d(TAG, "requestbody "+ requestBody );
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(requestBody)
                .build();

        Log.d(TAG, "request " + request );

        try (Response response = CLIENT.newCall(request).execute()) {
            Log.d(TAG, "enter try in user log api " );
            if (!response.isSuccessful()) {

                Log.d(TAG, "Request failed with code: " + response.code() );
                throw new IOException("Request failed with code: " + response.code());
            }

            // Use Gson to deserialize the JSON response directly into a LoggedInUser object
            return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), LoggedInUser.class);
        }
    }
}