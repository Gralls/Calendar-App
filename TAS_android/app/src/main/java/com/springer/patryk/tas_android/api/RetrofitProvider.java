package com.springer.patryk.tas_android.api;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patryk on 2016-10-21.
 */

public class RetrofitProvider {

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "http://10.0.2.2:8080/api/";

    private RetrofitProvider() {}

    public static ApiEndpoint getRetrofitApiInstance(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiEndpoint.class);
    }
}
