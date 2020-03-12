package com.example.watherapp.Weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ajts.androidmads.fontutils.FontUtils;
import com.example.watherapp.R;
import com.example.watherapp.Weather.WeatherResponse;
import com.example.watherapp.Weather.WeatherService;
import com.example.watherapp.Weather.api.NetworkUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static String baseURL ="http://api.openweathermap.org/";
    public static String AppID = "4f7cb81dd9d2cf29553d87079393abaf";
    public static String lat="41";
    public static String lon ="20";
    private static int SPLASH_TIME_OUT=4000;
    private TextView weatherData;
    private BottomNavigationView nav;
    private FrameLayout frameLayout;
    private NowFragment nowFragment;
    private HourlyFragment hourlyFragment;
    private DailyFragment dailyFragment;
    private NewsFragment newsFragment;
    public static final String TAG =MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout_main);
        nav = (BottomNavigationView) findViewById(R.id.menu_nav);
        nowFragment=new NowFragment();
        hourlyFragment = new HourlyFragment();
        dailyFragment = new DailyFragment();
        newsFragment = new NewsFragment();
        URL weatherUrl = NetworkUtils.buildUrlForWeather();
        new FetchWeatherDetail().execute(weatherUrl);
        Log.i(TAG,"onCreate:weatherUrl:"+weatherUrl);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_now:
                        nav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(nowFragment);
                        return true;
                    case R.id.nav_daily:
                        nav.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(dailyFragment);
                        return true;
                    case R.id.nav_hourly:
                        nav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(hourlyFragment);
                        return true;
                    case R.id.nav_news:
                        nav.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(newsFragment);
                        return true;
                    default:
                        return false;





                }
            }
        });

        weatherData = findViewById(R.id.textView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentData();
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_main,fragment);
        fragmentTransaction.commit();
    }

    void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call call = service.getCurrentWeatherData(lat, lon, AppID);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = (WeatherResponse) response.body();
                    assert weatherResponse != null;

                    String stringBuilder = "Country: " +
                            weatherResponse.sys.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main.temp +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main.temp_max +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main.pressure+
                            "\n" +
                            "Wind: " +
                            weatherResponse.wind.speed;


                    weatherData.setText(stringBuilder);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                weatherData.setText(t.getMessage());
            }
        });
    }
    private class FetchWeatherDetail extends AsyncTask<URL,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL weatherUrl = urls[0];
            String weatherSearchResults = null;
            try{
                weatherSearchResults = NetworkUtils.getResponseFromHttpUrl(weatherUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i(TAG,"doInBackground weatherSearchResults:"+weatherSearchResults);
            return weatherSearchResults;
        }
    }
}
