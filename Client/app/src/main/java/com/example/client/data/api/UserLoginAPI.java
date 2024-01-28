package com.example.client.data.api;

import androidx.annotation.NonNull;

import com.example.client.data.model.LoggedInUser;
import com.example.client.data.model.UserLogging;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserLoginAPI implements ServerCommunication {
    private static final String REGISTER_ENDPOINT = "/register";
    private static final String LOGIN_ENDPOINT = "/login";
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static LoggedInUser login(String email, String password) throws Exception {
        return getLoggedInUser(email, password, LOGIN_ENDPOINT);
    }

    public static LoggedInUser register(String email, String password) throws Exception {
        return getLoggedInUser(email, password, REGISTER_ENDPOINT);
    }

    @NonNull
    private static LoggedInUser getLoggedInUser(String email, String password, String endpoint) throws IOException {
        Gson gson = new Gson();

        UserLogging user = new UserLogging(email, password);

        RequestBody requestBody = RequestBody.create(gson.toJson(user), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }

            // Use Gson to deserialize the JSON response directly into a LoggedInUser object
            return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), LoggedInUser.class);
        }
    }
}
