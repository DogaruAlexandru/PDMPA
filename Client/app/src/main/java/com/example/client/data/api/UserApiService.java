package com.example.client.data.api;

import com.example.client.data.model.LoggedInUser;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApiService {

    @POST("/login")
    Call<LoggedInUser> login(@Body UserLoginAPI.UserLogging userLogging);

    @POST("/register")
    Call<LoggedInUser> register(@Body UserLoginAPI.UserLogging userLogging);
}
