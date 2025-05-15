package com.example.weather_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeScreen extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2500; // 2.5 секунды

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Обязательная инициализация Firebase перед использованием FirebaseAuth
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.welcome_screen);

        new Handler().postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // Пользователь уже вошёл — переходим в главное меню
                startActivity(new Intent(this, MainActivity.class));
            } else {
                // Пользователь не вошёл — открываем логин
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, SPLASH_DELAY);
    }
}
