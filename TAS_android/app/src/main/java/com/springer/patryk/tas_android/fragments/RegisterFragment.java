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
import android.widget.TextView;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.api.ApiEndpoint;
import com.springer.patryk.tas_android.api.RetrofitProvider;
import com.springer.patryk.tas_android.models.User;
import com.springer.patryk.tas_android.utils.TextValidation;

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

    @BindView(R.id.registerEmail)
    EditText registerEmail;
    @BindView(R.id.registerLogin)
    EditText registerLogin;
    @BindView(R.id.registerPassword)
    EditText registerPassword;
    @BindView(R.id.registerButton)
    Button registerButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.register_fragment, container, false);
        mContext=getContext();
        ButterKnife.bind(this,rootView);
        apiService = RetrofitProvider.getRetrofitApiInstance(mContext);
        registerEmail.addTextChangedListener(new TextValidation(registerEmail) {
            @Override
            public void validate(TextView textView, String text) {
                if (Patterns.EMAIL_ADDRESS.matcher(registerEmail.getText().toString()).matches()) {

                }
                else if(registerEmail.getText().toString().equals(""))
                    registerEmail.setError("This field is required");
                else
                    registerEmail.setError("Wrong email format");
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user=new User();
                user.setLogin(registerLogin.getText().toString());

                user.setPassword(registerPassword.getText().toString());


//        Call<User> call=apiService.createUser(user);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });
            }
        });

        return rootView;
    }

    public void confirmRegister(View view) {


    }
}
