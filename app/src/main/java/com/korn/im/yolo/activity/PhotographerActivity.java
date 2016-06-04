package com.korn.im.yolo.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.common.PicturesAdapter;
import com.korn.im.yolo.fragments.PortfolioListFragment;

import me.relex.circleindicator.CircleIndicator;

public class PhotographerActivity extends AppCompatActivity {

    private TextView descriptionView;
    private RecyclerView genreList;
    private PicturesAdapter picturesAdapter;

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
        int id = intent.getIntExtra(PortfolioListFragment.SELECTED_PORTFOLIO_ID, -1);
        if(id == -1) finish();

        //Portfolio portfolio = .listOfPortfolios.get(id);
       /* getSupportActionBar().setTitle(portfolio.getTitle());

        descriptionView.setText(portfolio.getContent());

        picturesAdapter.imageUrlList = portfolio.getListOfPhotoReferences();
        picturesAdapter.notifyDataSetChanged();*/
    }

    private void initUi() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        descriptionView = (TextView) findViewById(R.id.descriptionView);
        genreList = (RecyclerView) findViewById(R.id.genreList);


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(picturesAdapter = new PicturesAdapter(this));

        CircleIndicator circleIndicator = (CircleIndicator) findViewById(R.id.viewPagerIndicator);
        circleIndicator.setViewPager(viewPager);
        picturesAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
