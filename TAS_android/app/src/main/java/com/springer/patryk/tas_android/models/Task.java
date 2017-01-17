package com.springer.patryk.tas_android.models;

/**
 * Created by Patryk on 2016-11-29.
 */

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {

    @PrimaryKey
    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("startDate")
    private String startDate;
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("user")
    private String user;
    @SerializedName("description")
    private String description;
    @SerializedName("guests")
    private RealmList<Guest> guests;


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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public RealmList<Guest> getGuests() {
        return guests;
    }

    public void setGuests(RealmList<Guest> guests) {
        this.guests = guests;
    }
}