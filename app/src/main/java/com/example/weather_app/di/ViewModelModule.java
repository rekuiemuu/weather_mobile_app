package com.example.weather_app.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.weather_app.viewmodel.ForecastViewModel;
import com.example.weather_app.viewmodel.WeatherViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.multibindings.IntoMap;

@Module
@InstallIn(SingletonComponent.class)
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ForecastViewModel.class)
    abstract ViewModel bindForecastViewModel(ForecastViewModel vm);

    @Binds
    abstract ViewModelProvider.Factory bindFactory(WeatherViewModelFactory factory);
}
