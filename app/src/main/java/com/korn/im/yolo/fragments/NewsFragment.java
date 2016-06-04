package com.korn.im.yolo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.korn.im.yolo.R;
import com.korn.im.yolo.activity.NewsActivity;
import com.korn.im.yolo.common.ItemDecorator;
import com.korn.im.yolo.common.NewsAdapter;
import com.korn.im.yolo.loaders.DataLoader;
import com.korn.im.yolo.objects.News;

import java.util.List;

/**
 * Created by korn on 04.06.16.
 */
public class NewsFragment extends Fragment {
    private RecyclerView newsList;
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefreshView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        initUi(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        DataLoader.getInstance().sendRequest(DataLoader.REQUEST_RESTORE_DATA, News.CATEGORY_ID,
                new DataLoader.ResultListener<News>() {
                    @Override
                    public void onResult(List<News> list) {
                        newsAdapter.addNewData(list);
                    }
                });
    }

    private void refresh() {
        DataLoader.getInstance().sendRequest(DataLoader.REQUEST_LOAD_NEW_DATA, News.CATEGORY_ID,
                new DataLoader.ResultListener<News>() {
                    @Override
                    public void onResult(List<News> list) {
                        newsAdapter.addNewData(list);
                        if(swipeRefreshView.isRefreshing()) swipeRefreshView.setRefreshing(false);
                    }
                });
    }

    private void initUi(View view) {
        newsList = (RecyclerView) view.findViewById(R.id.newsListView);
        if(newsList != null) {
            newsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            newsList.setAdapter(newsAdapter = new NewsAdapter(getActivity()));
            newsList.addItemDecoration(new ItemDecorator(getActivity(), 5, 5));
            newsAdapter.setOnListItemButtonClicked(new NewsAdapter.OnListItemButtonClicked() {
                @Override
                public void onListItemButtonClicked(int position) {
                    Intent intent = new Intent(getActivity(), NewsActivity.class);
                    intent.putExtra(NewsActivity.NEWS_EXTRA, newsAdapter.getItem(position));
                    startActivity(intent);
                }
            });
        }

        swipeRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshView);
        swipeRefreshView.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

}
