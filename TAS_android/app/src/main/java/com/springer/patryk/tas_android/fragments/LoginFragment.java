package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.springer.patryk.tas_android.MyApp;
import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.SessionManager;
import com.springer.patryk.tas_android.activities.MainActivity;
import com.springer.patryk.tas_android.models.Token;
import com.springer.patryk.tas_android.models.User;
import com.springer.patryk.tas_android.utils.InputUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 2016-10-21.
 */
public class LoginFragment extends Fragment {

    private Context mContext;
    private SessionManager sessionManager;
    @BindView(R.id.login)
    EditText userLogin;
    @BindView(R.id.password)
    EditText userPassword;
    @BindView(R.id.loginButton)
    Button loginButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.login_fragment, container, false);
        mContext = getContext();
        ButterKnife.bind(this, rootView);
        sessionManager = new SessionManager(mContext);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    final User user = new User();
                    user.setLogin(userLogin.getText().toString());
                    user.setPassword(userPassword.getText().toString());

                    Call<String> call = MyApp.getApiService().login(user);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.code() == 200) {
                                sessionManager.setToken("Bearer "+response.body());
                                getUserDetails(sessionManager.getToken(),user.getLogin());
                            } else
                                Toast.makeText(mContext, "Bad credentials", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        return rootView;
    }

    public boolean validateInput() {
        boolean validateStatus = true;
        if (InputUtils.checkIsEmpty(userLogin.getText().toString())) {
            userLogin.setError("This field is required");
            validateStatus = false;
        }
        if (InputUtils.checkIsEmpty(userPassword.getText().toString())) {
            userPassword.setError("This field is required");
            validateStatus = false;
        }
        return validateStatus;
    }

    void getUserDetails(String token,String login) {
        Call<List<User>> call = MyApp.getApiService().getUsers(token,login);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                sessionManager.createSession(response.body().get(0));
                startActivity(new Intent(mContext,MainActivity.class));
                Log.v("Session", sessionManager.getUserDetails().toString());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }
}
