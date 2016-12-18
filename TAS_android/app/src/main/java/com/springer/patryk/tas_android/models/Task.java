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

public class Task implements Serializable {

    @SerializedName("_id")
    private String id;
    private String title;
    private String startDate;

    private String user;
    private String description;

    private List<String> guests = new ArrayList<>();
    private Map<String, Object> additionalProperties = new HashMap<>();

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
     * @return The guests
     */
    public List<String> getGuests() {
        return guests;
    }

    /**
     * @param guests The guests
     */
    public void setGuests(List<String> guests) {
        this.guests = guests;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.lang.String getDescription() {
        return description;
    }
}