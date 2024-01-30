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
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

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
        // Create a Callable to perform the network call
        Callable<List<Product>> getProductsCallable = () -> getProductsCall(userId);

        // Wrap the Callable in a FutureTask
        FutureTask<List<Product>> futureTask = new FutureTask<>(getProductsCallable);

        // Start a new thread to execute the FutureTask
        new Thread(futureTask).start();

        // Wait for the task to complete and get the result
        try {
            return futureTask.get();
        } catch (Exception e) {
            // Handle exceptions if needed
            e.printStackTrace();
            return null; // Or throw an exception if appropriate
        }
    }

    private static List<Product> getProductsCall(long userId) throws IOException {
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
        // Create a Callable to perform the network call
        Callable<Void> createProductCallable = () -> {
            createProductCall(obj);
            return null; // Callable requires a return type
        };

        // Wrap the Callable in a FutureTask
        FutureTask<Void> futureTask = new FutureTask<>(createProductCallable);

        // Start a new thread to execute the FutureTask
        new Thread(futureTask).start();

        // Wait for the task to complete
        try {
            futureTask.get();
        } catch (Exception e) {
            // Handle exceptions if needed
            e.printStackTrace();
        }
    }

    private static void createProductCall(UserIdProductFull obj) throws IOException {
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

    public static void updateProduct(ProductFull productFull) throws IOException {
        // Create a Callable to perform the network call
        Callable<Void> updateProductCallable = () -> {
            updateProductCall(productFull);
            return null; // Callable requires a return type
        };

        // Wrap the Callable in a FutureTask
        FutureTask<Void> futureTask = new FutureTask<>(updateProductCallable);

        // Start a new thread to execute the FutureTask
        new Thread(futureTask).start();

        // Wait for the task to complete
        try {
            futureTask.get();
        } catch (Exception e) {
            // Handle exceptions if needed
            e.printStackTrace();
        }
    }

    private static void updateProductCall(ProductFull productFull) throws IOException {
        RequestBody requestBody = RequestBody.create(gson.toJson(productFull), JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + UPDATE_PRODUCT_ENDPOINT)
                .put(requestBody)
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }
        }
    }

    public static ProductFull getProduct(long productId) throws IOException {
        // Create a Callable to perform the network call
        Callable<ProductFull> getProductCallable = () -> getProductCall(productId);

        // Wrap the Callable in a FutureTask
        FutureTask<ProductFull> futureTask = new FutureTask<>(getProductCallable);

        // Start a new thread to execute the FutureTask
        new Thread(futureTask).start();

        // Wait for the task to complete and get the result
        try {
            return futureTask.get();
        } catch (Exception e) {
            // Handle exceptions if needed
            e.printStackTrace();
            return null; // Or throw an exception if appropriate
        }
    }

    private static ProductFull getProductCall(long productId) throws IOException {
        String urlWithParams = BASE_URL + GET_PRODUCT_ENDPOINT + "?productId=" + productId;

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
        // Create a Callable to perform the network call
        Callable<Void> deleteProductCallable = () -> {
            deleteProductCall(productId);
            return null; // Callable requires a return type
        };

        // Wrap the Callable in a FutureTask
        FutureTask<Void> futureTask = new FutureTask<>(deleteProductCallable);

        // Start a new thread to execute the FutureTask
        new Thread(futureTask).start();

        // Wait for the task to complete
        try {
            futureTask.get();
        } catch (Exception e) {
            // Handle exceptions if needed
            e.printStackTrace();
        }
    }

    private static void deleteProductCall(long productId) throws IOException {
        String urlWithParams = BASE_URL + DELETE_PRODUCT_ENDPOINT + "?productId=" + productId;

        Request request = new Request.Builder()
                .url(urlWithParams)
                .delete()
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }
        }
    }

}
