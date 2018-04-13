package nl.brandonyuen.android.lolapp;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brand on 4/8/2018.
 */

public class GetLiveGameData extends Fetcher {

    private String finalUrl;
    private JSONObject liveGameData = new JSONObject();

    GetLiveGameData(LiveGameActivity a, Context c, String summonerID) {
        super(a,c);

        String url = this.mContext.getString(R.string.url_livegame);
        finalUrl = url + summonerID;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(finalUrl, this.mContext);

        Log.e(TAG, "Response from url: " + jsonStr);

        // Read json and process data
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                liveGameData = new JSONObject(jsonStr);
                Log.e(TAG, "Live Game Data: " + jsonObj);

            }
            catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        }
        else {
            Log.e(TAG, "Couldn't get json from server.");

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        this.liveGameActivity.updateLiveGame(liveGameData);
    }

}
