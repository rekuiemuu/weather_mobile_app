package com.example.weather_app.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.weather_app.AppExecutors;
import com.example.weather_app.BuildConfig;
import com.example.weather_app.api.ApiResponse;
import com.example.weather_app.api.WeatherService;
import com.example.weather_app.db.ForecastDao;
import com.example.weather_app.model.Resource;
import com.example.weather_app.model.SavedDailyForecast;
import com.example.weather_app.model.Uvi;
import com.example.weather_app.model.UviDb;
import com.example.weather_app.model.WeatherForecast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ForecastRepository {

    private final ForecastDao forecastDao;
    private final WeatherService weatherService;
    private final AppExecutors appExecutors;

    @Inject
    public ForecastRepository(AppExecutors appExecutors,
                              ForecastDao forecastDao,
                              WeatherService weatherService) {
        this.appExecutors = appExecutors;
        this.forecastDao = forecastDao;
        this.weatherService = weatherService;
    }

    public LiveData<Resource<List<SavedDailyForecast>>> fetchForecast(String city, String numDays) {
        return new NetworkBoundResource<List<SavedDailyForecast>, WeatherForecast>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull WeatherForecast item) {
                forecastDao.deleteNewsTable();

                if (item.getForecast() != null && item.getForecast().getForecastday() != null) {
                    List<SavedDailyForecast> saved = new ArrayList<>();

                    for (com.example.weather_app.model.DailyForecast df : item.getForecast().getForecastday()) {
                        SavedDailyForecast s = new SavedDailyForecast();
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date parsedDate = sdf.parse(df.getDate());
                            if (parsedDate != null) {
                                s.setDate(parsedDate.getTime()); // это long
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            s.setDate(0L); // fallback, если ошибка
                        }

                        s.setDayTemp(df.getDay().getAvgTemp());
                        s.setMaxTemp(df.getDay().getMaxTemp());
                        s.setMinTemp(df.getDay().getMinTemp());
                        s.setHumidity((int) df.getDay().getHumidity());
                        s.setDescription(df.getDay().getCondition().getText());
                        s.setImageUrl(df.getDay().getCondition().getIcon());
                        saved.add(s);
                    }

                    forecastDao.insertForecastList(saved);
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<SavedDailyForecast> data) {
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
                // лог или обработка
            }
        }.asLiveData();
    }

//    public LiveData<Resource<UviDb>> fetchUvi(Double lat, Double lon) {
//        final Double finalLat = lat;
//        final Double finalLon = lon;
//
//        return new NetworkBoundResource<UviDb, Uvi>(appExecutors) {
//
//            @Override
//            protected void saveCallResult(@NonNull Uvi item) {
//                forecastDao.deleteUvi();
//                if (item != null) {
//                    UviDb uviDb = new UviDb();
//                    uviDb.setLat(item.getLat());
//                    uviDb.setLon(item.getLon());
//                    uviDb.setValue(item.getValue());
//                    forecastDao.insertUvi(uviDb);
//                }
//            }
//
//            @Override
//            protected boolean shouldFetch(@Nullable UviDb data) {
//                return true;
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<UviDb> loadFromDb() {
//                return forecastDao.loadUvi();
//            }
//
//            @NonNull
//            @Override
//            protected LiveData<ApiResponse<Uvi>> createCall() {
//                return weatherService.getUvi(finalLat, finalLon, BuildConfig.WEATHER_API_KEY);
//            }
//
//            @Override
//            protected void onFetchFailed() {
//                // лог ошибок
//            }
//
//        }.asLiveData();
//    }

}

