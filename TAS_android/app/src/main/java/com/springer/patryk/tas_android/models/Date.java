package com.springer.patryk.tas_android.models;

/**
 * Created by Patryk on 2016-11-29.
 */

public class Date {

    private String dayOfMonth;
    private String dayOfWeek;
    private String year;
    private String time;

    public Date(String day_of_month, String day_of_week, String year, String time) {
        this.dayOfMonth = day_of_month;
        this.dayOfWeek = day_of_week;
        this.year = year;
        this.time = time;
    }

    public Date(String day_of_month) {
        this.dayOfMonth = day_of_month;
    }

    public String getTime() {
        return time;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getYear() {
        return year;
    }
}
