package com.example.weather_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeatherForecast {

    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("forecast")
    @Expose
    private Forecast forecast;

    public Location getLocation() {
        return location;
    }

    public Forecast getForecast() {
        return forecast;
    }
}

