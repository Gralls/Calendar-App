package com.springer.patryk.tas_android;

import android.content.Context;
import android.content.SharedPreferences;

import com.springer.patryk.tas_android.models.User;

import java.util.HashMap;

/**
 * Created by Patryk on 10.12.2016.
 */

public class SessionManager {

    private SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context mContext;

    private final String PREF_NAME="Session";
    private final String LOGIN_KEY = "login";
    private final String ID_KEY = "id";
    private final String NAME_KEY = "name";
    private final String EMAIL_KEY = "email";
    private final String TOKEN_KEY = "token";
    private final int PRIVATE_MODE=0;

    public SessionManager(Context mContext) {
        this.mContext = mContext;
        preferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor=preferences.edit();
    }

    public void createSession(User user) {
        editor.putString(ID_KEY, user.getId());
        editor.putString(NAME_KEY, user.getName());
        editor.putString(EMAIL_KEY, user.getEmail());
        editor.putString(LOGIN_KEY, user.getLogin());
        editor.commit();
    }

    public void setToken(String token) {
        editor.putString(TOKEN_KEY, token);
        editor.commit();
    }

    public String getToken(){
        return preferences.getString(TOKEN_KEY, "");
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();

        user.put(ID_KEY, preferences.getString(ID_KEY, null));
        user.put(EMAIL_KEY, preferences.getString(EMAIL_KEY, null));
        user.put(LOGIN_KEY,preferences.getString(LOGIN_KEY,null));
        user.put(NAME_KEY,preferences.getString(NAME_KEY,null));

        return user;
    }
}
