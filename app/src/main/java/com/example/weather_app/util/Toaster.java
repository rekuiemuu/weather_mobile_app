package com.example.weather_app.util;

import android.content.Context;
import android.widget.Toast;

public class Toaster {

    public static void successToast(Context context, String msg) {
        Toast.makeText(context, "✅ " + msg, Toast.LENGTH_SHORT).show();
    }

    public static void errorToast(Context context, String msg) {
        Toast.makeText(context, "❌ " + msg, Toast.LENGTH_SHORT).show();
    }

    public static void infoToast(Context context, String msg) {
        Toast.makeText(context, "ℹ️ " + msg, Toast.LENGTH_SHORT).show();
    }
}
