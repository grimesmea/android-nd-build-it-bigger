package com.udacity.gradle.builditbigger;

import android.test.AndroidTestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Test to check that the RetrieveJoke async task receives a non-empty string
 */
public class RetrieveJokeAndroidTest extends AndroidTestCase {

    private final CountDownLatch signal = new CountDownLatch(1);

    public void testJokeRetriever() {
        new RetrieveJoke(new TestJokeListener()).execute();
        try {
            boolean success = signal.await(5, TimeUnit.SECONDS);
            if (!success) {
                fail("Server not running.");
            }
        } catch (InterruptedException e) {
            fail();
        }
    }

    private class TestJokeListener implements RetrieveJoke.OnJokeRetrievedListener {

        @Override
        public void onJokeRetrieved(String joke) {
            assertTrue(joke != null && joke.length() > 0);
            signal.countDown();
        }
    }
}