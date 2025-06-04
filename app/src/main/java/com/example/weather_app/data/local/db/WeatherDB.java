package com.example.weather_app.data.local.db;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.weather_app.domain.model.SavedDailyForecast;


@Database(entities = {SavedDailyForecast.class, UviDb.class},
        version = 1,
        exportSchema = false)

public abstract class WeatherDB extends RoomDatabase {
    abstract public ForecastDao forecastDao();
}
