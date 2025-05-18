package com.example.weather_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("region")
    @Expose
    private String region;

    @SerializedName("country")
    @Expose
    private String country;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lon")
    @Expose
    private double lon;

    public String getName() { return name; }

    public String getRegion() { return region; }

    public String getCountry() { return country; }

    public double getLat() { return lat; }

    public double getLon() { return lon; }
}
