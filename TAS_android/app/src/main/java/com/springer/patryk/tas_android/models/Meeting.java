package com.springer.patryk.tas_android.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Patryk on 2017-01-02.
 */

public class Meeting extends RealmObject {

    @SerializedName("_id")
    @PrimaryKey
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("startTime")
    private String startTime;

    @SerializedName("place")
    private String place;

    @SerializedName("description")
    private String description;

    @SerializedName("user")
    private String user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
