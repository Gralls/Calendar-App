package com.springer.patryk.tas_android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

                    Call<User> call=apiService.createUser(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Toast.makeText(mContext,"Register complete",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });

                }

            }
        });

        return rootView;
    }


    public boolean validateInput() {
        boolean validateStatus = true;

        if (!Pattern.matches("^[a-zA-Zsda0-9]{1,10}$", registerLogin.getText().toString())) {
            if (!checkIsEmpty(registerLogin))
                registerLogin.setError("Invalid login format");
            validateStatus = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText().toString()).matches()) {
            if (!checkIsEmpty(registerEmail))
                registerEmail.setError("Invalid e-mail format");
            validateStatus = false;
        }

        if (checkIsEmpty(registerPassword))
            validateStatus = false;
        if (!comparePassword(registerPassword.getText().toString(), registerPasswordConfirm.getText().toString())) {
            if (!checkIsEmpty(registerPasswordConfirm))
                registerPassword.setError("Password is not the same as confirmed password");
            validateStatus = false;
        } else
            registerPassword.setError(null);

        return validateStatus;
    }

    public boolean checkIsEmpty(EditText editText) {
        if (editText.getText().toString().trim().equals("")) {
            editText.setError("This field is required");
            return true;
        }
        return false;
    }

    public boolean comparePassword(String password, String passwordConfirm) {
        return password.trim().equals(passwordConfirm.trim());
    }

}
