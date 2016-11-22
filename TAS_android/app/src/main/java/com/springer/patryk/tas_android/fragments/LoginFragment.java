package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.activities.HelloActivity;
import com.springer.patryk.tas_android.activities.MainActivity;
import com.springer.patryk.tas_android.api.ApiEndpoint;
import com.springer.patryk.tas_android.api.RetrofitProvider;
import com.springer.patryk.tas_android.models.Token;
import com.springer.patryk.tas_android.models.User;
import com.springer.patryk.tas_android.utils.InputUtils;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

/**
 * Created by Patryk on 2016-10-21.
 */
public class LoginFragment extends Fragment {

    private Context mContext;
    private ApiEndpoint apiService;

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
        apiService = RetrofitProvider.getRetrofitApiInstance(mContext);
        final Intent intent = new Intent(mContext, MainActivity.class);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    User user = new User();
                    user.setLogin(userLogin.getText().toString());
                    user.setPassword(userPassword.getText().toString());

                    Call<Token> call = apiService.login(user);
                    call.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (response.code() == 200) {
                                startActivity(intent);
                            } else
                                Toast.makeText(mContext, "404 User not found", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
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
}
