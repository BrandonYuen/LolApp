package nl.brandonyuen.android.lolapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String          TAG = MainActivity.class.getSimpleName();

    private ProgressDialog  pDialog;

    private TextView        mTextMessage;

    public static TextView  result_status;
    public static EditText  input_name;
    public static String    summonerName;
    public static TextView  soloduoResult_ranktier;
    public static TextView  flexResult_ranktier;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_live:
                    mTextMessage.setText(R.string.title_live);
                    return true;
                case R.id.navigation_search:
                    mTextMessage.setText(R.string.title_search);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        result_status = findViewById(R.id.result_status);
        input_name = findViewById(R.id.summonerName);
        soloduoResult_ranktier = findViewById(R.id.soloduoResult_ranktier);
        flexResult_ranktier = findViewById(R.id.flexResult_ranktier);
    }

    public void onApplySummonerName (View view) {
        summonerName = input_name.getText().toString();
        Log.e("DEBUG", "summonerName: " + summonerName);
        new GetSummonerID(summonerName, getString(R.string.url_summoner)).execute();
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetSummonerID extends AsyncTask<Void, Void, Void> {

        private String  urlString;
        private String  summonerID;

        GetSummonerID(String name, String url) {
            urlString = url + name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    summonerID = jsonObj.getString("id");

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            // Show ID in app
            result_status.setText("ID: "+summonerID);

            // If an correct ID is fetched, collect more data of summoner
            if (summonerID != null) {
                new GetLeagueStats(summonerID, getString(R.string.url_leagueStats)).execute();
            }

        }

    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetLeagueStats extends AsyncTask<Void, Void, Void> {

        private String  urlString;

        // TODO: Declare all variables to be set in the App views
        private JSONArray leagueList = new JSONArray();

        private String  queueType;
        private int     wins;
        private int     losses;
        private String  leagueName;
        private String  tier;
        private String  rank;
        private int     leaguePoints;

        GetLeagueStats (String id, String url) {
            urlString = url + id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    leagueList = new JSONArray(jsonStr);
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            // Show new data in app
            Log.e(TAG, "leagueList: " + leagueList);

            // looping through All Leagues of Summoner
            for (int i = 0; i < leagueList.length(); i++) {
                JSONObject l = null;
                try {
                    l = leagueList.getJSONObject(i);

                    int wins = l.getInt("wins");
                    int losses = l.getInt("losses");
                    int leaguePoints = l.getInt("leaguePoints");

                    String queueType = l.getString("queueType");
                    String leagueName = l.getString("leagueName");
                    String tier = l.getString("tier");
                    String rank = l.getString("rank");

                    switch (queueType){
                        case "RANKED_FLEX_SR":
                            // Show data in views
                            soloduoResult_ranktier.setText(tier + " " + rank);
                            break;
                        case "RANKED_SOLO_5x5":
                            // Do stuff
                            flexResult_ranktier.setText(tier + " " + rank);
                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
