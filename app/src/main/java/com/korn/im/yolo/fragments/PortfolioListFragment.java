package com.korn.im.yolo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.common.ItemDecorator;
import com.korn.im.yolo.common.PortfolioPicturesAdapter;
import com.korn.im.yolo.common.PortfoliosAdapter;
import com.korn.im.yolo.loaders.DataLoader;
import com.korn.im.yolo.objects.Portfolio;
import com.korn.im.yolo.activity.PhotographerActivity;

import java.util.List;

/**
 * Fragment of photographers imageUrlList
 */
public class PortfolioListFragment extends Fragment implements PortfoliosAdapter.OnListItemClicked {
    private static final String TAG = "PortfolioListFragment";

    public static final String SELECTED_PORTFOLIO_ID = "photographItem";
    private static final String LAST_SHOWED_ITEM_ID = "lastShowedItemId";
    private static final String PORTFOLIOS_LIST = "listOfPortfolio";

    private RecyclerView portfolioListView;
    private RecyclerView picturesViewListView;

    private PortfoliosAdapter portfoliosAdapter;

    private PortfolioPicturesAdapter picturesAdapter;


    private TextView photographersEmptyView;
    private TextView photographerEmptyView;
    private View photographerContainer;
    private SwipeRefreshLayout swipeRefreshView;


    private int lastShowedPhotographerId = -1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            lastShowedPhotographerId = savedInstanceState.getInt(LAST_SHOWED_ITEM_ID);
        }

        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(view == null) {
            view = inflater.inflate(R.layout.fragment_photographers_list, container, false);
        }

        initUi(view);

        setHasOptionsMenu(true);

        return view;
    }
    private void initUi(View view) {
        photographerContainer = view.findViewById(R.id.photographerContainer);
        photographersEmptyView = (TextView) view.findViewById(R.id.photographersEmptyView);
        photographerEmptyView = (TextView) view.findViewById(R.id.photographerEmptyView);


        picturesViewListView = (RecyclerView) view.findViewById(R.id.picturesViewList);

        if(picturesViewListView != null) {
            picturesViewListView.setAdapter(picturesAdapter = new PortfolioPicturesAdapter(getActivity()));
            picturesViewListView.setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.HORIZONTAL, false));
        }

        swipeRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshView);
        swipeRefreshView.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DataLoader.getInstance().sendRequest(DataLoader.REQUEST_LOAD_NEW_DATA, Portfolio.CATEGORY_ID,
                        new DataLoader.ResultListener<Portfolio>() {
                            @Override
                            public void onResult(List<Portfolio> list) {
                                portfoliosAdapter.addNewPortfolios(list);
                                setPhotographersListVisibility(portfoliosAdapter.size() > 0);
                                if(swipeRefreshView.isRefreshing()) swipeRefreshView.setRefreshing(false);
                            }
                        });
            }
        });

        portfolioListView = (RecyclerView) view.findViewById(R.id.photographersList);
        LinearLayoutManager linearLayoutManager;
        portfolioListView.setLayoutManager(linearLayoutManager =  new LinearLayoutManager(getActivity()));
        portfolioListView.setAdapter(portfoliosAdapter = new PortfoliosAdapter(getActivity(), linearLayoutManager));
        portfolioListView.addItemDecoration(new ItemDecorator(getActivity(), 3, 1));

        portfoliosAdapter.setClickedItem(lastShowedPhotographerId);
        portfoliosAdapter.setOnListItemClicked(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.update)
            DataLoader.getInstance().sendRequest(DataLoader.REQUEST_LOAD_NEW_DATA, Portfolio.CATEGORY_ID,
                    new DataLoader.ResultListener<Portfolio>() {
                        @Override
                        public void onResult(List<Portfolio> list) {
                            portfoliosAdapter.addNewPortfolios(list);
                            setPhotographersListVisibility(true);
                        }
                    });

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putInt(LAST_SHOWED_ITEM_ID, lastShowedPhotographerId);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DataLoader.getInstance().sendRequest(DataLoader.REQUEST_RESTORE_DATA, Portfolio.CATEGORY_ID,
                new DataLoader.ResultListener<Portfolio>() {
                    @Override
                    public void onResult(List<Portfolio> list) {
                        portfoliosAdapter.addNewPortfolios(list);
                        setPhotographersListVisibility(true);
                        showPhotographer(lastShowedPhotographerId);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showPhotographerLocal(int position) {
        if(picturesAdapter != null) {
            picturesAdapter.imageUrlList = portfoliosAdapter.getItem(position).getListOfPhotoReferences();
            picturesAdapter.notifyDataSetChanged();
        }

        setPhotographerVisibility(true);
    }

    @Override
    public void onClick(int position) {
        showPhotographer(lastShowedPhotographerId = position);
    }

    private void showPhotographer(int position) {
        if(position == -1) return;

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            showPhotographerLocal(position);
        else showPhotographerRemote(position);
    }

    private void showPhotographerRemote(int position) {
        Intent intent = new Intent(getActivity(), PhotographerActivity.class);
        intent.putExtra(SELECTED_PORTFOLIO_ID, portfoliosAdapter.getItem(position));
        startActivity(intent);
    }


    public void setPhotographersListVisibility(boolean visible) {
        if(photographersEmptyView != null) {
            photographersEmptyView.setVisibility(visible?View.GONE:View.VISIBLE);
            portfolioListView.setVisibility(visible?View.VISIBLE:View.GONE);
        }
    }

    public void setPhotographerVisibility(boolean visible) {
        if(photographerEmptyView != null && photographerContainer != null) {
            photographerEmptyView.setVisibility(visible?View.GONE:View.VISIBLE);
            photographerContainer.setVisibility(visible?View.VISIBLE:View.GONE);
        }
    }
}
