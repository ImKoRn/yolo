package com.korn.im.yolo.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.korn.im.yolo.R;
import com.korn.im.yolo.activity.MainActivity;

/**
 * Created by korn on 26.05.16.
 */
public class GcmMessagesListenerService extends com.google.android.gms.gcm.GcmListenerService {
    private static final String TAG = "GcmMessagesListenerService";
    private static final int NOTIFICATION = 0;
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_TITLE = "title";

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String title = data.getString(EXTRA_TITLE);
        String message = data.getString(EXTRA_MESSAGE);
        showNotification(this, title, message);
    }

    private void showNotification(Context context, String title, String message) {
        Notification notification = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(context, 0,
                        new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .getNotification();

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION, notification);
    }
}
