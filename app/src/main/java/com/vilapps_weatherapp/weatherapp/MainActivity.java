package com.vilapps_weatherapp.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements DailyWeatherAdapter.OnDailyWeatherClickListener, Serializable {
    private static final DecimalFormat df2 = new DecimalFormat("#.##");
    private static String latLong = "";
    private final String DEGREE_SYMBOL = "\u00B0";
    TextView currentTempTextView, cityNameTextView, humidityTextView, pressureTextView, uvTextView, windSpeedTextView, feelsLikeTextView, visibilityTextView, sunriseTextView, sunsetTextView;
    ImageView mainImageView;
    Button addLocationButton, fiveDayForecastBttn, hourlyForecastBttn, tenDayForecastBttn, mapButtonImageView;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationDataService locationDataService = new LocationDataService(this, MainActivity.this);
    List<Hour> weatherList;
    int LOCATION_REQUEST_CODE = 1001;
    SharedPreferences sharedPref;
    private AlertDialog dialog;
    private RecyclerView recyclerView;
    private Object DailyWeatherModel;
    //List for the recyclerView for daily weather Forecast
    private List<DailyWeatherModel> dailyWeatherForecast = new ArrayList<>();
    //List to save object to send to other activity
    private List<DailyWeatherViewModel> dailyWeatherView = new ArrayList<>();
    int skyBlue = Color.rgb(154, 190, 245);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up ad
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-4317081344586319/8457839945");

        MobileAds.initialize(this, initializationStatus -> {
        });
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //Reference all textViews Needed
        recyclerView = findViewById(R.id.hourly_recycle_view);
        sunriseTextView = findViewById(R.id.sunrise_text_view);
        sunsetTextView = findViewById(R.id.sunset_text_view);
        cityNameTextView = findViewById(R.id.tz_id_text_view);
        currentTempTextView = findViewById(R.id.current_temp_text_view);
        feelsLikeTextView = findViewById(R.id.feels_like_text_view);
        humidityTextView = findViewById(R.id.humidity_textView);
        pressureTextView = findViewById(R.id.pressure_textView);
        visibilityTextView = findViewById(R.id.visibility_textView);
        uvTextView = findViewById(R.id.uv_text_view);
        windSpeedTextView = findViewById(R.id.wind_speed_textView);
        mainImageView = findViewById(R.id.weather_image_view);
        addLocationButton = findViewById(R.id.location_button);
        addLocationButton.setVisibility(View.INVISIBLE);
        fiveDayForecastBttn = findViewById(R.id.five_day_forecast_button);
        hourlyForecastBttn = findViewById(R.id.hourly_forecast_button);
        tenDayForecastBttn = findViewById(R.id.ten_day_forecast_button);
        mapButtonImageView = findViewById(R.id.map_image_view);
        mapButtonImageView.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (locationDataService.hasLocationPermission() == true) {
                getLastLocation();
            } else {
                requestLocationPermission();
            }
        }

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);

        addLocationButton.setOnClickListener(v -> requestLocationPermission());

        hourlyForecastBttn.setOnClickListener(v -> getHourDataFromAPI(latLong));

        fiveDayForecastBttn.setOnClickListener(v -> getDailyForecastFromApi(latLong, 7));

        tenDayForecastBttn.setOnClickListener(v -> getDailyForecastFromApi(latLong, 10));

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
        } if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    String latitude = df2.format(location.getLatitude()) + ",";
                    String longitude = df2.format(location.getLongitude()) + "";
                    latLong = latitude + longitude;

                    //Methods To Get Data
                    getHourDataFromAPI(latLong);
                    getDataFromApi(latLong);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("this is the exception" + e);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //PERMISSION GRANTED
                getLastLocation();
            } else {
                //PERMISSION DENIED
                addLocationButton.setVisibility(View.VISIBLE);
                if (sharedPref.getString("user_id", "").matches("")) {
                    zipPopUpView();
                } else {
                    String zip = sharedPref.getString("user_id", "");
                    getDataFromApi(zip);
                    getHourDataFromAPI(zip);
                }
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
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                System.out.println("IT IS GETTING THE DATA");
                String currentTemp = Math.round(response.body().getCurrent().getTempF()) + DEGREE_SYMBOL;
                String city = response.body().getLocation().getName();
                String weatherDescription = response.body().getCurrent().getCondition().getText();
                String feelsLike = Math.round(response.body().getCurrent().getFeelslikeF()) + DEGREE_SYMBOL;
                String humidity = response.body().getCurrent().getHumidity() + "%";
                String pressure = Math.round(response.body().getCurrent().getPressureMb()) + "";
                String uv = response.body().getCurrent().getUv() + "";
                String windSpeedAndDirection = Math.round(response.body().getCurrent().getWindMph()) + " mph " + response.body().getCurrent().getWindDir();
                String visibility = Math.round(response.body().getCurrent().getVisMiles()) + " Mi.";
                int isDay = response.body().getCurrent().getIsDay();
                updateUI(currentTemp, city, weatherDescription, feelsLike, humidity, pressure, uv, windSpeedAndDirection, visibility, isDay);
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
            }
        });
    }


    public void getHourDataFromAPI(String latlong) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APICall apiCall = retrofit.create(APICall.class);
        Call<WeatherModel> call = apiCall.getHourAndAstroDetails(latlong, 1);
        System.out.println(call.request().url());
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                weatherList = response.body().getForecast().getForecastday().get(0).getHour();
                setAdapter(weatherList);
                String sunriseTime = response.body().getForecast().getForecastday().get(0).getAstro().getSunrise();
                String sunsetTime = response.body().getForecast().getForecastday().get(0).getAstro().getSunset();
                sunriseTextView.setText(sunriseTime);
                sunsetTextView.setText(sunsetTime);
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
            }
        });
    }

    private void setAdapter(List<Hour> list) {
        WeatherAdapter adapter = new WeatherAdapter(list, MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        int currentTime = Calendar.getInstance().getTime().getHours();
        recyclerView.scrollToPosition(currentTime + 1);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        recyclerView.setAdapter(adapter);
    }

    private void updateUI(String currentTemp, String cityName, String weatherDescription, String feelsLike,
                          String humidity, String pressure, String uv, String windSpeed, String visibility, int isDay) {
        View root = mainImageView.getRootView();
        currentTempTextView.setText(currentTemp);
        cityNameTextView.setText(cityName);
        feelsLikeTextView.setText(feelsLike);
        humidityTextView.setText(humidity);
        pressureTextView.setText(pressure);
        uvTextView.setText(uv);
        windSpeedTextView.setText(windSpeed);
        visibilityTextView.setText(visibility);
        if (isDay == 1) {
            root.setBackgroundColor(skyBlue);
            mainImageView.setImageResource(R.drawable.sunny_background);
            mainImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            root.setBackgroundColor(getColor(R.color.dark_sky_blue));
            mainImageView.setImageResource(R.drawable.nighttime_image);
            mainImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private void zipPopUpView() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View addNewZipView = getLayoutInflater().inflate(R.layout.zip_pop_up, null);
        dialogBuilder.setView(addNewZipView);
        dialog = dialogBuilder.create();
        dialog.show();

        EditText zipEditText = dialog.findViewById(R.id.zip_edit_text);
        Button addZipCodeButton = dialog.findViewById(R.id.add_zip_button);
        addZipCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zip = zipEditText.getText().toString();
                sharedPref.edit().putString("user_id", zip).apply();
                dialog.dismiss();
                getDataFromApi(zip);
                getHourDataFromAPI(zip);
            }
        });
    }

    public void getDailyForecastFromApi(String latlong, int days) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherapi.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APICall apiCall = retrofit.create(APICall.class);
        Call<WeatherModel> call = apiCall.getHourAndAstroDetails(latlong, days);
        System.out.println(call.request().url());
        System.out.println("This is LatLong" + latlong);
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                List<Forecastday> daysForecast = response.body().getForecast().getForecastday();
                System.out.println("This is the size of daysForecast List" + daysForecast.size());

                String date = "";
                for (int i = 0; i < daysForecast.size(); i++) {
                    if (i == 0) {
                        date = "Today";
                    }else {
                        String fullDate = daysForecast.get(i).getDate();
                        date = fullDate.substring(5);
                    }
                    //String fullDate = daysForecast.get(i).getDate();
                    String highTemp = Math.round(daysForecast.get(i).getDay().getMaxtempF()) + DEGREE_SYMBOL;
                    String lowTemp = Math.round(daysForecast.get(i).getDay().getMintempF()) + DEGREE_SYMBOL;
                    String precipChance = daysForecast.get(i).getDay().getDailyChanceOfRain() + "%";
                    String weatherIcon = daysForecast.get(1).getDay().getCondition().getIcon();
                    dailyWeatherForecast.add(new DailyWeatherModel(date, highTemp, lowTemp, precipChance, weatherIcon));
                    //
                    String cityName = response.body().getLocation().getName();
                    Double humidity = response.body().getForecast().getForecastday().get(i).getDay().getAvghumidity();
                    String sunrise = response.body().getForecast().getForecastday().get(i).getAstro().getSunrise();
                    String sunset = response.body().getForecast().getForecastday().get(i).getAstro().getSunset();
                    Double windSpeedDir = response.body().getForecast().getForecastday().get(i).getDay().getMaxwindMph();
                    Double visibilty = response.body().getForecast().getForecastday().get(i).getDay().getAvgvisMiles();
                    Double uv = response.body().getForecast().getForecastday().get(i).getDay().getUv();
                    Hour hourlyTemps = response.body().getForecast().getForecastday().get(i).getHour().get(i);
                    List<Hour> hourTemp = response.body().getForecast().getForecastday().get(i).getHour();

                    dailyWeatherView.add(new DailyWeatherViewModel(cityName,date,humidity,sunrise,sunset,windSpeedDir,visibilty, uv, hourTemp));
                }
                DailyWeatherAdapter adapter = new DailyWeatherAdapter(dailyWeatherForecast, MainActivity.this, MainActivity.this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.swapAdapter(adapter, false);
                recyclerView.setAdapter(adapter);
                recyclerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast toast;
                        Toast.makeText(MainActivity.this, "You touched the: ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                System.out.println("this is the message " + t.getMessage());
            }
        });
    }

    @Override
    public void onDailyWeatherClick(int position) {
        Intent intent = new Intent(this, DailyWeatherActivity.class);
        DailyWeatherViewModel dailyWeatherVMObject;
        dailyWeatherVMObject = dailyWeatherView.get(position);
        intent.putExtra("DailyWeatherObject", (Serializable) dailyWeatherVMObject);

        startActivity(intent);

    }
}