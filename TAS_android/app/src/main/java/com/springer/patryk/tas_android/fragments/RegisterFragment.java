package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.api.ApiEndpoint;
import com.springer.patryk.tas_android.api.RetrofitProvider;
import com.springer.patryk.tas_android.models.User;
import com.springer.patryk.tas_android.utils.InputUtils;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 2016-10-21.
 */

public class RegisterFragment extends Fragment {

    public static final String LOG_TAG=RegisterFragment.class.getSimpleName();

    private ApiEndpoint apiService;
    private Context mContext;
    private boolean registerProceeded = false;

    @BindView(R.id.registerEmail)
    EditText registerEmail;
    @BindView(R.id.registerLogin)
    EditText registerLogin;
    @BindView(R.id.registerPassword)
    EditText registerPassword;
    @BindView(R.id.registerPassowrdConfirm)
    EditText registerPasswordConfirm;
    @BindView(R.id.registerName)
    EditText registerName;
    @BindView(R.id.registerButton)
    Button registerButton;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.register_fragment, container, false);
        mContext = getContext();
        ButterKnife.bind(this, rootView);
        apiService = RetrofitProvider.getRetrofitApiInstance(mContext);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    User user = new User();
                    user.setLogin(registerLogin.getText().toString());
                    user.setEmail(registerEmail.getText().toString());
                    user.setPassword(registerPassword.getText().toString());
                    user.setName(registerName.getText().toString());

                    Call<User> call = apiService.createUser(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 400)
                                Toast.makeText(mContext,"User already exists",Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(mContext,"User created!",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
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

        if (!Pattern.matches("^[a-zA-Zsda0-9]{1,10}$", registerLogin.getText().toString().trim())) {
            if (!InputUtils.checkIsEmpty(registerLogin))
                registerLogin.setError("Invalid login format");
            validateStatus = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText().toString().trim()).matches()) {
            if (!InputUtils.checkIsEmpty(registerEmail))
                registerEmail.setError("Invalid e-mail format");
            validateStatus = false;
        }

        if (InputUtils.checkIsEmpty(registerPassword))
            validateStatus = false;
        if (!InputUtils.checkIsEmpty(registerPasswordConfirm)) {
            if (!comparePassword(registerPassword.getText().toString(), registerPasswordConfirm.getText().toString())) {
                registerPassword.setError("Password is not the same as confirmed password");
                validateStatus = false;
            } else
                registerPassword.setError(null);
        }
        return validateStatus;
    }



    public boolean comparePassword(String password, String passwordConfirm) {
        return password.trim().equals(passwordConfirm.trim());
    }

}
