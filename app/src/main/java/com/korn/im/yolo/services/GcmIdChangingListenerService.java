package com.korn.im.yolo.services;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by korn on 26.05.16.
 */
public class GcmIdChangingListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent intent = new Intent(this, GcmRegistrationIntentService.class);
        startService(intent);
    }
}
