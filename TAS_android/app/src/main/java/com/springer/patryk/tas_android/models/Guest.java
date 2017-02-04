package com.springer.patryk.tas_android.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Patryk on 04.01.2017.
 */

public class Guest extends RealmObject {

    @SerializedName("id")
    String id;
    @SerializedName("flag")
    String flag;
    @SerializedName("login")
    String login;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "Guest{" +
                "id='" + id + '\'' +
                ", flag='" + flag + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
