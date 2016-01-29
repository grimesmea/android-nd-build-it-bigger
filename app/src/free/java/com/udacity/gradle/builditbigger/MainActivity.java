package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import grimesmea.gmail.com.jokedisplay.JokeActivity;

/**
 * Activity managing async requests for jokes and interstitial ad display
 */
public class MainActivity extends ActionBarActivity {

    private ProgressBar spinner;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (ProgressBar) findViewById(R.id.progressBar);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            MainActivityFragment fragment = new MainActivityFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment).commit();
        }

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        requestNewInterstitial();

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                getJoke();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        interstitialAd.loadAd(adRequest);
    }


    public void getJoke(View view) {
        spinner.setVisibility(View.VISIBLE);
        Log.d("MainActivity", String.valueOf(interstitialAd.isLoaded()));
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            new RetrieveJoke(new JokeRetrievalHandler()).execute();
        }
    }

    public void getJoke() {
        spinner.setVisibility(View.VISIBLE);
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        } else {
            new RetrieveJoke(new JokeRetrievalHandler()).execute();
        }
    }

    private void displayJoke(String joke) {
        Intent intent = new Intent(getApplicationContext(), JokeActivity.class);
        intent.putExtra(JokeActivity.JOKE_KEY, joke);
        startActivity(intent);
    }

    // Handler allowing the RetrieveJoke to ultimately display the joke it receives as a result
    // once the interstitial ad is closed
    private class JokeRetrievalHandler implements RetrieveJoke.OnJokeRetrievedListener {

        @Override
        public void onJokeRetrieved(String joke) {
            spinner.setVisibility(View.GONE);
            displayJoke(joke);
        }
    }
}
