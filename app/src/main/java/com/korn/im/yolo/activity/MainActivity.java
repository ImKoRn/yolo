package com.korn.im.yolo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.korn.im.yolo.R;
import com.korn.im.yolo.common.ApplicationHelper;
import com.korn.im.yolo.fragments.EmailFragment;
import com.korn.im.yolo.fragments.NewsFragment;
import com.korn.im.yolo.fragments.PortfolioListFragment;
import com.korn.im.yolo.services.GcmRegistrationIntentService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivityActivity";

    private static final String LAST_MENU_CATEGORY = "lastMenuCategory";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 0;
    private static final String LAST_TITLE = "ttl";

    private int lastMenuCategory = R.id.navBlog;

    private BroadcastReceiver connectivityBroadcast = null;
    private boolean isConnectivityBroadcastRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        initUi();

        connectivityBroadcast = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                checkInternet();
            }
        };

        if(savedInstanceState == null)
            setFragment(lastMenuCategory);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerGcm();

        startConnectivityBroadcast();

        checkInternet();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopConnectivityBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startConnectivityBroadcast() {
        if(!isConnectivityBroadcastRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectivityBroadcast,
                    intentFilter);
            isConnectivityBroadcastRegistered = true;
        }
    }

    private void stopConnectivityBroadcast() {
        if(connectivityBroadcast != null && isConnectivityBroadcastRegistered) {
            unregisterReceiver(connectivityBroadcast);
            isConnectivityBroadcastRegistered = false;
        }
    }

    private void checkInternet() {
        showNoInternetConnectionMessage(ApplicationHelper.hasEthernetConnection(this));
    }

    private void showNoInternetConnectionMessage(boolean hasInternet) {
        getSupportActionBar().setSubtitle(hasInternet?"": getResources().getString(R.string.noInternetConnectionMsg));
    }

    private void registerGcm() {
        if(checkPlayServices()) {
            if(ApplicationHelper.hasEthernetConnection(this))
                startService(new Intent(this, GcmRegistrationIntentService.class));
        }
    }

    private void initUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigationDrawerOpen, R.string.navigationDrawerClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.mainNavView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setFragment(int itemId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (lastMenuCategory = itemId) {
            case R.id.navBlog: {
                fragmentTransaction.replace(R.id.holder, new NewsFragment());
                break;
            }
            case R.id.navPortfolio: {
                fragmentTransaction.replace(R.id.holder, new PortfolioListFragment());
                break;
            }
            default: {
            }
        }
        fragmentTransaction.commit();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
                Log.i(TAG, "This device has play services errors.");
            } else {
                Toast.makeText(this, "You device not supported for GCM notification's", Toast.LENGTH_LONG).show();
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //TODO implement transaction

        if (item.getItemId() == lastMenuCategory) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        getSupportActionBar().setTitle(item.getTitle());

        setFragment(item.getItemId());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastMenuCategory = savedInstanceState.getInt(LAST_MENU_CATEGORY, R.id.navBlog);
        getSupportActionBar().setTitle(savedInstanceState
                .getString(LAST_TITLE, getResources().getString(R.string.appName)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putInt(LAST_MENU_CATEGORY, lastMenuCategory);
            outState.putString(LAST_TITLE, getSupportActionBar().getTitle().toString());
        }
    }

}
