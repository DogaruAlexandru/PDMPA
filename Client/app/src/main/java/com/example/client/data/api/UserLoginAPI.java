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
import java.util.concurrent.CountDownLatch;

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
        return callEndpoint(userLogging, LOGIN_ENDPOINT);
    }

    public static LoggedInUser register(UserLogging userLogging) throws Exception {
        return callEndpoint(userLogging, REGISTER_ENDPOINT);
    }

    public static LoggedInUser callEndpoint(final UserLogging userLogging, String endpoint) throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final LoggedInUser[] result = {null};
        final Exception[] exception = {null};

        // Start a new thread to perform the network operation
        new Thread(() -> {
            try {
                result[0] = getLoggedInUser(userLogging, endpoint);
            } catch (Exception e) {
                exception[0] = e;
            } finally {
                // Signal that the operation is complete
                latch.countDown();
            }
        }).start();

        try {
            // Wait for the background thread to complete
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        // Check for any exception thrown during the background thread
        if (exception[0] != null) {
            throw exception[0];
        }

        // Return the result obtained from the background thread
        return result[0];
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
