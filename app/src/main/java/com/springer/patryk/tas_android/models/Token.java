package com.springer.patryk.tas_android.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patryk on 2016-11-06.
 */

public class Token {
    private Boolean success;
    private String token;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Boolean getSuccess() {
        return success;
    }


    public void setSuccess(Boolean success) {
        this.success = success;
    }


    public String getToken() {
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
