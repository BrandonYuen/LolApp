package nl.brandonyuen.android.lolapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brand on 4/8/2018.
 */

public class LiveGameActivity extends AppCompatActivity {

    private String          TAG = LiveGameActivity.class.getSimpleName();

    public String summonerID;

    public static TextView  result_message;

    public static TextView  friendly1_name;
    public static TextView  friendly1_winperc;
    public static TextView  friendly1_totalgames;
    public static TextView  friendly1_ranktier;

    public static TextView  friendly2_name;
    public static TextView  friendly2_winperc;
    public static TextView  friendly2_totalgames;
    public static TextView  friendly2_ranktier;

    public static TextView  friendly3_name;
    public static TextView  friendly3_winperc;
    public static TextView  friendly3_totalgames;
    public static TextView  friendly3_ranktier;

    public static TextView  friendly4_name;
    public static TextView  friendly4_winperc;
    public static TextView  friendly4_totalgames;
    public static TextView  friendly4_ranktier;

    public static TextView  friendly5_name;
    public static TextView  friendly5_winperc;
    public static TextView  friendly5_totalgames;
    public static TextView  friendly5_ranktier;

    public static TextView  enemy1_name;
    public static TextView  enemy1_winperc;
    public static TextView  enemy1_totalgames;
    public static TextView  enemy1_ranktier;

    public static TextView  enemy2_name;
    public static TextView  enemy2_winperc;
    public static TextView  enemy2_totalgames;
    public static TextView  enemy2_ranktier;

    public static TextView  enemy3_name;
    public static TextView  enemy3_winperc;
    public static TextView  enemy3_totalgames;
    public static TextView  enemy3_ranktier;

    public static TextView  enemy4_name;
    public static TextView  enemy4_winperc;
    public static TextView  enemy4_totalgames;
    public static TextView  enemy4_ranktier;

    public static TextView  enemy5_name;
    public static TextView  enemy5_winperc;
    public static TextView  enemy5_totalgames;
    public static TextView  enemy5_ranktier;

    public static TextView      friendly_header;
    public static TableLayout   friendly_tableLayout;
    public static TextView      enemy_header;
    public static TableLayout   enemy_tableLayout;

    public static TableLayout tableLayout1;

    private List<Integer> participantIds = new ArrayList<Integer>();

    // Navigation click listener
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Ga naar nieuwe activity
                    Intent intent = new Intent(LiveGameActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        // Cast navigation view
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);

        // Cast content views
        friendly1_name         = findViewById(R.id.friendly1_name);
        friendly1_winperc      = findViewById(R.id.friendly1_winperc);
        friendly1_totalgames   = findViewById(R.id.friendly1_totalgames);
        friendly1_ranktier     = findViewById(R.id.friendly1_ranktier);
        friendly2_name         = findViewById(R.id.friendly2_name);
        friendly2_winperc      = findViewById(R.id.friendly2_winperc);
        friendly2_totalgames   = findViewById(R.id.friendly2_totalgames);
        friendly2_ranktier     = findViewById(R.id.friendly2_ranktier);
        friendly3_name         = findViewById(R.id.friendly3_name);
        friendly3_winperc      = findViewById(R.id.friendly3_winperc);
        friendly3_totalgames   = findViewById(R.id.friendly3_totalgames);
        friendly3_ranktier     = findViewById(R.id.friendly3_ranktier);
        friendly4_name         = findViewById(R.id.friendly4_name);
        friendly4_winperc      = findViewById(R.id.friendly4_winperc);
        friendly4_totalgames   = findViewById(R.id.friendly4_totalgames);
        friendly4_ranktier     = findViewById(R.id.friendly4_ranktier);
        friendly5_name         = findViewById(R.id.friendly5_name);
        friendly5_winperc      = findViewById(R.id.friendly5_winperc);
        friendly5_totalgames   = findViewById(R.id.friendly5_totalgames);
        friendly5_ranktier     = findViewById(R.id.friendly5_ranktier);

        enemy1_name         = findViewById(R.id.enemy1_name);
        enemy1_winperc      = findViewById(R.id.enemy1_winperc);
        enemy1_totalgames   = findViewById(R.id.enemy1_totalgames);
        enemy1_ranktier     = findViewById(R.id.enemy1_ranktier);
        enemy2_name         = findViewById(R.id.enemy2_name);
        enemy2_winperc      = findViewById(R.id.enemy2_winperc);
        enemy2_totalgames   = findViewById(R.id.enemy2_totalgames);
        enemy2_ranktier     = findViewById(R.id.enemy2_ranktier);
        enemy3_name         = findViewById(R.id.enemy3_name);
        enemy3_winperc      = findViewById(R.id.enemy3_winperc);
        enemy3_totalgames   = findViewById(R.id.enemy3_totalgames);
        enemy3_ranktier     = findViewById(R.id.enemy3_ranktier);
        enemy4_name         = findViewById(R.id.enemy4_name);
        enemy4_winperc      = findViewById(R.id.enemy4_winperc);
        enemy4_totalgames   = findViewById(R.id.enemy4_totalgames);
        enemy4_ranktier     = findViewById(R.id.enemy4_ranktier);
        enemy5_name         = findViewById(R.id.enemy5_name);
        enemy5_winperc      = findViewById(R.id.enemy5_winperc);
        enemy5_totalgames   = findViewById(R.id.enemy5_totalgames);
        enemy5_ranktier     = findViewById(R.id.enemy5_ranktier);

        result_message      = findViewById(R.id.message);

        friendly_header         = findViewById(R.id.textView);
        enemy_header            = findViewById(R.id.textView2);
        friendly_tableLayout    = findViewById(R.id.tableLayout);
        enemy_tableLayout       = findViewById(R.id.tableLayout2);

        // Get summoner ID from preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultValue = getString(R.string.saved_summonerid_default_key);
        summonerID = sharedPref.getString(getString(R.string.saved_summonerid_key), defaultValue);

        checkLiveGame();
    }

    protected void checkLiveGame() {
        new GetLiveGameData(this, LiveGameActivity.this, summonerID).execute();
    }

    protected void updateLiveGame(JSONObject liveGameData) {
        //TODO: tt
        Log.d(TAG, "Updating fields with live game data: "+liveGameData);

        // Set default values for views
        friendly1_winperc.setText("NaN");
        friendly1_totalgames.setText("NaN");
        friendly1_ranktier.setText("NaN");
        friendly2_winperc.setText("NaN");
        friendly2_totalgames.setText("NaN");
        friendly2_ranktier.setText("NaN");
        friendly3_winperc.setText("NaN");
        friendly3_totalgames.setText("NaN");
        friendly3_ranktier.setText("NaN");
        friendly4_winperc.setText("NaN");
        friendly4_totalgames.setText("NaN");
        friendly4_ranktier.setText("NaN");
        friendly5_winperc.setText("NaN");
        friendly5_totalgames.setText("NaN");
        friendly5_ranktier.setText("NaN");
        enemy1_winperc.setText("NaN");
        enemy1_totalgames.setText("NaN");
        enemy1_ranktier.setText("NaN");
        enemy2_winperc.setText("NaN");
        enemy2_totalgames.setText("NaN");
        enemy2_ranktier.setText("NaN");
        enemy3_winperc.setText("NaN");
        enemy3_totalgames.setText("NaN");
        enemy3_ranktier.setText("NaN");
        enemy4_winperc.setText("NaN");
        enemy4_totalgames.setText("NaN");
        enemy4_ranktier.setText("NaN");
        enemy5_winperc.setText("NaN");
        enemy5_totalgames.setText("NaN");
        enemy5_ranktier.setText("NaN");

        if (liveGameData.length() > 0) {

            JSONArray participants = null;
            try {
                participants = liveGameData.getJSONArray("participants");
                Integer friendlyTeamId = null;

                // Loop all participants
                for (int i = 0; i < participants.length(); i++) {
                    JSONObject p = null;
                    Integer id = null;
                    Integer teamId = null;

                    //Try to get participant data
                    try {
                        p = participants.getJSONObject(i);
                        id = p.getInt("summonerId");
                        teamId = p.getInt("teamId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Determine the friendly team id
                    if (id == Integer.parseInt(this.summonerID)) {
                        friendlyTeamId = teamId;
                        Log.d(TAG, "friendlyTeamId: "+friendlyTeamId);
                    }

                    // Fetch more data about participant
                    new GetLeagueStats(this, LiveGameActivity.this, Integer.toString(id), "live").execute();
                }

                // Loop all participants again, and fill views with participant names in corresponding team
                Integer friendlyCounter = 0;
                Integer enemyCounter = 0;
                for (int i=0; i<10; i++) {participantIds.add(0);}

                for (int i = 0; i < participants.length(); i++) {
                    JSONObject p = null;
                    String name = null;
                    Integer teamId = null;
                    Integer id = null;

                    //Try to get participant data
                    try {
                        p = participants.getJSONObject(i);
                        name = p.getString("summonerName");
                        teamId = p.getInt("teamId");
                        id = p.getInt("summonerId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Apply data to fields
                    Log.d(TAG, "teamId: "+teamId+" friendlyTeamId: "+friendlyTeamId);
                    if (teamId.equals(friendlyTeamId)) {
                        friendlyCounter++;
                        switch (friendlyCounter) {
                            case 1: friendly1_name.setText(name); participantIds.set(0, id); break;
                            case 2: friendly2_name.setText(name); participantIds.set(1, id); break;
                            case 3: friendly3_name.setText(name); participantIds.set(2, id); break;
                            case 4: friendly4_name.setText(name); participantIds.set(3, id); break;
                            case 5: friendly5_name.setText(name); participantIds.set(4, id); break;
                        }
                    }
                    else{
                        enemyCounter++;
                        switch (enemyCounter) {
                            case 1: enemy1_name.setText(name); participantIds.set(5, id); break;
                            case 2: enemy2_name.setText(name); participantIds.set(6, id); break;
                            case 3: enemy3_name.setText(name); participantIds.set(7, id); break;
                            case 4: enemy4_name.setText(name); participantIds.set(8, id); break;
                            case 5: enemy5_name.setText(name); participantIds.set(9, id); break;
                        }
                    }
                }

                // Update top message of activity
                result_message.setText("Found game!");

                // Show table layouts and headers for content
                friendly_tableLayout.setVisibility(View.VISIBLE);
                enemy_tableLayout.setVisibility(View.VISIBLE);
                friendly_header.setVisibility(View.VISIBLE);
                enemy_header.setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d(TAG, "Participants: "+participants);
        } else {
            // Update top message of activity
            result_message.setText("Could not find active live game.");

            // Hide table layouts and headers for content
            friendly_tableLayout.setVisibility(View.INVISIBLE);
            enemy_tableLayout.setVisibility(View.INVISIBLE);
            friendly_header.setVisibility(View.INVISIBLE);
            enemy_header.setVisibility(View.INVISIBLE);
        }
    }

    protected void updateParticipantData (JSONArray leagueList, Integer id) {
        if (leagueList.length() > 0) {
            try {
                JSONObject l = leagueList.getJSONObject(0);

                int wins = l.getInt("wins");
                int losses = l.getInt("losses");
                double totalGames = wins+losses;
                String totalGamesStr = String.format("%.0f", totalGames);
                double winPerc = Math.ceil((wins / totalGames) * 100);
                String winPercStr = String.format("%.0f", winPerc);
                int rowNumber = 0;

                String tier = l.getString("tier");
                String rank = l.getString("rank");

                // Determine which row this participant is
                for (int i=0; i < participantIds.size(); i++) {
                    int particId = participantIds.get(i);

                    if (particId == id) {
                        rowNumber = i+1;
                        Log.d(TAG, "summoner [" + id + "] is on row ("+rowNumber+")");
                    }
                }

                switch (rowNumber) {
                    case 1:
                        friendly1_winperc.setText(winPercStr + "%");
                        friendly1_totalgames.setText(totalGamesStr);
                        friendly1_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(friendly1_winperc, winPerc);
                        break;
                    case 2:
                        friendly2_winperc.setText(winPercStr + "%");
                        friendly2_totalgames.setText(totalGamesStr);
                        friendly2_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(friendly2_winperc, winPerc);
                        break;
                    case 3:
                        friendly3_winperc.setText(winPercStr + "%");
                        friendly3_totalgames.setText(totalGamesStr);
                        friendly3_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(friendly3_winperc, winPerc);
                        break;
                    case 4:
                        friendly4_winperc.setText(winPercStr + "%");
                        friendly4_totalgames.setText(totalGamesStr);
                        friendly4_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(friendly4_winperc, winPerc);
                        break;
                    case 5:
                        friendly5_winperc.setText(winPercStr + "%");
                        friendly5_totalgames.setText(totalGamesStr);
                        friendly5_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(friendly5_winperc, winPerc);
                        break;
                    case 6:
                        enemy1_winperc.setText(winPercStr + "%");
                        enemy1_totalgames.setText(totalGamesStr);
                        enemy1_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(enemy1_winperc, winPerc);
                        break;
                    case 7:
                        enemy2_winperc.setText(winPercStr + "%");
                        enemy2_totalgames.setText(totalGamesStr);
                        enemy2_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(enemy2_winperc, winPerc);
                        break;
                    case 8:
                        enemy3_winperc.setText(winPercStr + "%");
                        enemy3_totalgames.setText(totalGamesStr);
                        enemy3_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(enemy3_winperc, winPerc);
                        break;
                    case 9:
                        enemy4_winperc.setText(winPercStr + "%");
                        enemy4_totalgames.setText(totalGamesStr);
                        enemy4_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(enemy4_winperc, winPerc);
                        break;
                    case 10:
                        enemy5_winperc.setText(winPercStr + "%");
                        enemy5_totalgames.setText(totalGamesStr);
                        enemy5_ranktier.setText(tier + " " + rank);
                        positiveWinrateCheck(enemy5_winperc, winPerc);
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void positiveWinrateCheck(View view, double winPerc) {
        Log.d(TAG, "winPerc: "+winPerc);
        if (winPerc >= 60) {
            view.setBackgroundResource(R.color.colorAccent);
        }

    }
}
