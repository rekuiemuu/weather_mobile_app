package com.example.weather_app.viewmodel;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;


import com.example.weather_app.model.Resource;
import com.example.weather_app.model.UviDb;
import com.example.weather_app.repository.ForecastRepository;

import javax.inject.Inject;

public class UviViewModel extends ViewModel {

    private ForecastRepository forecastRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public UviViewModel(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

}
