package com.example.weather_app.presentation.weekly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app.R;
import com.example.weather_app.domain.model.SavedDailyForecast;
import com.example.weather_app.presentation.util.Utility;

import java.util.Calendar;
import java.util.List;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder> {

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    private List<SavedDailyForecast> forecasts;
    private Context mContext;

    public WeeklyAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.weekly_items, parent, false);

        return new WeeklyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyViewHolder holder, int position) {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        // Determine the values of the wanted data
        SavedDailyForecast forecast = forecasts.get(position);
        Double max_temp = forecast.getMaxTemp();
        Double min_temp = forecast.getMinTemp();
        Long date = forecast.getDate();
        String description = forecast.getDescription();
        int weather_id = forecast.getWeatherid();

        //Set values
        holder.description.setText(description);
        holder.temp.setText(Utility.formatTemperature(mContext, forecast.getMaxTemp()) + "/" + Utility.formatTemperature(mContext, forecast.getMinTemp()));
        int resId = Utility.getIconResourceForWeatherCondition(weather_id);
        if (resId != -1) {
            holder.imageView.setImageResource(resId);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_unknown); // твой запасной ресурс
        }
        holder.day.setText(Utility.format(forecast.getDate()));
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (forecasts == null) {
            return 0;
        }
        return forecasts.size();
    }

    public List<SavedDailyForecast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<SavedDailyForecast> forecastEntities) {
        forecasts = forecastEntities;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        forecasts.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(SavedDailyForecast item, int position) {
        forecasts.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    // Inner class for creating ViewHolders
    public class WeeklyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView day;
        TextView temp;
        TextView description;
        ImageView imageView;

        WeeklyViewHolder(View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.day);
            temp = itemView.findViewById(R.id.temp);
            description = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.weather_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = forecasts.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}

