package com.example.weather_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Forecast {
    @SerializedName("forecastday")
    private List<Forecastday> forecastday;

    public List<Forecastday> getForecastday() {
        return forecastday;
    }
    public void setForecastday(List<Forecastday> forecastday) {
        this.forecastday = forecastday;
    }
}
