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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Patryk on 2016-10-21.
 */

public class RegisterFragment extends Fragment {

    public static final String LOG_TAG = RegisterFragment.class.getSimpleName();

    private Context mContext;
    private boolean registerProceeded = false;
    private SessionManager sessionManager;
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
        sessionManager = new SessionManager(mContext);
        final Intent intent = new Intent(mContext, MainActivity.class);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkIsValidInput()) {
                    final User user = new User();
                    user.setLogin(registerLogin.getText().toString());
                    user.setEmail(registerEmail.getText().toString());
                    user.setPassword(registerPassword.getText().toString());
                    user.setName(registerName.getText().toString());

                    Call<User> call = MyApp.getApiService().createUser(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 409) {
                                Toast.makeText(mContext, "User already exists", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, "User created!", Toast.LENGTH_LONG).show();
                                login(user);
                            }

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


    public boolean checkIsValidInput() {
        boolean validateStatus = true;
        if (!validateLogin()) {
            validateStatus = false;
        }
        if (!validateEmail()) {
            validateStatus = false;
        }
        if (!validatePassword()) {
            validateStatus = false;
        }
        return validateStatus;
    }

    public boolean validateLogin() {
        String login = registerLogin.getText().toString();
        if (InputUtils.checkIsEmpty(login)) {
            registerLogin.setError("Login cannot be empty");
            return false;
        } else if (!InputUtils.checkIsValidLogin(login)) {
            registerLogin.setError("Invalid login format");
            return false;
        }
        registerLogin.setError(null);
        return true;
    }

    public boolean validateEmail() {
        String email = registerEmail.getText().toString();

        if (InputUtils.checkIsEmpty(email)) {
            registerEmail.setError("E-mail cannot be empty");
            return false;
        } else if (!InputUtils.checkIsValidEmail(email)) {
            registerEmail.setError("Invalid e-mail format");
            return false;
        }
        registerLogin.setError(null);
        return true;
    }


    public boolean validatePassword() {
        String password = registerPassword.getText().toString();
        String comparedPassword = registerPasswordConfirm.getText().toString();

        if (!InputUtils.comparePassword(password, comparedPassword)) {
            registerPassword.setError("Passwords are not the same");
            registerPasswordConfirm.setError("Passwords are not the same");
            return false;
        } else if (InputUtils.checkIsEmpty(password)) {
            registerPassword.setError("Password cannot be empty");
            if (InputUtils.checkIsEmpty(comparedPassword)) {
                registerPasswordConfirm.setError("Password cannot be empty");
            }
            return false;
        }
        registerPasswordConfirm.setError(null);
        registerPassword.setError(null);
        return true;
    }

    public void login(User user) {
        Call<Token> call = MyApp.getApiService().login(user);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 200) {
                    sessionManager.setToken(response.body().getToken());
                    getUserDetails(response.body().getToken());
                } else
                    Toast.makeText(mContext, "404 User not found", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUserDetails(String token) {
        Call<User> call = MyApp.getApiService().getUserDetails(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = new User();
                user.setEmail(response.body().getEmail());
                user.setId(response.body().getId());
                user.setLogin(response.body().getLogin());
                user.setName(response.body().getName());
                sessionManager.createSession(user);
                startActivity(new Intent(mContext,MainActivity.class));
                Log.v("Session", sessionManager.getUserDetails().toString());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
