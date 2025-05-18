package com.example.weather_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyForecast {

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("day")
    @Expose
    private Day day;

    public String getDate() {
        return date;
    }

    public Day getDay() {
        return day;
    }
}

