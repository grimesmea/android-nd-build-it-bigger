package com.udacity.gradle.builditbigger;

import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests to check that the RetrieveJoke async task receives a non-empty string
 */
public class RetrieveJokeAndroidTest extends TestCase {

    // IP addresses for both Genymotion and Android Studio emulators are provided below. Only the IP
    // address for the emulator you are using should be uncommented. If using your own device, add
    // your device IP address below!
    private static final String GCE_URL = "http://10.0.3.2:8080/_ah/api/"; // Genymotion
    // private static final String GCE_URL = "http://10.0.2.2:8080/_ah/api/"; // Android Studio Emulator IP

    private final CountDownLatch signal = new CountDownLatch(1);

    public void testJokeRetriever() {
        new RetrieveJoke(new TestJokeListener(), GCE_URL).execute();
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
