package com.example.client.data.api;

import static com.example.client.data.api.ServerCommunication.BASE_URL;
import static com.example.client.data.api.ServerCommunication.CLIENT;
import static com.example.client.data.api.ServerCommunication.JSON;
import static com.example.client.data.api.ServerCommunication.gson;

import com.example.client.data.model.Container;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContainerAPI {
    private static final String GET_CONTAINER_LIST_ENDPOINT = "/user_containers";
    private static final String CREATE_CONTAINER_ENDPOINT = "/create_container";
    private static final String UPDATE_CONTAINER_ENDPOINT = "/update_container";
    private static final String GET_CONTAINER_ENDPOINT = "/get_container";
    private static final String DELETE_CONTAINER_ENDPOINT = "/delete_container";

    public record UserIdContainer(long userId, Container container) {
    }

    public static List<Container> getContainers(long userId) throws IOException {
        String urlWithParams = BASE_URL + GET_CONTAINER_LIST_ENDPOINT + "?userId=" + userId;

        Request request = new Request.Builder()
                .url(urlWithParams)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }

            // Parse the JSON response into a list of Container objects
            Type containerListType = new TypeToken<List<Container>>() {}.getType();
            return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), containerListType);
        }
    }

    public static void createContainer(UserIdContainer obj) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(obj), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + CREATE_CONTAINER_ENDPOINT)
                .post(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }
        }
    }

    public static void updateContainer(long containerId) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(containerId), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + UPDATE_CONTAINER_ENDPOINT)
                .post(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }
        }
    }

    public static Container getContainer(long containerId) throws IOException {
        String urlWithParams = BASE_URL + GET_CONTAINER_ENDPOINT + "&containerId=" + containerId;

        Request request = new Request.Builder()
                .url(urlWithParams)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }

            // Parse the JSON response into a Container object
            return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), Container.class);
        }
    }


    public static void deleteContainer(long containerId) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(containerId), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + DELETE_CONTAINER_ENDPOINT)
                .post(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }
        }
    }
}
