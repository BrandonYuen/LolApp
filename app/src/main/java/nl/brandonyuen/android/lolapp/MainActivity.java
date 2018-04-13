package nl.brandonyuen.android.lolapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private String          TAG = MainActivity.class.getSimpleName();

    public static TextView  result_status;
    public static EditText  input_name;
    public static String    summonerName;
    public static String    summonerID;
    public static TextView  result_soloduo_ranktier;
    public static ImageView result_soloduo_badge;
    public static TextView  result_soloduo_wins;
    public static TextView  result_soloduo_losses;
    public static TextView  result_flex_ranktier;
    public static ImageView result_flex_badge;
    public static TextView  result_flex_wins;
    public static TextView  result_flex_losses;

    private JSONArray leagueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Load Preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = getString(R.string.saved_summonername_default_key);
        summonerName = sharedPref.getString(getString(R.string.saved_summonername_key), defaultValue);


        // Cast bottom navigation menu
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Cast input views
        input_name = findViewById(R.id.summonerName);

        // Cast content views
        result_status           = findViewById(R.id.result_status);
        result_soloduo_ranktier = findViewById(R.id.result_soloduo_ranktier);
        result_soloduo_badge    = findViewById(R.id.result_soloduo_badge);
        result_flex_ranktier    = findViewById(R.id.result_flex_ranktier);
        result_flex_badge       = findViewById(R.id.result_flex_badge);
        result_flex_wins        = findViewById(R.id.result_flex_wins);
        result_flex_losses      = findViewById(R.id.result_flex_losses);
        result_soloduo_wins     = findViewById(R.id.result_soloduo_wins);
        result_soloduo_losses   = findViewById(R.id.result_soloduo_losses);

        // Update summoner name input field with saved preference (or default value)
        input_name.setText(summonerName);

        // Get saved league stats from last update
        String leagueList_str = sharedPref.getString(getString(R.string.saved_leagueliststring_key), null);
        try {
            leagueList = new JSONArray(leagueList_str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Update league stats fields
        if (leagueList != null) {updateLeagueStats(leagueList);}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Ga naar settings activity
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // Navigation click listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
        = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_live:
                //Ga naar nieuwe activity
                Intent intent = new Intent(MainActivity.this, LiveGameActivity.class);
                startActivity(intent);
                return true;
        }
        return false;
        }
    };

    public void onButtonClickUpdate (View view) {
        summonerName = input_name.getText().toString();
        Log.d("DEBUG", "summonerName: " + summonerName);
        if(summonerName != null && !summonerName.isEmpty()) {
            new GetSummonerID(this, MainActivity.this, summonerName).execute();
            result_status.setText("Waiting for data...");

            // Save new summoner name to preferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.saved_summonername_key), summonerName);
            editor.apply();
        }
    }

    public void updateSummonerID(String id) {
        Log.d("DEBUG", "updated SummonerID: " + id);
        summonerID = id;
        result_status.setText("SummonerID: "+id);

        // If an correct ID is fetched, update ID in pref and get more stats of user
        if (summonerID != null) {

            // Save new summoner name to preferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.saved_summonerid_key), summonerID);
            editor.apply();

            new GetLeagueStats(this, MainActivity.this, summonerID, "stats").execute();
        }
    }

    public void updateLeagueStats(JSONArray leagueList) {
        Log.d(TAG, "leagueList: " + leagueList);
        // Remove current league stats
        result_flex_ranktier.setText("Null");
        result_flex_badge.setImageResource(0);
        result_flex_wins.setText("Null");
        result_flex_losses.setText("Null");

        result_soloduo_ranktier.setText("Null");
        result_soloduo_badge.setImageResource(0);
        result_soloduo_wins.setText("Null");
        result_soloduo_losses.setText("Null");

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
                        result_flex_ranktier.setText(tier + " " + rank);
                        int flex_imageID = getResources().getIdentifier("nl.brandonyuen.android.lolapp:drawable/" + "badge_" + tier.toLowerCase(), null, null);
                        result_flex_badge.setImageResource(flex_imageID);
                        result_flex_wins.setText(""+wins);
                        result_flex_losses.setText(""+losses);
                        break;
                    case "RANKED_SOLO_5x5":
                        // Do stuff
                        result_soloduo_ranktier.setText(tier + " " + rank);
                        int soloduo_imageID = getResources().getIdentifier("nl.brandonyuen.android.lolapp:drawable/" + "badge_" + tier.toLowerCase(), null, null);
                        result_soloduo_badge.setImageResource(soloduo_imageID);
                        result_soloduo_wins.setText(""+wins);
                        result_soloduo_losses.setText(""+losses);
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
