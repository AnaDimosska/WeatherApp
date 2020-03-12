package com.example.watherapp.Weather.api;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class NetworkUtils {
    public static final String TAG="NETWORKUTILS";
    private final static String WEATHER_BASE_URL="https://dataservice.accuweather.com/forecasts/v1/daily/5day/1172";
    private final static String API_KEY="8JjFJhATILn2L6SL9c55RrSPYRDAvrzy";
    private final static String PARAM_API_KEY="apikey";
    public static URL buildUrlForWeather(){
        Uri buildUrl = Uri.parse(WEATHER_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API_KEY,API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(buildUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i(TAG,"buildUriForWeather:url"+url);
        return url;
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            } else return null;
        } finally {
            urlConnection.disconnect();
        }
    }
}
