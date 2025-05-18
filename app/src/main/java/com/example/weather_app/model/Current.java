package com.example.weather_app.model;

import com.google.gson.annotations.SerializedName;

public class Current {
    @SerializedName("temp_c")
    private double tempC;
    @SerializedName("feelslike_c")
    private double feelsLikeC;
    @SerializedName("humidity")
    private int humidity;
    @SerializedName("wind_kph")
    private double windKph;
    @SerializedName("uv")
    private double uv;
    @SerializedName("condition")
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public double getTempC() {
        return tempC;
    }

    public void setTempC(double tempC) {
        this.tempC = tempC;
    }

    public double getFeelsLikeC() {
        return feelsLikeC;
    }

    public void setFeelsLikeC(double feelsLikeC) {
        this.feelsLikeC = feelsLikeC;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWindKph() {
        return windKph;
    }

    public void setWindKph(double windKph) {
        this.windKph = windKph;
    }

    public double getUv() {
        return uv;
    }

    public void setUv(double uv) {
        this.uv = uv;
    }
}
