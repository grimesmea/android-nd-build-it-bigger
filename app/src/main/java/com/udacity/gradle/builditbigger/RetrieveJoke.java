package com.udacity.gradle.builditbigger;

import android.os.AsyncTask;

import com.gmail.grimesmea.builditbigger.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

/**
 * Async task that retrieves a joke from my GCE API
 */
public class RetrieveJoke extends AsyncTask<Void, Void, String> {
    private static final String LOG_TAG = RetrieveJoke.class.getSimpleName();
    private static final String DEFAULT_GCE_URL = "https://build-it-1196.appspot.com/_ah/api/";
    private static MyApi myApiService = null;
    private String gceUrl;
    private OnJokeRetrievedListener jokeRetrievedListener;

    public RetrieveJoke(OnJokeRetrievedListener jokeRetrievedListener) {
        this.jokeRetrievedListener = jokeRetrievedListener;
        this.gceUrl = DEFAULT_GCE_URL;
    }

    public RetrieveJoke(OnJokeRetrievedListener jokeRetrievedListener, String gceUrl) {
        this.jokeRetrievedListener = jokeRetrievedListener;
        this.gceUrl = gceUrl;
    }

    @Override
    protected String doInBackground(Void... params) {
        if (myApiService == null) {
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl(gceUrl);
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
        if (result != null) {
            jokeRetrievedListener.onJokeRetrieved(result);
        }
    }

    public interface OnJokeRetrievedListener {
        void onJokeRetrieved(String joke);
    }
}
