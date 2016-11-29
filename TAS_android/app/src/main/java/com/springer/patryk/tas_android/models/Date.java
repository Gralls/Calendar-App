package com.springer.patryk.tas_android.models;

/**
 * Created by Patryk on 2016-11-29.
 */

public class Date {
    private  int dayOfMonth;
    private  int dayOfWeek;
    private  int year;


    public Date(int day_of_month, int day_of_week, int year) {
        this.dayOfMonth = day_of_month;
        dayOfWeek = day_of_week;
        this.year = year;
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
}
