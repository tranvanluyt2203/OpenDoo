package com.example.opendoor.Adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opendoor.R;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private final int itemView;
    private final List<Object> data;

    public WeatherAdapter(int itemView, List<Object> data) {
        this.itemView = itemView;
        this.data = data;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(itemView, parent, false);
        return new WeatherViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        String time = ((List<String>) data.get(0)).get(position);
        double prob = ((List<Double>) data.get(1)).get(position);
        double temp = ((List<Double>) data.get(2)).get(position);
        int iconResId = ((List<Integer>) data.get(3)).get(position);

        // Set data into views
        holder.item_time.setText(time);
        holder.item_prob.setText(String.format("%.0f%%", prob * 100));
        holder.item_temp.setText(String.format("%.0f°C", temp));
        holder.item_img.setImageResource(iconResId);

        if (time.contains("Now") || time.contains("Today")) {
            holder.item.setBackgroundResource(R.drawable.view_item_weather_select);
        } else {
            holder.item.setBackgroundResource(R.drawable.view_item_weather_unselect);
        }
    }

    @Override
    public int getItemCount() {
        return ((List<String>) data.get(0)).size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView item_time, item_prob, item_temp;
        ImageView item_img;
        LinearLayout item;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            item_time = itemView.findViewById(R.id.item_time);
            item_prob = itemView.findViewById(R.id.item_prob);
            item_temp = itemView.findViewById(R.id.item_temp);
            item_img = itemView.findViewById(R.id.item_img);
        }
    }
}
