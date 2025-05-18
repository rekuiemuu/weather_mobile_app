package com.example.weather_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Condition {

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("icon")
    @Expose
    private String icon;

    public String getText() { return text; }
    public String getIcon() { return icon; }
}

