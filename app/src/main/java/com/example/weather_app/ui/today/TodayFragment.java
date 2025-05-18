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
import com.example.weather_app.model.UviDb;
import com.example.weather_app.util.SharedPreferences;
import com.example.weather_app.util.Utility;
import com.example.weather_app.viewmodel.ForecastViewModel;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TodayFragment extends Fragment {

    /** Hilt-фабрика для ViewModel-ов */
    @Inject ViewModelProvider.Factory viewModelFactory;

    private FragmentHomeBinding binding;          // ViewBinding
    private ForecastViewModel   forecastVm;

    public static TodayFragment create() {        // фабричный метод
        return new TodayFragment();
    }

    /* ───────────────────────────────────────────── */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // цвет статус-бара, как раньше
        requireActivity().getWindow()
                .setStatusBarColor(ContextCompat.getColor(requireContext(),
                        R.color.colorPrimaryToday));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        fetchData();
    }

    /* ───────────────────────────────────────────── */

    private void fetchData() {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        SharedPreferences prefs = SharedPreferences.getInstance(requireContext());
        String city    = prefs.getCity();
        String numDays = prefs.getNumDays();

        forecastVm = new ViewModelProvider(this, viewModelFactory)
                .get(ForecastViewModel.class);

        forecastVm.fetchResults(city, numDays)
                .observe(getViewLifecycleOwner(), result -> {

                    List<SavedDailyForecast> list = result.data;
                    binding.city.setText(Utility.toTitleCase(city));

                    if (list == null || list.isEmpty()) return;

                    SavedDailyForecast d0 = list.get(0);


                    binding.weatherResource.setImageResource(
                            Utility.getArtResourceForConditionText(d0.getDescription())
                    );

                    binding.condition.setText(Utility.toTitleCase(d0.getDescription()));
                    binding.date.setText(String.format("%s, %s",
                            Utility.format(d0.getDate()),
                            Utility.formatDate(d0.getDate())));

                    binding.humidityValue.setText(d0.getHumidity() + "%");
                    Float windValue = (float) d0.getWind();
                    if (windValue != null) {
                        binding.windValue.setText(
                                Utility.getFormattedWind(requireContext(), windValue));
                    } else {
                        binding.windValue.setText(getString(R.string.no_data)); // или "-"
                    }


                    prefs.putStringValue(SharedPreferences.DESC, d0.getDescription());

                    /* температура по времени суток */
                    String showTemp; String feelTemp;
                    if (hour < 12) {                  // утро
                        showTemp = Utility.formatTemperature(requireContext(), d0.getMorningTemp());
                        feelTemp = Utility.formatTemperature(requireContext(), d0.getFeelslikeMorning());
                    } else if (hour < 16) {           // день
                        showTemp = Utility.formatTemperature(requireContext(), d0.getDayTemp());
                        feelTemp = Utility.formatTemperature(requireContext(), d0.getFeelslikeDay());
                    } else if (hour < 21) {           // вечер
                        showTemp = Utility.formatTemperature(requireContext(), d0.getEveningTemp());
                        feelTemp = showTemp; // feels-like равно eveningTemp
                    } else {                          // ночь
                        showTemp = Utility.formatTemperature(requireContext(), d0.getNightTemp());
                        feelTemp = Utility.formatTemperature(requireContext(), d0.getFeelslikeNight());
                    }

                    binding.tempCondition.setText(showTemp);
                    binding.temperature.setText(feelTemp);
                    prefs.putStringValue(SharedPreferences.TEMP, showTemp);
                });
    }
}
