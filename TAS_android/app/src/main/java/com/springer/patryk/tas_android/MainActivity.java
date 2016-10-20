package com.springer.patryk.tas_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.springer.patryk.tas_android.api.ApiEndpoint;
import com.springer.patryk.tas_android.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://10.0.2.2:8080/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiEndpoint apiService = retrofit.create(ApiEndpoint.class);
        User user=new User();
        user.setPassword("android");
        user.setEmail("android@gmail.com");
        user.setLogin("androidowiLogin");
        user.setName("androidowyName");
        Call<User> call=apiService.createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Log.d("dasdas","code: " +response.code());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
