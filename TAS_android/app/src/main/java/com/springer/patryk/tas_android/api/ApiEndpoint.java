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
    Call<List<User>> getUsers(@Header("Authorization") String token,@Query("login") String... login);

    @POST("create-user")
    Call<User> createUser(@Body User user);

    @POST("authorize-user")
    Call<String> login(@Body User user);

    /*@GET("check-user-authorization")
    Call<User> getUserDetails(@Header("Authorization") String token);*/

    @GET("tasks")
    Call<List<Task>> getTasks(@Header("Authorization") String token,@Query("user") String userID);

    @POST("tasks")
    Call<Task> createTask(@Header("Authorization") String token,@Body Task task);

    @DELETE("tasks/{id}")
    Call<Void> deleteTask(@Header("Authorization") String token,@Path("id") String id);

    @PATCH("tasks/{id}")
    Call<Void> editTask(@Header("Authorization") String token,@Path("id") String id, @Body Task task);

    @GET("meetings")
    Call<List<Meeting>> getMeeting(@Header("Authorization") String token,@Query("user") String userID);

    @POST("meetings")
    Call<Void> createMeeting(@Header("Authorization") String token,@Body Meeting meeting);

    @DELETE("meetings/{id}")
    Call<Void> deleteMeeting(@Header("Authorization") String token,@Path("id") String id);

    @PATCH("meetings/{id}")
    Call<Void> editMeeting(@Header("Authorization") String token,@Path("id") String id, @Body Meeting meeting);
}
