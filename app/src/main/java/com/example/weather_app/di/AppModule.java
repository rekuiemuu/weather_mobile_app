package com.example.weather_app.di;

import android.app.Application;
import androidx.room.Room;

import com.example.weather_app.AppExecutors;
import com.example.weather_app.api.WeatherService;
import com.example.weather_app.db.ForecastDao;
import com.example.weather_app.db.WeatherDB;
import com.example.weather_app.util.Constants;
import com.example.weather_app.util.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public final class AppModule {

    @Provides @Singleton
    static AppExecutors provideExecutors() {
        return new AppExecutors();
    }

    @Provides @Singleton
    static WeatherService provideWeatherService() {
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(log)
                .build();

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(client)
                .build()
                .create(WeatherService.class);
    }

    @Provides @Singleton
    static WeatherDB provideDb(Application app) {
        return Room.databaseBuilder(app, WeatherDB.class, "weather.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides @Singleton
    static ForecastDao provideForecastDao(WeatherDB db) {
        return db.forecastDao();
    }
}
