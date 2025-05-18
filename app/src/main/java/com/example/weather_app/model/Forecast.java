package com.example.weather_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("forecastday")
    @Expose
    private List<DailyForecast> forecastday;

    public List<DailyForecast> getForecastday() {
        return forecastday;
    }
}
