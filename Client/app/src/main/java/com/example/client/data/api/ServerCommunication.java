package com.example.client.data.api;

import okhttp3.MediaType;

public interface ServerCommunication {
    String BASE_URL = "localhost:5000";
    MediaType JSON = MediaType.get("application/json; charset=utf-8");
}
