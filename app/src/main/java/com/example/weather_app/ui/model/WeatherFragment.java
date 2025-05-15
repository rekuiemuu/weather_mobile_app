package com.example.weather_app.ui.model;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weather_app.R;
import com.example.weather_app.db.WeatherDbHelper;
import com.example.weather_app.util.Toaster;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherFragment extends Fragment {

    private static final String API_KEY = "d0b4522493b145db855192918251105"; // TODO: move to BuildConfig or secrets
    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private EditText cityInput;
    private TextView nameTv, updatedAtTv, tempTv, minTempTv, maxTempTv, pressureTv, windTv, humidityTv;
    private ImageView conditionIv, searchBtn, micBtn, locationBtn;
    private RequestQueue queue;
    private WeatherDbHelper dbHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        initViews(root);
        initListeners();
        queue = Volley.newRequestQueue(requireContext());
        dbHelper = new WeatherDbHelper(requireContext());
        // загрузим погоду для дефолтного города при первом запуске
        fetchWeather("London");
        return root;
    }

    private void initViews(View root) {
        cityInput    = root.findViewById(R.id.city_et);
        searchBtn    = root.findViewById(R.id.search_bar_iv);
        micBtn       = root.findViewById(R.id.mic_search_id);
        locationBtn  = root.findViewById(R.id.location_btn);
        nameTv       = root.findViewById(R.id.name_tv);
        updatedAtTv  = root.findViewById(R.id.updated_at_tv);
        conditionIv  = root.findViewById(R.id.condition_iv);
        tempTv       = root.findViewById(R.id.temp_tv);
        minTempTv    = root.findViewById(R.id.min_temp_tv);
        maxTempTv    = root.findViewById(R.id.max_temp_tv);
        pressureTv   = root.findViewById(R.id.pressure_tv);
        windTv       = root.findViewById(R.id.wind_tv);
        humidityTv   = root.findViewById(R.id.humidity_tv);
    }

    private void initListeners() {
        searchBtn.setOnClickListener(v -> {
            String city = cityInput.getText().toString().trim();
            if (TextUtils.isEmpty(city)) {
                Toaster.errorToast(requireContext(), "Введите название города");
                return;
            }
            fetchWeather(city);
        });

        locationBtn.setOnClickListener(v -> fetchWeatherByLocation());
    }

    public void searchCity(String city) {
        fetchWeather(city);
    }

    private void fetchWeatherByLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }
        FusedLocationProviderClient fused = LocationServices.getFusedLocationProviderClient(requireContext());
        fused.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                String query = location.getLatitude() + "," + location.getLongitude();
                fetchWeather(query);
            } else {
                Toaster.errorToast(requireContext(), "Не удалось получить местоположение");
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void fetchWeather(String query) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key="
                + API_KEY + "&q=" + query + "&days=1&aqi=no&alerts=no";

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject location = response.getJSONObject("location");
                        JSONObject current  = response.getJSONObject("current");
                        JSONObject day      = response.getJSONObject("forecast")
                                .getJSONArray("forecastday").getJSONObject(0)
                                .getJSONObject("day");

                        /* ---------- UI ---------- */
                        nameTv.setText(location.getString("name"));
                        updatedAtTv.setText(formatTime(current.getLong("last_updated_epoch")));

                        tempTv.setText(String.format(Locale.getDefault(), "%.0f°C",
                                current.getDouble("temp_c")));
                        minTempTv.setText(String.format(Locale.getDefault(), "%.0f°C",
                                day.getDouble("mintemp_c")));
                        maxTempTv.setText(String.format(Locale.getDefault(), "%.0f°C",
                                day.getDouble("maxtemp_c")));

                        pressureTv.setText(current.getInt("pressure_mb") + " mb");
                        windTv.setText(String.format(Locale.getDefault(), "%.0f km/h",
                                current.getDouble("wind_kph")));
                        humidityTv.setText(current.getInt("humidity") + "%");

                        /* ---------- иконка ---------- */
                        String iconUrl = "https:" + current.getJSONObject("condition").getString("icon");
                        Glide.with(requireContext())
                                .load(iconUrl)
                                .placeholder(R.drawable.ic_weather_placeholder)
                                .into(conditionIv);

                        /* ---------- запись в историю ---------- */
                        dbHelper.insert(
                                location.getString("name"),                             // city
                                String.format(Locale.getDefault(), "%.0f",
                                        current.getDouble("temp_c")),                   // temp
                                System.currentTimeMillis()                              // timestamp
                        );

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toaster.errorToast(requireContext(), "Ошибка разбора данных");
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toaster.errorToast(requireContext(), "Ошибка API или сети");
                });

        queue.add(request);
    }

    private String formatTime(long epoch) {
        Date date = new Date(epoch * 1000);
        return new SimpleDateFormat("EEEE HH:mm", Locale.getDefault()).format(date);
    }

    // запрашиваем разрешение и повторяем запрос после выдачи
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchWeatherByLocation();
        } else {
            Toaster.errorToast(requireContext(), "Разрешение на локацию отклонено");
        }
    }
}
