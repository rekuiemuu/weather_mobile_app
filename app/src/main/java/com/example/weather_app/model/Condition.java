// Condition.java
package com.example.weather_app.model;

import com.google.gson.annotations.SerializedName;

public class Condition {
    @SerializedName("text")
    private String text;
    @SerializedName("icon")
    private String icon;
    @SerializedName("code")
    private int code;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
}
