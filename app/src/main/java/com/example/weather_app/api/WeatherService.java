package com.example.weather_app.api;


import androidx.lifecycle.LiveData;


import com.example.weather_app.model.Uvi;
import com.example.weather_app.model.WeatherForecast;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {
    @GET("forecast.json") // ✅ стало
    LiveData<ApiResponse<WeatherForecast>> getWeatherForecast(
            @Query("q") String city,
            @Query("days") String numDays,
            @Query("key") String apiKey
    );
}