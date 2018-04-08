package nl.brandonyuen.android.lolapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by brand on 4/8/2018.
 */

public class GetSummonerID extends Fetcher {

    private String finalUrl;
    private String summonerID;

    GetSummonerID(MainActivity a, Context c, String name) {
        super(a, c);
        String url = this.mContext.getString(R.string.url_summoner);
        finalUrl = url + name;
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

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                summonerID = jsonObj.getString("id");

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

        this.activity.updateSummonerID(summonerID);
    }

}
