package nl.brandonyuen.android.lolapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brand on 4/8/2018.
 */

public class GetLeagueStats extends Fetcher {

    private String urlString;
    private String summonerID;

    // TODO: Declare all variables to be set in the App views
    private JSONArray leagueList = new JSONArray();

    GetLeagueStats(MainActivity a, Context c, String summonerID) {
        super(a, c);
        String url = this.mContext.getString(R.string.url_leagueStats);
        urlString = url + summonerID;
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

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                leagueList = new JSONArray(jsonStr);
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

        this.activity.updateLeagueStats(leagueList);
    }

}
