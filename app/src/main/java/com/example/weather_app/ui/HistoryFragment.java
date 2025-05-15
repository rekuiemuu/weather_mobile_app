package com.example.weather_app.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;

import com.example.weather_app.R;
import com.example.weather_app.db.WeatherDbHelper;
import com.example.weather_app.ui.adapter.HistoryAdapter;
import com.example.weather_app.ui.model.HistoryItem;
import com.example.weather_app.util.Toaster;

import java.text.SimpleDateFormat;
import java.util.*;

public class HistoryFragment extends Fragment {

    private WeatherDbHelper db;
    private HistoryAdapter  adapter;
    private final List<HistoryItem> data = new ArrayList<>();

    @Override public void onCreate(@Nullable Bundle b){
        super.onCreate(b);
        setHasOptionsMenu(true);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inf,@Nullable ViewGroup c,@Nullable Bundle s){
        View v=inf.inflate(R.layout.fragment_history,c,false);

        RecyclerView rv=v.findViewById(R.id.history_recycler);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        db=new WeatherDbHelper(requireContext());

        adapter=new HistoryAdapter(data,(id,pos)->{
            db.delete(id);
            data.remove(pos);
            adapter.notifyItemRemoved(pos);
        });
        rv.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){
            @Override public boolean onMove(@NonNull RecyclerView r,@NonNull RecyclerView.ViewHolder a,@NonNull RecyclerView.ViewHolder b){ return false; }
            @Override public void onSwiped(@NonNull RecyclerView.ViewHolder h,int dir){
                int pos=h.getAdapterPosition();
                db.delete(data.get(pos).id);
                data.remove(pos);
                adapter.notifyItemRemoved(pos);
            }
        }).attachToRecyclerView(rv);

        load();
        return v;
    }

    /** перечитать базу */
    private void load(){
        data.clear();
        Cursor c=db.getAll();
        while(c.moveToNext()){
            long id   =c.getLong (c.getColumnIndexOrThrow(WeatherDbHelper.COL_ID));
            String ct =c.getString(c.getColumnIndexOrThrow(WeatherDbHelper.COL_CITY));
            String tp =c.getString(c.getColumnIndexOrThrow(WeatherDbHelper.COL_TEMP)) + "°C";
            long ts   =c.getLong (c.getColumnIndexOrThrow(WeatherDbHelper.COL_TIME));
            String tm =new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault()).format(new Date(ts));
            data.add(new HistoryItem(id, ct, tp, tm));
        }
        c.close();
        adapter.notifyDataSetChanged();
    }

    @Override public void onResume() {   // <-- добавлено
        super.onResume();
        load();
    }

    @Override public void onCreateOptionsMenu(@NonNull Menu m,@NonNull MenuInflater i){
        i.inflate(R.menu.menu_history, m);
    }
    @Override public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.action_clear){
            db.deleteAll();
            data.clear();
            adapter.notifyDataSetChanged();
            Toaster.successToast(requireContext(),"История очищена");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
