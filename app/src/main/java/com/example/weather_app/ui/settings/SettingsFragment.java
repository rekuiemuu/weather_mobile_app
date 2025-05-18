package com.example.weather_app.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.weather_app.MainActivity;
import com.example.weather_app.databinding.SettingsFragmentBinding;
import com.example.weather_app.ui.NavigationController;
import com.example.weather_app.util.AutoClearedValue;
import com.example.weather_app.util.SharedPreferences;

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
    }

    public void didTapCancel(View v) {
        dismiss();
    }

    public void didTapOk(View v) {
        SharedPreferences prefs = SharedPreferences.getInstance(requireContext());

        prefs.putStringValue(SharedPreferences.CITY,
                binding.get().etSettingsCity.getText().toString());
        prefs.putStringValue(SharedPreferences.NUM_DAYS,
                binding.get().etSettingsNumDays.getText().toString());

        startActivity(new Intent(requireContext(), MainActivity.class));
        dismiss();
    }
}
