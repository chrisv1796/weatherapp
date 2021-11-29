package com.vilapps_weatherapp.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APICall {

    @GET("current.json?key=a40c463d60c24208a8520443212610&q=")
    Call<WeatherModel> getWeather(@Query("q") String latLong);



    @GET("forecast.json?key=a40c463d60c24208a8520443212610&aqi=no&alerts=no")
    //q=&days&aqi=no&alerts=no"
    Call<WeatherModel> getHourAndAstroDetails(
            @Query("q") String latLong,
            @Query("days") int days
    );
}
