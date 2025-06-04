package com.example.weather_app.presentation.viewmodel;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather_app.domain.model.Resource;
import com.example.weather_app.data.local.db.UviDb;
import com.example.weather_app.data.repository.ForecastRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class UviViewModel extends ViewModel {

    private final ForecastRepository forecastRepository;

    @Inject
    public UviViewModel(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

    @VisibleForTesting
    public LiveData<Resource<UviDb>> fetchUvi(Double lat, Double lon) {
        return forecastRepository.fetchUvi(lat, lon);
    }
}
