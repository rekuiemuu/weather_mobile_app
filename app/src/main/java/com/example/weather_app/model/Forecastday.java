package com.example.weather_app.model;

import com.google.gson.annotations.SerializedName;

public class Forecastday {
    // Это поле date_epoch из JSON
    @SerializedName("date_epoch")
    private long dateEpoch;

    // Вложенный объект day
    @SerializedName("day")
    private Day day;

    public long getDateEpoch() {
        return dateEpoch;
    }
    public void setDateEpoch(long dateEpoch) {
        this.dateEpoch = dateEpoch;
    }

    public Day getDay() {
        return day;
    }
    public void setDay(Day day) {
        this.day = day;
    }
}

