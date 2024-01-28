package com.example.client.data.api;

import static com.example.client.data.api.ServerCommunication.BASE_URL;
import static com.example.client.data.api.ServerCommunication.CLIENT;
import static com.example.client.data.api.ServerCommunication.JSON;
import static com.example.client.data.api.ServerCommunication.gson;

import com.example.client.data.model.Product;
import com.example.client.data.model.ProductFull;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProductAPI {
    private static final String GET_PRODUCT_LIST_ENDPOINT = "/user_products";
    private static final String CREATE_PRODUCT_ENDPOINT = "/create_product";
    private static final String UPDATE_PRODUCT_ENDPOINT = "/update_product";
    private static final String GET_PRODUCT_ENDPOINT = "/get_product";
    private static final String DELETE_PRODUCT_ENDPOINT = "/delete_product";

    public record UserIdProductFull(long userId, ProductFull productFull) {
    }

    public static List<Product> getProducts(long userId) throws IOException {
        String urlWithParams = BASE_URL + GET_PRODUCT_LIST_ENDPOINT + "?userId=" + userId;

        Request request = new Request.Builder()
                .url(urlWithParams)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }

            // Parse the JSON response into a list of Product objects
            Type productListType = new TypeToken<List<Product>>() {}.getType();
            return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), productListType);
        }
    }

    public static void createProduct(UserIdProductFull obj) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(obj), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + CREATE_PRODUCT_ENDPOINT)
                .post(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }
        }
    }

    public static void updateProduct(long productId) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(productId), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + UPDATE_PRODUCT_ENDPOINT)
                .post(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }
        }
    }

    public static ProductFull getProduct(long productId) throws IOException {
        String urlWithParams = BASE_URL + GET_PRODUCT_ENDPOINT + "&productId=" + productId;

        Request request = new Request.Builder()
                .url(urlWithParams)
                .get()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }

            // Parse the JSON response into a ProductFull object
            return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), ProductFull.class);
        }
    }


    public static void deleteProduct(long productId) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(productId), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + DELETE_PRODUCT_ENDPOINT)
                .post(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }
        }
    }

}
