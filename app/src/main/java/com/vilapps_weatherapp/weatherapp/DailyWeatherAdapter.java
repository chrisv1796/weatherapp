package com.vilapps_weatherapp.weatherapp;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.MyViewHolder> {
    private OnDailyWeatherClickListener onDailyWeatherClickListener;
    List<DailyWeatherModel> dailyForecastList;
    Context context;

    public DailyWeatherAdapter(List<DailyWeatherModel> dailyForecastList, Context context, OnDailyWeatherClickListener onDailyWeatherClickListener) {
        this.dailyForecastList = dailyForecastList;
        this.onDailyWeatherClickListener = onDailyWeatherClickListener;
        this.context = context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dateTextField;
        private TextView highTemp;
        private TextView lowTemp;
        private TextView chanceOfRain;
        private ImageView weatherIcon;
        OnDailyWeatherClickListener dailyWeatherClickListener;


        public MyViewHolder(View view, OnDailyWeatherClickListener dailyWeatherClickListener) {
            super(view);
            this.dailyWeatherClickListener = dailyWeatherClickListener;
            dateTextField = view.findViewById(R.id.date_daily);
            highTemp = view.findViewById(R.id.high_temp);
            lowTemp = view.findViewById(R.id.low_temp);
            chanceOfRain = view.findViewById(R.id.chance_rain_daily);
            weatherIcon = view.findViewById(R.id.weather_image_view);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            dailyWeatherClickListener.onDailyWeatherClick(getBindingAdapterPosition());

        }
    }

    @NonNull
    @Override
    public DailyWeatherAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_weather, parent,false);
        return new MyViewHolder(itemView, onDailyWeatherClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyWeatherAdapter.MyViewHolder holder, int position) {
        String date = dailyForecastList.get(position).getDate();
        String highTemp = dailyForecastList.get(position).getHighTemp();
        String lowtemp = dailyForecastList.get(position).getLowTemp();
        String chanceOfRain = dailyForecastList.get(position).getPrecipChance();
        String weatherIcon = dailyForecastList.get(position).getWeatherIcon();

        holder.dateTextField.setText(date);
        holder.highTemp.setText(highTemp);
        holder.lowTemp.setText(lowtemp);
        holder.chanceOfRain.setText(chanceOfRain);

        String iconUrl = "https:" + weatherIcon;
        Picasso.get().load(iconUrl).into(holder.weatherIcon);
    }

    public interface OnDailyWeatherClickListener {
        void onDailyWeatherClick(int position);
    }


    @Override
    public int getItemCount() {
        return dailyForecastList.size();
    }
}
