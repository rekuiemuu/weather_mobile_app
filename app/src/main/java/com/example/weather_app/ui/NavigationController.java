package com.example.weather_app.ui;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.weather_app.R;
import com.example.weather_app.ui.settings.SettingsFragment;
import com.example.weather_app.ui.today.TodayFragment;
import com.example.weather_app.ui.weekly.WeeklyFragment;

public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    public NavigationController(FragmentActivity activity) {
        this.containerId = R.id.nav_host_fragment;
        this.fragmentManager = activity.getSupportFragmentManager(); }

    public void navigateToHome() {
        fragmentManager.beginTransaction()
                .replace(containerId, TodayFragment.create(), "home")
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToWeekly() {
        fragmentManager.beginTransaction()
                .replace(containerId, WeeklyFragment.create(), "weekly")
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToSettings() {
        DialogFragment settingsFragment = SettingsFragment.create();
        settingsFragment.show(fragmentManager, "settings");
    }
}
