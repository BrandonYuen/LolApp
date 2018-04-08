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
    protected MainActivity activity;
    private ProgressDialog pDialog;

    public Fetcher (MainActivity a, Context c) {
        this.activity = a;
        this.mContext = c;
    }

    protected Context getContext () {
        return this.mContext;
    }

    @Override
    protected void onPreExecute() {
        pDialog = ProgressDialog.show(mContext, "Wait...", "Fetching data...", true);
        pDialog.setCancelable(false);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}