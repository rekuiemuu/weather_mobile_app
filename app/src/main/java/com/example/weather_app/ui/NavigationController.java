package com.example.weather_app.ui;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.weather_app.MainActivity;
import com.example.weather_app.R;
import com.example.weather_app.ui.settings.SettingsFragment;
import com.example.weather_app.ui.today.TodayFragment;
import com.example.weather_app.ui.weekly.WeeklyFragment;

import javax.inject.Inject;

public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(FragmentActivity activity) {
        this.containerId = R.id.nav_host_fragment;
        this.fragmentManager = activity.getSupportFragmentManager();
    }

    public void navigateToHome() {
        String tag = "home";
        TodayFragment homeFragment = new TodayFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, homeFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToWeekly() {
        String tag = "weekly";
        WeeklyFragment weeklyFragment = WeeklyFragment.create();
        fragmentManager.beginTransaction()
                .replace(containerId, weeklyFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }

    public void navigateToSettings() {
        String tag = "settings";
        DialogFragment settingsFragment = SettingsFragment.create();
        settingsFragment.show(fragmentManager, tag);
    }
}