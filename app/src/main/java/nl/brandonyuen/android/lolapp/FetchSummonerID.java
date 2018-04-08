package nl.brandonyuen.android.lolapp;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by brand on 4/6/2018.
 */

public class FetchSummonerID extends AsyncTask<Void, Void, Void> {
    String data;
    String data_parsed = "";
    String data_singleParsed = "";

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/"+MainActivity.summonerName);

            // Create http connection and input stream/reader
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Origin", "https://developer.riotgames.com");
            connection.setRequestProperty("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("X-Riot-Token", "RGAPI-530c0267-11ee-4b96-9ab1-8fbd3e748f4a");
            connection.setRequestProperty("Accept-Language", "en-US");

            // Create input stream and buffer reader
            InputStream in = connection.getInputStream();

            data = convertStreamToString(in);

            // TODO: remove
            Log.d("DATA", data);

            // Put data into JSON Array
            JSONArray JA_data = new JSONArray(data);

            // Loop/read JSON Array
            for (int i=0; i<JA_data.length(); i++) {

                // For every json object in the array
                JSONObject JO_data = (JSONObject) JA_data.get(i);
                data_singleParsed = "" + JO_data.get("id");
                data_parsed = data_singleParsed;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.result_status.setText(data_parsed);
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

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
