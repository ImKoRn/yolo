package com.korn.im.yolo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.objects.News;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by korn on 04.06.16.
 */
public class NewsActivity extends AppCompatActivity {
    public static final String NEWS_EXTRA = "news";
    private News news;
    private TextView contentView;
    private ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initNews();

        setContentView(R.layout.activity_news);

        initUi();

        prepareUiToNews();
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

    private void initNews() {
        Intent intent = getIntent();
        news = intent.getParcelableExtra(NEWS_EXTRA);

        if(news == null) finish();
    }

    private void initUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        contentView = (TextView) findViewById(R.id.contentView);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    private void prepareUiToNews() {
        if(news.getIconReference() != null)
            ImageLoader.getInstance().displayImage(news.getIconReference(), imageView);

        getSupportActionBar().setTitle(news.getTitle());

        contentView.setText(news.getContent());
    }
}
