package com.example.weather_app.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weather_app.R;
import com.example.weather_app.ui.model.HistoryItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder>{

    public interface OnDeleteClick{ void onDelete(long id,int pos); }
    private final List<HistoryItem> list;
    private final OnDeleteClick cb;

    public HistoryAdapter(List<HistoryItem> list, OnDeleteClick cb){
        this.list=list; this.cb=cb;
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p,int v){
        View v2= LayoutInflater.from(p.getContext()).inflate(R.layout.item_history,p,false);
        return new Holder(v2);
    }
    @Override public void onBindViewHolder(@NonNull Holder h,int i){
        HistoryItem it=list.get(i);
        h.city.setText(it.city);
        h.temp.setText(it.temp);
        h.time.setText(it.time);
        h.del.setOnClickListener(v-> cb.onDelete(it.id, h.getAdapterPosition()));
    }
    @Override public int getItemCount(){ return list.size(); }

    static class Holder extends RecyclerView.ViewHolder{
        TextView city,temp,time; ImageView del;
        Holder(View v){
            super(v);
            city=v.findViewById(R.id.city_tv);
            temp=v.findViewById(R.id.temp_tv);
            time=v.findViewById(R.id.time_tv);
            del =v.findViewById(R.id.delete_iv);
        }
    }
}
