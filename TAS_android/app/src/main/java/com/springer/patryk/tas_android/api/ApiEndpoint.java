package com.springer.patryk.tas_android.api;

import com.springer.patryk.tas_android.models.Task;
import com.springer.patryk.tas_android.models.Token;
import com.springer.patryk.tas_android.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Patryk on 2016-10-19.
 */

public interface ApiEndpoint {

    @GET("users/{id}")
    Call<User> getUser(@Path("id") String id);

    @GET("users")
    Call<User> getUsers();

    @POST("users")
    Call<User> createUser(@Body User user);

    @POST("auth")
    Call<Token> login(@Body User user);

    @GET("checkauth")
    Call<User> getUserDetails(@Header("Authorization") String token);

    @GET("tasks")
    Call<List<Task>> getTasks(@Query("creatorID") String userID);

    @POST("tasks")
    Call<Task>createTask(@Body Task task);

}
