package com.example.weather_app.ui.weekly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weather_app.R;
import com.example.weather_app.databinding.FragmentWeeklyBinding;
import com.example.weather_app.model.SavedDailyForecast;
import com.example.weather_app.util.SharedPreferences;
import com.example.weather_app.util.Utility;
import com.example.weather_app.viewmodel.ForecastViewModel;

import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WeeklyFragment extends Fragment implements WeeklyAdapter.ItemClickListener {

    private FragmentWeeklyBinding binding;
    private ForecastViewModel weeklyViewModel;
    private WeeklyAdapter adapter;

    public static WeeklyFragment create() {
        return new WeeklyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWeeklyBinding.inflate(inflater, container, false);

        requireActivity().getWindow().setStatusBarColor(
                ContextCompat.getColor(requireContext(), R.color.weekly_background));

        setupRecyclerView();
        setupViewModelAndFetch();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        adapter = new WeeklyAdapter(requireContext(), this);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.addItemDecoration(
                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    private void setupViewModelAndFetch() {
        weeklyViewModel = new ViewModelProvider(this).get(ForecastViewModel.class);

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        SharedPreferences prefs = SharedPreferences.getInstance(requireContext());
        String city = prefs.getCity();
        String numDays = prefs.getNumDays();

        weeklyViewModel.fetchResults(city, numDays).observe(getViewLifecycleOwner(), result -> {
            List<SavedDailyForecast> list = result.data;
            binding.city.setText(Utility.toTitleCase(city));

            if (list == null || list.isEmpty()) return;

            adapter.setForecasts(list);

            SavedDailyForecast today = list.get(0);
            binding.weatherResource.setImageResource(
                    Utility.getArtResourceForConditionText(today.getDescription())
            );
            binding.condition.setText(Utility.toTitleCase(today.getDescription()));
            binding.date.setText(String.format("%s, %s",
                    Utility.format(today.getDate()),
                    Utility.formatDate(today.getDate())));

            String temp;
            if (hour < 12) {
                temp = Utility.formatTemperature(requireContext(), today.getMorningTemp());
            } else if (hour < 16) {
                temp = Utility.formatTemperature(requireContext(), today.getDayTemp());
            } else if (hour < 21) {
                temp = Utility.formatTemperature(requireContext(), today.getEveningTemp());
            } else {
                temp = Utility.formatTemperature(requireContext(), today.getNightTemp());
            }

            binding.tempCondition.setText(temp);
            adapter.removeItem(0);
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        // обработка клика, если нужно
    }
}
