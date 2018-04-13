package nl.brandonyuen.android.lolapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brand on 4/8/2018.
 */

public class GetLeagueStats extends Fetcher {

    private static final String TAG = GetLeagueStats.class.getSimpleName();

    private String urlString;
    private String summonerID;
    private JSONArray leagueList = new JSONArray();
    private String leagueList_str;
    private String action;

    GetLeagueStats(MainActivity a, Context c, String summonerID, String action) {
        super(a, c);
        String url = this.mContext.getString(R.string.url_leagueStats);
        urlString = url + summonerID;
        this.action = action;
    }

    GetLeagueStats(LiveGameActivity a, Context c, String summonerID, String action) {
        super(a, c);
        String url = this.mContext.getString(R.string.url_leagueStats);
        urlString = url + summonerID;
        this.action = action;
        this.summonerID = summonerID;
        this.leagueList = null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(urlString, this.mContext);

        Log.d(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            // Try to create JSONArray from JSONString and save data
            try {
                leagueList = new JSONArray(jsonStr);
                leagueList_str = jsonStr;
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (this.action.equals("stats")) {
            // Save data to preferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.mainActivity);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(this.mainActivity.getString(R.string.saved_leagueliststring_key), leagueList_str);
            editor.apply();

            // Update data in activity
            this.mainActivity.updateLeagueStats(leagueList);
        }
        else if (this.action.equals("live")) {
            // Update data in activity
            this.liveGameActivity.updateParticipantData(leagueList, Integer.parseInt(summonerID));
        }
    }

}
