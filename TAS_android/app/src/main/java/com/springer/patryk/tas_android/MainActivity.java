package com.springer.patryk.tas_android;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.springer.patryk.tas_android.api.ApiEndpoint;
import com.springer.patryk.tas_android.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private Retrofit retrofit;

    @BindView(R.id.registerEmail)
    EditText registerEmail;
    @BindView(R.id.registerLogin)
    EditText registerLogin;
    @BindView(R.id.registerPassowrd)
    EditText registerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
         retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    public void confirmRegister(View view) {
        ApiEndpoint apiService = retrofit.create(ApiEndpoint.class);
        User user=new User();
        user.setLogin(registerLogin.getText().toString());
        if (Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText().toString()).matches()) {
            user.setEmail(registerEmail.getText().toString());
        }
        else
            registerEmail.setTextColor(Color.RED);
        user.setPassword(registerPassword.getText().toString());
        user.setName("name");

        Call<User> call=apiService.createUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
