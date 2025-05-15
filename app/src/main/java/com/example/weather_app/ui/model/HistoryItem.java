package com.example.weather_app.ui.model;

public class HistoryItem {
    public long   id;
    public String city;
    public String temp;
    public String time;
    public HistoryItem(long id,String city,String temp,String time){
        this.id=id; this.city=city; this.temp=temp; this.time=time;
    }
}
