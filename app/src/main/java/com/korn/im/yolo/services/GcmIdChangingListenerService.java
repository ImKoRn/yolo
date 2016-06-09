package com.korn.im.yolo.services;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Don`n now how it works
 */
public class GcmIdChangingListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent intent = new Intent(this, GcmRegistrationIntentService.class);
        startService(intent);
    }
}
