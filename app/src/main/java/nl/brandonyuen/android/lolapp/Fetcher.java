package nl.brandonyuen.android.lolapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by brand on 4/8/2018.
 * Abstract class for fetching data
 */

public abstract class Fetcher extends AsyncTask<Void,Void,Void> {

    protected static final  String  TAG = Fetcher.class.getSimpleName();

    protected Context mContext;
    protected MainActivity mainActivity;
    protected LiveGameActivity liveGameActivity;
    protected Boolean showProgressDialog;
    private ProgressDialog pDialog;

    // Overloading for use of multiple activities
    public Fetcher (MainActivity a, Context c) {
        this.mainActivity = a;
        this.mContext = c;
        this.showProgressDialog = true;
    }

    public Fetcher (LiveGameActivity a, Context c) {
        this.liveGameActivity = a;
        this.mContext = c;
        this.showProgressDialog = false;
    }

    protected Context getContext () {
        return this.mContext;
    }

    @Override
    protected void onPreExecute() {
        if (showProgressDialog) {
            // Show progress dialog
            pDialog = ProgressDialog.show(mContext, "Wait...", "Fetching data...", true);
            pDialog.setCancelable(false);
            super.onPreExecute();
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        if (showProgressDialog) {
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
}