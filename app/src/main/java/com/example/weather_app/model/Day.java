// Day.java
package com.example.weather_app.model;

import com.google.gson.annotations.SerializedName;

public class Day {
    @SerializedName("avgtemp_c")
    private double avgtempC;
    @SerializedName("maxtemp_c")
    private double maxtempC;
    @SerializedName("mintemp_c")
    private double mintempC;
    @SerializedName("humidity")
    private int humidity;
    @SerializedName("condition")
    private Condition condition;

    public double getAvgtempC() { return avgtempC; }
    public void setAvgtempC(double avgtempC) { this.avgtempC = avgtempC; }

    public double getMaxtempC() { return maxtempC; }
    public void setMaxtempC(double maxtempC) { this.maxtempC = maxtempC; }

    public double getMintempC() { return mintempC; }
    public void setMintempC(double mintempC) { this.mintempC = mintempC; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public Condition getCondition() { return condition; }
    public void setCondition(Condition condition) { this.condition = condition; }
}
