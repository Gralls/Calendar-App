package com.springer.patryk.tas_android.models;

/**
 * Created by Patryk on 2016-11-29.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {

    private String id;
    private String title;
    private String startDate;
    private String endDate;
    private String creatorID;
    private String description;
    private List<String> watchersID = new ArrayList<>();
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
     * @return The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate The endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return The creatorID
     */
    public String getCreatorID() {
        return creatorID;
    }

    /**
     * @param creatorID The creatorID
     */
    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    /**
     * @return The watchersID
     */
    public List<String> getWatchersID() {
        return watchersID;
    }

    /**
     * @param watchersID The watchersID
     */
    public void setWatchersID(List<String> watchersID) {
        this.watchersID = watchersID;
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