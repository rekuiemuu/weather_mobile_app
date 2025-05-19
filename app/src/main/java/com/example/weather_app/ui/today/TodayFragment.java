package com.example.weather_app.ui.today;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.weather_app.R;
import com.example.weather_app.databinding.FragmentHomeBinding;
import com.example.weather_app.model.SavedDailyForecast;
import com.example.weather_app.db.UviDb;
import com.example.weather_app.util.SharedPreferences;
import com.example.weather_app.util.Utility;
import com.example.weather_app.viewmodel.ForecastViewModel;
import com.example.weather_app.viewmodel.UviViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import java.util.Calendar;
import java.util.List;

@AndroidEntryPoint
public class TodayFragment extends Fragment {

    private FragmentHomeBinding binding;

    private ForecastViewModel forecastViewModel;
    private UviViewModel uviViewModel;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        requireActivity().getWindow()
                .setStatusBarColor(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimaryToday)
                );
        fetchData();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void fetchData() {
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String city    = SharedPreferences.getInstance(requireContext()).getCity();
        String numDays = SharedPreferences.getInstance(requireContext()).getNumDays();

        binding.city.setText(Utility.toTitleCase(city));

        forecastViewModel = new ViewModelProvider(this)
                .get(ForecastViewModel.class);


        forecastViewModel.fetchResults(city, numDays)
                .observe(getViewLifecycleOwner(), result -> {
                    List<SavedDailyForecast> forecasts = result.data;
                    if (forecasts != null && !forecasts.isEmpty()) {
                        SavedDailyForecast today = forecasts.get(0);
                        showForecast(today, hourOfDay);
                        fetchUvi(today.getLat(), today.getLon());
                    }
                });
    }

    private void showForecast(SavedDailyForecast forecast, int hour) {
        // Icon and description
        binding.weatherResource.setImageResource(
                Utility.getArtResourceForWeatherCondition(forecast.getWeatherid())
        );
        binding.condition.setText(Utility.toTitleCase(forecast.getDescription()));
        binding.date.setText(String.format(
                "%s, %s",
                Utility.format(forecast.getDate()),
                Utility.formatDate(forecast.getDate())
        ));
        // Humidity & wind
        binding.humidityValue.setText(forecast.getHumidity() + "%");
        binding.windValue.setText(
                Utility.getFormattedWind(requireContext(), (float) forecast.getWind())
        );

        // Save description
        SharedPreferences.getInstance(requireContext())
                .putStringValue(SharedPreferences.DESC, forecast.getDescription());

        // Choose which temperature to show
        double temp, feels;
        if (hour < 12) {
            temp  = forecast.getMorningTemp();
            feels = forecast.getFeelslikeMorning();
        } else if (hour < 16) {
            temp  = forecast.getDayTemp();
            feels = forecast.getFeelslikeDay();
        } else if (hour < 21) {
            temp  = forecast.getEveningTemp();
            feels = forecast.getFeelslikeEve();
        } else {
            temp  = forecast.getNightTemp();
            feels = forecast.getFeelslikeNight();
        }

        binding.tempCondition.setText(
                Utility.formatTemperature(requireContext(), temp)
        );
        binding.temperature.setText(
                Utility.formatTemperature(requireContext(), feels)
        );
        SharedPreferences.getInstance(requireContext())
                .putStringValue(
                        SharedPreferences.TEMP,
                        Utility.formatTemperature(requireContext(), temp)
                );
    }

    private void fetchUvi(double lat, double lon) {
        uviViewModel = new ViewModelProvider(this)
                .get(UviViewModel.class);

        uviViewModel.fetchUvi(lat, lon)
                .observe(getViewLifecycleOwner(), result -> {
                    UviDb uvi = result.data;
                    if (uvi != null) {
                        binding.uvValue.setText(String.valueOf(uvi.getValue()));
                    }
                });
    }

}
