package com.example.weatherapp;

import com.vilapps_weatherapp.weatherapp.WeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APICall {

    @GET("current.json?key= Enter Your API Key here &q=")
    Call<WeatherModel> getWeather(@Query("q") String latLong);




    @GET("forecast.json?key=  Enter Your API Key Here  &q=&days=1&aqi=no&alerts=no")
    Call<WeatherModel> getHourDetails(@Query("q") String latLong);

}
