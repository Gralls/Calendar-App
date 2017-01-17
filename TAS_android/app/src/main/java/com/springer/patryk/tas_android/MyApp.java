package com.springer.patryk.tas_android;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.springer.patryk.tas_android.adapters.GuestAdapter;
import com.springer.patryk.tas_android.api.ApiEndpoint;
import com.springer.patryk.tas_android.models.Guest;

import net.danlew.android.joda.JodaTimeAndroid;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Patryk on 2016-11-30.
 */

public class MyApp extends Application {

    private static ApiEndpoint retrofit;

    public static final String BASE_URL = "https://ira-project.herokuapp.com/api/";
    public static final String BASE_URL_DEVICE="http://192.168.43.52:8080/api/";

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Guest.class, new GuestAdapter())
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiEndpoint.class);

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("tas.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded() // todo remove for production
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static ApiEndpoint getApiService() {
        return retrofit;
    }

}
