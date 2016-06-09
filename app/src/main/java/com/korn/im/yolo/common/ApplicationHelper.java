package com.korn.im.yolo.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;

public final class ApplicationHelper
{
    private static final String APP_VERSION_PREFS = "application_version";

    private static int getApplicationVersionCode(Context context)
    {
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo;
        int applicationVersion = 1;
        try
        {
            packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            applicationVersion = packageInfo.versionCode;
        }
        catch (NameNotFoundException ignored)
        {
        }
        return applicationVersion;
    }

    private static int getApplicationVersionCodeFromPreferences(Context context)
    {
        return context.getSharedPreferences(APP_VERSION_PREFS, Context.MODE_PRIVATE).getInt("application_version_code", 0);
    }

    public static boolean hasEthernetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager != null &&
                connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}