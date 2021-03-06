package nl.brandonyuen.android.lolapp;

/**
 * Created by brand on 4/6/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();

    public String makeServiceCall(String reqUrl, Context context) {
        String response = null;
        try {
            // Get API_KEY from preferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            String defaultValue = context.getString(R.string.saved_apikey_default_key);
            String API_key = sharedPref.getString(context.getString(R.string.saved_apikey_key), "DEFAULT");
            //Log.d(TAG, "API_KEY: "+API_key);

            // Create http connection
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Origin", "https://developer.riotgames.com");
            conn.setRequestProperty("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
            conn.setRequestProperty("X-Riot-Token", API_key);
            conn.setRequestProperty("Accept-Language", "en-US");

            // Read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        // Try to build string
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}