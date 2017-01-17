package com.springer.patryk.tas_android.api;

import com.springer.patryk.tas_android.models.Meeting;
import com.springer.patryk.tas_android.models.Task;
import com.springer.patryk.tas_android.models.Token;
import com.springer.patryk.tas_android.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
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
    Call<List<User>> getUsers(@Query("login") String... login);

    @POST("users")
    Call<Void> createUser(@Body User user);

    @POST("authorize-user")
    Call<Token> login(@Body User user);

    @GET("check-user-authorization")
    Call<User> getUserDetails(@Header("Authorization") String token);

    @GET("tasks")
    Call<List<Task>> getTasks(@Query("user") String userID);

    @POST("tasks")
    Call<Task> createTask(@Body Task task);

    @DELETE("tasks/{id}")
    Call<Void> deleteTask(@Path("id") String id);

    @PATCH("tasks/{id}")
    Call<Void> editTask(@Path("id") String id, @Body Task task);

    @GET("meetings")
    Call<List<Meeting>> getMeeting(@Query("user") String userID);

    @POST("meetings")
    Call<Void> createMeeting(@Body Meeting meeting);

    @DELETE("meetings/{id}")
    Call<Void> deleteMeeting(@Path("id") String id);

    @PATCH("meetings/{id}")
    Call<Void> editMeeting(@Path("id") String id, @Body Meeting meeting);
}
