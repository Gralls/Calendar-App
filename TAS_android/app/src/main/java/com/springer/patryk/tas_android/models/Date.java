package com.springer.patryk.tas_android.models;

/**
 * Created by Patryk on 2016-11-29.
 */

public class Date {

    private int dayOfMonth;
    private int dayOfWeek;
    private int year;
    private int month;
    private String time;

    public Date(int day_of_month, int day_of_week,int month, int year, String time) {
        this.dayOfMonth = day_of_month;
        this.dayOfWeek = day_of_week;
        this.year = year;
        this.month=month;
        this.time = time;
    }

    public Date(int day_of_month) {
        this.dayOfMonth = day_of_month;
    }

    public String getTime() {
        return time;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month=month;
    }
}
