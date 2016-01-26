package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;
import android.util.Log;

import com.gmail.grimesmea.builditbigger.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Async task that retrieves a joke from my GCE API
 */
class RetrieveJoke extends AsyncTask<Void, Void, String> {
    private static final String LOG_TAG = RetrieveJoke.class.getSimpleName();
    private static MyApi myApiService = null;
    private OnJokeRetrievedListener jokeRetrievedListener;

    public RetrieveJoke(OnJokeRetrievedListener jokeRetrievedListener) {
        this.jokeRetrievedListener = jokeRetrievedListener;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://build-it-1196.appspot.com/_ah/api/");
            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(LOG_TAG, "retrieved joke - " + result);
        if (result != null) {
            jokeRetrievedListener.onJokeRetrieved(result);
        }
    }

    public interface OnJokeRetrievedListener {
        void onJokeRetrieved(String joke);
    }
}
