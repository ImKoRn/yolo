package com.korn.im.yolo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.korn.im.yolo.R;
import com.korn.im.yolo.fragments.EmailFragment;
import com.korn.im.yolo.fragments.PhotographersListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String LAST_MENU_CATEGORY = "lastMenuCategory";
    private int lastMenuCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.mainNavView);
        navigationView.setNavigationItemSelectedListener(this);

        setFragment(lastMenuCategory);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            startActivity(new Intent(this, PhotographerActivity.class));
            return true;
        }

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

        setFragment(item.getItemId());

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setFragment(int itemId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (lastMenuCategory = itemId) {
            case R.id.nav_blog : {
                fragmentTransaction.replace(R.id.holder, new EmailFragment());
                break;
            }
            case R.id.nav_portfolio : {
                fragmentTransaction.replace(R.id.holder, new PhotographersListFragment());
                break;
            }
        }
        fragmentTransaction.commit();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            lastMenuCategory = savedInstanceState.getInt(LAST_MENU_CATEGORY, R.id.nav_blog);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putInt(LAST_MENU_CATEGORY, lastMenuCategory);
        }
    }
}
