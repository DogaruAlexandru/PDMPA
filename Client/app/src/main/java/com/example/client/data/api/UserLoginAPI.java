package com.example.client.data.api;

import androidx.annotation.NonNull;

import com.example.client.data.model.LoggedInUser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

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
        JsonObject json = getJsonObject(email, password);

        RequestBody requestBody = RequestBody.create(json.getAsString(), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(requestBody)
                .build();

        Response response = CLIENT.newCall(request).execute();
        json = JsonParser.parseString(response.toString()).getAsJsonObject();

        if (!response.isSuccessful()) {
            assert !json.isEmpty() : "Request failed with code: " + response.code();
            throw new IOException(json.getAsString());
        }

        return new LoggedInUser(json.get("user_id").getAsString(), email);
    }

    @NonNull
    private static JsonObject getJsonObject(String email, String password) {
        JsonObject json = new JsonObject();
        json.addProperty("username", email);
        json.addProperty("password", password);
        return json;
    }
}
