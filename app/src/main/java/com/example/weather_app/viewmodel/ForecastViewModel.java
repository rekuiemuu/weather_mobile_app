package com.example.weather_app.viewmodel;


import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather_app.model.Resource;
import com.example.weather_app.model.SavedDailyForecast;
import com.example.weather_app.model.UviDb;
import com.example.weather_app.repository.ForecastRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ForecastViewModel extends ViewModel {
    private final ForecastRepository repository;

    @Inject
    public ForecastViewModel(ForecastRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<SavedDailyForecast>>> fetchResults(String city, String numDays) {
        return repository.fetchForecast(city, numDays);
    }
}


