package com.springer.patryk.tas_android;

import android.app.Application;

import com.springer.patryk.tas_android.api.ApiEndpoint;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patryk on 2016-11-30.
 */

public class MyApp extends Application {

    private static ApiEndpoint retrofit;

    public static final String BASE_URL = "http://10.0.2.2:8080/api/";
    public static final String BASE_URL_DEVICE="http://192.168.0.10:8080/api/";

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiEndpoint.class);

        Realm.init(this);

    }

    public static ApiEndpoint getApiService() {
        return retrofit;
    }

}
