package com.example.client.data.api;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class ServerCommunication {
    public final static String BASE_URL = "http://192.168.90.27:5000"; //todo use the current host (not localhost 127.0.0.1)
    public final static MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public static final OkHttpClient CLIENT = new OkHttpClient();
    public static final Gson gson = new Gson();
}
