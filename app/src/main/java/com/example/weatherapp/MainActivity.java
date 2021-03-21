package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    private final String DEGREE_SYMBOL = "\u00B0";
    TextView currentTempTextView, cityNameTextView, humidityTextView, pressureTextView, uvTextView, windSpeedTextView, feelsLikeTextView, weatherDescriptionTextView, visibilityTextView;
    WeatherModelReceiver receiver;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final String TAG = "MyActivity";
    private ArrayList<Weather> weatherLsit;
    LocationDataService locationDataService = new LocationDataService(this, MainActivity.this);
    int LOCATION_REQUEST_CODE = 1001;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference all textViews Needed
        recyclerView = findViewById(R.id.hourly_recycle_view);
        cityNameTextView = findViewById(R.id.tz_id_text_view);
        currentTempTextView = findViewById(R.id.current_temp_text_view);
        feelsLikeTextView = findViewById(R.id.feels_like_text_view);
        weatherDescriptionTextView = findViewById(R.id.current_details_text_view);
        humidityTextView = findViewById(R.id.humidity_textView);
        pressureTextView = findViewById(R.id.pressure_textView);
        visibilityTextView = findViewById(R.id.visibility_textView);
        uvTextView = findViewById(R.id.uv_text_view);
        windSpeedTextView = findViewById(R.id.wind_speed_textView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (locationDataService.hasLocationPermission() == true) {
                getLastLocation();
            } else {
                requestLocationPermission();
            }
        }

        //HARDCODED TO GET IDEA HOW APP WILL LOOK
        //TODO: Get Real Data From API
        weatherLsit = new ArrayList<>();
        weatherLsit.add(new Weather(10, 6, R.drawable.cloudy_icon));
        weatherLsit.add(new Weather(15, 7, R.drawable.cloudy_icon));
        weatherLsit.add(new Weather(10, 8, R.drawable.cloudy_icon));
        weatherLsit.add(new Weather(60, 9, R.drawable.cloudy_icon));
        weatherLsit.add(new Weather(80, 10, R.drawable.cloudy_icon));
        weatherLsit.add(new Weather(20, 12, R.drawable.cloudy_icon));
        weatherLsit.add(new Weather(13, 12, R.drawable.cloudy_icon));
        setAdapter();
    }

    public void requestLocationPermission() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.d("MainActivity", "Ask Location Permission You Should accept");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    //Gets Last Known Location and passes a string of the lat and long to the getDataFunction
    public void getLastLocation() {
        WeatherModel weatherModel = new WeatherModel();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {

                return;
            }
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    String latitude = df2.format(location.getLatitude()) + ",";
                    String longitude = df2.format(location.getLongitude()) + "";
                    String latLong = latitude + longitude;
                    getDataFromApi(latLong);
                } else if (location == null) {
                    Toast.makeText(MainActivity.this, "Location is Null", Toast.LENGTH_SHORT).show();
                    System.out.println("LOCATION IS INDEED NULL");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //PERMISSION GRANTED
                getLastLocation();
            } else {
                //PERMISSION DENIED
                Toast.makeText(this, "PLEASE GRANT PERMISSION", LENGTH_LONG).show();
            }
        }
    }


    //Gets the current Weather from by passing the latitude and longitude to the retrofit url query
    public void getDataFromApi(String latLong) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APICall apiCall = retrofit.create(APICall.class);
        Call<WeatherModel> call = apiCall.getWeather((latLong));
        System.out.println(call.request().url());
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                String currentTemp = Math.round(response.body().getCurrent().getTempF()) + DEGREE_SYMBOL;
                String city = response.body().getLocation().getName();
                String weatherDescription = response.body().getCurrent().getCondition().getText();
                String feelsLike = Math.round(response.body().getCurrent().getFeelslikeF()) + DEGREE_SYMBOL;
                String humidity = response.body().getCurrent().getHumidity() + "%";
                String pressure = Math.round(response.body().getCurrent().getPressureMb()) + "";
                String uv = response.body().getCurrent().getUv() + "";
                String windSpeedAndDirection = Math.round(response.body().getCurrent().getWindMph()) + " mph " + response.body().getCurrent().getWindDir();
                String visibility = Math.round(response.body().getCurrent().getVisMiles()) + " Mi.";

                updateUI(currentTemp, city, weatherDescription, feelsLike, humidity, pressure, uv, windSpeedAndDirection, visibility);
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
            }
        });
    }

    private void setAdapter() {
        WeatherAdapter adapter = new WeatherAdapter(weatherLsit);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void updateUI(String currentTemp, String cityName, String weatherDescription, String feelsLike, String humidity, String pressure, String uv, String windSpeed, String visibility) {

        currentTempTextView.setText(currentTemp);
        cityNameTextView.setText(cityName);
        feelsLikeTextView.setText(feelsLike);
        weatherDescriptionTextView.setText(weatherDescription);
        humidityTextView.setText(humidity);
        pressureTextView.setText(pressure);
        uvTextView.setText(uv);
        windSpeedTextView.setText(windSpeed);
        visibilityTextView.setText(visibility);
    }


}