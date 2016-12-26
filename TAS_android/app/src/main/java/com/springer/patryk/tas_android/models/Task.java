package com.springer.patryk.tas_android.models;

/**
 * Created by Patryk on 2016-11-29.
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject {

    @PrimaryKey
    @SerializedName("_id")
    private String id;
    private String title;
    private String startDate;

    private String user;
    private String description;

    /**
     * @return The id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The _id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate The startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return The user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user The user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @param guests The guests
     */
    public void setGuests(List<String> guests) {
        List<String> guests1 = guests;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}