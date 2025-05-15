package com.example.weather_app;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.weather_app.databinding.ActivityMainBinding;
import com.example.weather_app.ui.HistoryFragment;
import com.example.weather_app.ui.model.WeatherFragment;
import com.example.weather_app.util.PermissionUtils;
import com.example.weather_app.util.Toaster;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final int REQUEST_CODE_MIC_INPUT = 101;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean granted = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION)) ||
                        Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_COARSE_LOCATION));
                if (granted) {
                    showWeatherFragment();
                } else {
                    Toaster.errorToast(this, "Разрешение на локацию отклонено");
                    finish();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (PermissionUtils.hasLocationPermissions(this)) {
            showWeatherFragment();
        } else {
            permissionLauncher.launch(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            });
        }

        initListeners();
    }

    private void initListeners() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_weather) {
                showWeatherFragment();
                return true;
            } else if (id == R.id.nav_history) {
                showHistoryFragment();
                return true;
            }
            return false;
        });
    }

    private void showWeatherFragment() {
        replaceFragment(new WeatherFragment());
    }

    private void showHistoryFragment() {
        replaceFragment(new HistoryFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionUtils.hasInternet(this)) {
            Toaster.errorToast(this, "Проверьте подключение к интернету");
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void startVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Назовите город");
        try {
            startActivityForResult(intent, REQUEST_CODE_MIC_INPUT);
        } catch (Exception e) {
            Log.e("VoiceSearch", "Ошибка микрофона", e);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MIC_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String city = results.get(0).toUpperCase();
                Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
                if (current instanceof WeatherFragment) {
                    ((WeatherFragment) current).searchCity(city);
                }
            }
        }
    }
}
