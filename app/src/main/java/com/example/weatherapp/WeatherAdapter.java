package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {
    private ArrayList<Weather> horlyWeatherList;

    public WeatherAdapter(ArrayList<Weather> hourlyWeatherList) {
        this.horlyWeatherList = hourlyWeatherList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView timeTextView;
        private ImageView weatherImageView;
        private TextView tempTextView;

        public MyViewHolder(View view) {
            super(view);
            timeTextView = view.findViewById(R.id.time_text_view);
            weatherImageView = view.findViewById(R.id.image_view);
            tempTextView = view.findViewById(R.id.temp_text_view);
        }
    }

    @NonNull
    @Override
    public WeatherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_item, parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.MyViewHolder holder, int position) {
        String hour = horlyWeatherList.get(position).getHour() + "";
        int image = horlyWeatherList.get(position).getImg();
        String temp = horlyWeatherList.get(position).getTemp() + "";

        holder.tempTextView.setText(temp);
        holder.weatherImageView.setImageResource(image);
        holder.timeTextView.setText(hour);
    }

    @Override
    public int getItemCount() {
        return horlyWeatherList.size();
    }
}
