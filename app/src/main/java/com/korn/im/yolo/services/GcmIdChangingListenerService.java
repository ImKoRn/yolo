package com.korn.im.yolo.services;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Don`n now how it works
 */
public class GcmIdChangingListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, GcmRegistrationIntentService.class);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        startService(intent);
    }
}
