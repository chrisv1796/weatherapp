package com.example.weatherapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.internal.$Gson$Preconditions;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {
    List<Hour> horlyWeatherList;
    Context context;

    public WeatherAdapter(List<Hour> hourlyWeatherList, Context context) {
        this.horlyWeatherList = hourlyWeatherList;
        this.context = context;
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
        String time = horlyWeatherList.get(position).getTime() + "";
        String temp = Math.round(horlyWeatherList.get(position).getTempF()) + "";
        String image = horlyWeatherList.get(position).getCondition().getIcon();
        int index = time.indexOf(" ");
        String hour = time.substring(index);
        String imageUrl = "https:" + image;
        Picasso.get().load(imageUrl).into(holder.weatherImageView);


        holder.tempTextView.setText(temp);
        holder.timeTextView.setText(hour);
    }

    @Override
    public int getItemCount() {
        return horlyWeatherList.size();
    }
}
