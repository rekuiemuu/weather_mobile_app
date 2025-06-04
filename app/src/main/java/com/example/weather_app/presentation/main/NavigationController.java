package com.example.weather_app.presentation.main;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.weather_app.R;
import com.example.weather_app.presentation.settings.SettingsFragment;
import com.example.weather_app.presentation.today.TodayFragment;
import com.example.weather_app.presentation.weekly.WeeklyFragment;

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
    public void navigateToToday() {
        String tag = "today";
        TodayFragment todayFragment = new TodayFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, todayFragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }


}