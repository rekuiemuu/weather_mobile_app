package com.example.weather_app.model;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class SavedDailyForecast implements Serializable {
    @PrimaryKey(autoGenerate = true) private int id;

    // просто поля БД, без @SerializedName
    private long date;
    private double minTemp;
    private double maxTemp;
    private double dayTemp;
    private double nightTemp;
    private double eveningTemp;
    private double morningTemp;
    private double feelslikeDay;
    private double feelslikeNight;
    private double feelslikeEve;
    private double feelslikeMorning;
    private int humidity;
    private double wind;
    private String description;
    private int weatherId;
    private String imageUrl;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getDayTemp() {
        return dayTemp;
    }

    public void setDayTemp(double dayTemp) {
        this.dayTemp = dayTemp;
    }

    public double getNightTemp() {
        return nightTemp;
    }

    public void setNightTemp(double nightTemp) {
        this.nightTemp = nightTemp;
    }

    public double getEveningTemp() {
        return eveningTemp;
    }

    public void setEveningTemp(double eveningTemp) {
        this.eveningTemp = eveningTemp;
    }

    public double getMorningTemp() {
        return morningTemp;
    }

    public void setMorningTemp(double morningTemp) {
        this.morningTemp = morningTemp;
    }

    public double getFeelslikeDay() {
        return feelslikeDay;
    }

    public void setFeelslikeDay(double feelslikeDay) {
        this.feelslikeDay = feelslikeDay;
    }

    public double getFeelslikeNight() {
        return feelslikeNight;
    }

    public void setFeelslikeNight(double feelslikeNight) {
        this.feelslikeNight = feelslikeNight;
    }

    public double getFeelslikeEve() {
        return feelslikeEve;
    }

    public void setFeelslikeEve(double feelslikeEve) {
        this.feelslikeEve = feelslikeEve;
    }

    public double getFeelslikeMorning() {
        return feelslikeMorning;
    }

    public void setFeelslikeMorning(double feelslikeMorning) {
        this.feelslikeMorning = feelslikeMorning;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getWind() {
        return wind;
    }

    public void setWind(double wind) {
        this.wind = wind;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
