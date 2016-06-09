package com.korn.im.yolo.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.korn.im.yolo.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class GcmRegistrationIntentService extends IntentService {
    private static final String TAG = "GcmRegistrationIntentService";

    private static final int MAX_ATTEMPT = 3;
    private static final String REGISTRATION_SITE_URL = "http://yolo.kiev.ua/?regId=";
    private static final long SECOND = 1000;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public GcmRegistrationIntentService() {
        super(TAG);
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);

        String token;

        try {
            token = instanceID.getToken(getString(R.string.senderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "Token received");

            sendTokenToServer(token);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("LongLogTag")
    private void sendTokenToServer(String token) {
        long delay = SECOND * new Random().nextInt(5) + 1;
        for (int attempt = 0; attempt < MAX_ATTEMPT; attempt++) {
            try {
                HttpURLConnection connection = ((HttpURLConnection) new URL(REGISTRATION_SITE_URL + token).openConnection());
                Log.i(TAG, "Server response " + connection.getResponseMessage());
                connection.disconnect();

                break;
            } catch (MalformedURLException e){
                e.printStackTrace();
                Log.e(TAG, "Bad url to server");
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Can't connect to server");
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.e(TAG, "Interrupted when waiting for connection");
            }
            delay *=2;
        }
    }
}
