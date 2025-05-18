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
import com.example.weather_app.ui.weekly.WeeklyAdapter.ItemClickListener;
import com.example.weather_app.util.SharedPreferences;
import com.example.weather_app.util.Utility;
import com.example.weather_app.viewmodel.ForecastViewModel;
import dagger.hilt.android.AndroidEntryPoint;
import java.util.Calendar;
import java.util.List;
import javax.inject.Inject;

@AndroidEntryPoint
public class WeeklyFragment extends Fragment implements ItemClickListener {

    private FragmentWeeklyBinding binding;


    private ForecastViewModel forecastViewModel;
    private WeeklyAdapter adapter;

    public static WeeklyFragment create() {
        return new WeeklyFragment();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentWeeklyBinding.inflate(inflater, container, false);
        // Красим статус-бар
        requireActivity().getWindow()
                .setStatusBarColor(
                        ContextCompat.getColor(requireContext(), R.color.weekly_background)
                );

        // Настраиваем RecyclerView
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new WeeklyAdapter(requireContext(), this);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
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
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String city    = SharedPreferences.getInstance(requireContext()).getCity();
        String numDays = SharedPreferences.getInstance(requireContext()).getNumDays();

        binding.city.setText(Utility.toTitleCase(city));

        forecastViewModel = new ViewModelProvider(this)
                .get(ForecastViewModel.class);
        forecastViewModel.fetchResults(city, numDays)
                .observe(getViewLifecycleOwner(), result -> {
                    List<SavedDailyForecast> forecasts = result.data;
                    if (forecasts != null && !forecasts.isEmpty()) {
                        // заполняем список
                        adapter.setForecasts(forecasts);

                        // показываем подробности для первого дня
                        SavedDailyForecast today = forecasts.get(0);
                        binding.weatherResource.setImageResource(
                                Utility.getArtResourceForWeatherCondition(today.getWeatherid())
                        );
                        binding.condition.setText(
                                Utility.toTitleCase(today.getDescription())
                        );
                        binding.date.setText(String.format(
                                "%s, %s",
                                Utility.format(today.getDate()),
                                Utility.formatDate(today.getDate())
                        ));

                        double temp;
                        if (hourOfDay < 12) {
                            temp = today.getMorningTemp();
                        } else if (hourOfDay < 16) {
                            temp = today.getDayTemp();
                        } else if (hourOfDay < 21) {
                            temp = today.getEveningTemp();
                        } else {
                            temp = today.getNightTemp();
                        }
                        binding.tempCondition.setText(
                                Utility.formatTemperature(requireContext(), temp)
                        );

                        // убираем первый элемент из адаптера (либо обновляем список без него)
                        adapter.removeItem(0);
                    }
                });
    }

    @Override
    public void onItemClickListener(int position) {
        // TODO: здесь обработка клика по элементу списка (например, переход на детальную страницу)
    }
}
