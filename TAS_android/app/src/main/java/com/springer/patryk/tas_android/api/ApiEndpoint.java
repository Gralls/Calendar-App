package com.springer.patryk.tas_android.api;

import com.springer.patryk.tas_android.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
}
