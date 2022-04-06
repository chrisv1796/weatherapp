package com.vilapps_weatherapp.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.weatherapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DailyWeatherActivity extends AppCompatActivity implements Serializable {
    TextView nameTextView, dateTextView, sunriseTextView, sunsetTextView, windSpeedDirTextView, humidityTextView, visibilityTextView, uvTextView;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);
        int skyBlue = Color.rgb(154, 190, 245);

        nameTextView = findViewById(R.id.city_name_tv);
        dateTextView = findViewById(R.id.date_tv);
        sunriseTextView = findViewById(R.id.sunrise_text_view);
        sunsetTextView = findViewById(R.id.sunset_text_view);
        windSpeedDirTextView = findViewById(R.id.wind_speed_textView);
        visibilityTextView = findViewById(R.id.visibility_textView);
        uvTextView = findViewById(R.id.uv_text_view);
        humidityTextView = findViewById(R.id.humidity_textView);
        LineChart lineChart = findViewById(R.id.line_chart);
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1,4));


        

        View root = dateTextView.getRootView();
        root.setBackgroundColor(skyBlue);

        Intent intent = getIntent();
        DailyWeatherViewModel dailyWeatherObject = (DailyWeatherViewModel) intent.getSerializableExtra("DailyWeatherObject");


        dateTextView.setText(dailyWeatherObject.getDate());
        nameTextView.setText(dailyWeatherObject.getName());
        sunriseTextView.setText(dailyWeatherObject.getSunrise());
        sunsetTextView.setText(dailyWeatherObject.getSunset());
        humidityTextView.setText(dailyWeatherObject.getHumidity().toString());
        windSpeedDirTextView.setText(dailyWeatherObject.getWindSpeedDir().toString());
        uvTextView.setText(dailyWeatherObject.getUv().toString());
        visibilityTextView.setText(dailyWeatherObject.getVisibility().toString());
        System.out.println("this is the temp" + dailyWeatherObject.getHourWeatherlist().get(0).getTempF().toString());
    }
}