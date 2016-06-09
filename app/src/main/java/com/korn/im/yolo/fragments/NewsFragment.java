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
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.activity.NewsActivity;
import com.korn.im.yolo.common.AutoUpdateOnScrollListener;
import com.korn.im.yolo.common.NewsAdapter;
import com.korn.im.yolo.loaders.DataLoader;
import com.korn.im.yolo.objects.News;

import java.util.List;

/**
 * News fragment
 */
public class NewsFragment extends Fragment {
    private NewsAdapter newsAdapter;
    private SwipeRefreshLayout swipeRefreshView;
    private TextView emptyNewsListTextView;
    private AutoUpdateOnScrollListener scrollListener;

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
        swipeRefreshView.setRefreshing(true);
        refresh(DataLoader.REQUEST_RESTORE_DATA);
    }

    private void refresh(int request) {
        DataLoader.getInstance().sendRequest(request, News.CATEGORY_ID,
                new DataLoader.ResultListener<News>() {
                    @Override
                    public void onResult(List<News> list) {
                        newsAdapter.addNewData(list);
                        showEmptyListText();
                        scrollListener.setLastItems(false);
                        if(swipeRefreshView.isRefreshing()) swipeRefreshView.setRefreshing(false);
                    }
                });
    }

    private void initUi(View view) {
        emptyNewsListTextView = (TextView) view.findViewById(R.id.emptyListTextView);

        RecyclerView newsList = (RecyclerView) view.findViewById(R.id.newsListView);
        if(newsList != null) {
            final LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
                @Override
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            };
            newsList.addOnScrollListener(scrollListener = new AutoUpdateOnScrollListener() {
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if(llm.findLastVisibleItemPosition() == newsAdapter.getItemCount() - 1 &&
                            !isLastItems()) {
                        DataLoader.getInstance().sendRequest(DataLoader.REQUEST_LOAD_NEW_DATA, News.CATEGORY_ID,
                                newsAdapter.getItemCount(),
                                new DataLoader.ResultListener<News>() {
                                    @Override
                                    public void onResult(List<News> list) {
                                        if(list.size() == 0)
                                            setLastItems(true);
                                        showEmptyListText();
                                        newsAdapter.loading(false);
                                        newsAdapter.addData(list);
                                    }
                                });
                        newsAdapter.loading(true);
                    }
                }
            });
            newsList.setLayoutManager(llm);
            newsList.setAdapter(newsAdapter = new NewsAdapter(getActivity()));
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
                refresh(DataLoader.REQUEST_LOAD_NEW_DATA);
            }
        });
    }

    private void showEmptyListText() {
        boolean show = newsAdapter.getItemCount() == 0;
        emptyNewsListTextView.setVisibility(show?View.VISIBLE:View.GONE);
    }
}
