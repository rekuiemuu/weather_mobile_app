package com.example.weather_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Day {

    @SerializedName("maxtemp_c")
    @Expose
    private float maxTemp;

    @SerializedName("mintemp_c")
    @Expose
    private float minTemp;

    @SerializedName("avgtemp_c")
    @Expose
    private float avgTemp;

    @SerializedName("humidity")
    @Expose
    private float humidity;

    @SerializedName("condition")
    @Expose
    private Condition condition;

    public float getMaxTemp() { return maxTemp; }
    public float getMinTemp() { return minTemp; }
    public float getAvgTemp() { return avgTemp; }
    public float getHumidity() { return humidity; }
    public Condition getCondition() { return condition; }
}

