package com.example.weather_app.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.weather_app.AppExecutors;
import com.example.weather_app.BuildConfig;
import com.example.weather_app.api.ApiResponse;
import com.example.weather_app.api.WeatherService;
import com.example.weather_app.db.ForecastDao;
import com.example.weather_app.model.Day;
import com.example.weather_app.model.Forecast;
import com.example.weather_app.model.Forecastday;
import com.example.weather_app.model.Resource;
import com.example.weather_app.model.SavedDailyForecast;
// import com.example.weather_app.model.Uvi;
// import com.example.weather_app.model.UviDb;
import com.example.weather_app.model.WeatherForecast;
import com.example.weather_app.util.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class ForecastRepository {

    private final ForecastDao forecastDao;
    private final WeatherService weatherService;
    private final AppExecutors appExecutors;
    private final Context appContext;

    @Inject
    public ForecastRepository(AppExecutors appExecutors,
                              ForecastDao forecastDao,
                              WeatherService weatherService,
                              @ApplicationContext Context appContext) {
        this.appExecutors  = appExecutors;
        this.forecastDao  = forecastDao;
        this.weatherService = weatherService;
        this.appContext    = appContext;
    }

    public LiveData<Resource<List<SavedDailyForecast>>> fetchForecast(String city, String numDays) {
        return new NetworkBoundResource<List<SavedDailyForecast>, WeatherForecast>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                // 1) текущие данные в SharedPreferences
                if (item.getCurrent() != null) {
                    SharedPreferences prefs = SharedPreferences.getInstance(appContext);
                    prefs.getTemp(item.getCurrent().getTempC());
                    prefs.setFeelsLike(item.getCurrent().getFeelslikeC());
                    prefs.setHumidity(item.getCurrent().getHumidity());
                    prefs.setUv(item.getCurrent().getUv());
                    prefs.setWind(item.getCurrent().getWindKph());
                    prefs.setDesc(item.getCurrent().getCondition().getText());
                }

                // 2) ежедневный прогноз в БД
                Forecast forecast = item.getForecast();
                if (forecast != null && forecast.getForecastday() != null) {
                    List<SavedDailyForecast> savedList = new ArrayList<>();
                    for (Forecastday df : forecast.getForecastday()) {
                        Day d = df.getDay();
                        SavedDailyForecast s = new SavedDailyForecast();
                        s.setDate(df.getDateEpoch());
                        s.setDayTemp(d.getAvgtempC());
                        s.setMaxTemp(d.getMaxtempC());
                        s.setMinTemp(d.getMintempC());
                        s.setHumidity(d.getHumidity());
                        s.setDescription(d.getCondition().getText());
                        s.setImageUrl(d.getCondition().getIcon());
                        savedList.add(s);
                    }
                    forecastDao.insertForecastList(savedList);
                }
            }


            @Override
            protected boolean shouldFetch(@Nullable List<SavedDailyForecast> data) {
                // всегда фетчим при заходе
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<SavedDailyForecast>> loadFromDb() {
                return forecastDao.loadForecast();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<WeatherForecast>> createCall() {
                return weatherService.getWeatherForecast(
                        city,
                        numDays,
                        BuildConfig.WEATHER_API_KEY
                );
            }

            @Override
            protected void onFetchFailed() {
                // здесь можно логировать ошибку
            }
        }.asLiveData();
    }

    /*
    // Если потребуется UV-index:
    public LiveData<Resource<UviDb>> fetchUvi(Double lat, Double lon) {
        return new NetworkBoundResource<UviDb, Uvi>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Uvi item) {
                forecastDao.deleteUvi();
                if (item != null) {
                    UviDb uviDb = new UviDb();
                    uviDb.setLat(item.getLat());
                    uviDb.setLon(item.getLon());
                    uviDb.setValue(item.getValue());
                    forecastDao.insertUvi(uviDb);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable UviDb data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<UviDb> loadFromDb() {
                return forecastDao.loadUvi();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Uvi>> createCall() {
                return weatherService.getUvi(lat, lon, BuildConfig.WEATHER_API_KEY);
            }

            @Override
            protected void onFetchFailed() { }
        }.asLiveData();
    }
    */

}
