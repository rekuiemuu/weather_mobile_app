package com.example.weather_app.binding;

import android.view.View;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.example.weather_app.R;

public class BindingAdapters {

    /**
     * Управляет видимостью View.
     * Если значение true — показывает, иначе скрывает.
     */
    @BindingAdapter("visibleGone")
    public static void showHide(View view, boolean show) {
        view.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Загружает изображение из URL с помощью Glide.
     * Использует placeholder и центрирование.
     */
    @BindingAdapter("imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.broken_clouds)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.broken_clouds);
        }
    }
}
