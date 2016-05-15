package com.korn.im.yolo.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.common.GenreAdapter;
import com.korn.im.yolo.fragments.PhotographersListFragment;
import com.korn.im.yolo.objects.Photograph;

public class PhotographerActivity extends AppCompatActivity {

    private TextView descriptionView;
    private RecyclerView genreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            finish();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer);

        initUi();

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        Intent intent = getIntent();
        if(intent != null)
            showPhotographer(intent);
    }

    private void showPhotographer(Intent intent) {
        Photograph photograph = (Photograph) intent.getSerializableExtra(PhotographersListFragment.PHOTOGRAPH_ITEM);
        getSupportActionBar().setTitle(photograph.getName());

        descriptionView.setText(photograph.getDescription());
        genreList.setLayoutManager(new LinearLayoutManager(this));
        genreList.setAdapter(new GenreAdapter(this, photograph.getListOfPhotoGenre()));
    }

    private void initUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        descriptionView = (TextView) findViewById(R.id.descriptionView);
        genreList = (RecyclerView) findViewById(R.id.genreList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
