package com.example.weather_app.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.example.weather_app.MainActivity;
import com.example.weather_app.R;
import com.example.weather_app.databinding.SettingsFragmentBinding;
import com.example.weather_app.ui.NavigationController;
import com.example.weather_app.util.AutoClearedValue;
import com.example.weather_app.util.SharedPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsFragment extends DialogFragment {

    private AutoClearedValue<SettingsFragmentBinding> binding;
    private NavigationController navigationController;

    public static SettingsFragment create() {
        return new SettingsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SettingsFragmentBinding b = SettingsFragmentBinding.inflate(inflater, container, false);
        binding = new AutoClearedValue<>(this, b);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // создаём вручную, как и настроено в проекте
        navigationController = new NavigationController(requireActivity());

        SharedPreferences prefs = SharedPreferences.getInstance(requireContext());
        binding.get().setCity(prefs.getCity());
        binding.get().setNumDays(prefs.getNumDays());
        binding.get().executePendingBindings();


        binding.get().btnSettingsOk.setOnClickListener(v -> didTapOk());
        binding.get().btnSettingsCancel.setOnClickListener(v -> didTapCancel());


    }

    public void didTapOk() {
        SharedPreferences prefs = SharedPreferences.getInstance(requireContext());

        String city = binding.get().etSettingsCity.getText().toString().trim();
        String days = binding.get().etSettingsNumDays.getText().toString().trim();

        prefs.putStringValue(SharedPreferences.CITY, city);
        prefs.putStringValue(SharedPreferences.NUM_DAYS, days);

        dismiss();

        new Handler(Looper.getMainLooper()).post(() -> {
            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_nav);
            bottomNav.setSelectedItemId(R.id.navigation_today);
        });
    }



    public void didTapCancel() {
        dismiss();
    }


}
