package com.korn.im.yolo.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.common.AutoUpdateOnScrollListener;
import com.korn.im.yolo.common.PortfolioPicturesAdapter;
import com.korn.im.yolo.common.PortfoliosAdapter;
import com.korn.im.yolo.loaders.DataLoader;
import com.korn.im.yolo.objects.Portfolio;
import com.korn.im.yolo.activity.PortfolioActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment of photographers imageUrlList
 */
public class PortfolioListFragment extends Fragment implements PortfoliosAdapter.OnListItemClicked {
    private static final String ORDER_SUBJECT = "Order";
    private static final String CONTACTS_FIELD = "Contacts:\n";
    private static final String PHOTOGRAPHER_FIELD = "Photographer:\n";

    private static final Uri TARGET_URI = Uri.parse("mailto:kor.wolf13@gmail.com");

    private static final String LAST_SHOWED_ITEM_ID = "lastShowedItemId";

    public static final String SELECTED_PORTFOLIO = "photographItem";

    private PortfoliosAdapter portfoliosAdapter;

    private PortfolioPicturesAdapter picturesAdapter;


    private TextView emptyListTextView;
    private TextView photographerEmptyView;
    private View photographerContainer;
    private SwipeRefreshLayout swipeRefreshView;

    private AutoUpdateOnScrollListener scrollListener;

    private int lastShowedPhotographerId = -1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            lastShowedPhotographerId = savedInstanceState.getInt(LAST_SHOWED_ITEM_ID);
        }

        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(view == null) {
            view = inflater.inflate(R.layout.fragment_portfolio_list, container, false);
        }

        initUi(view);

        setHasOptionsMenu(true);

        return view;
    }
    private void initUi(View view) {
        photographerContainer = view.findViewById(R.id.photographerContainer);
        emptyListTextView = (TextView) view.findViewById(R.id.emptyListTextView);
        photographerEmptyView = (TextView) view.findViewById(R.id.photographerEmptyView);


        RecyclerView picturesViewListView = (RecyclerView) view.findViewById(R.id.picturesViewList);

        if(picturesViewListView != null) {
            picturesViewListView.setAdapter(picturesAdapter = new PortfolioPicturesAdapter(getActivity()));
            picturesViewListView.setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.HORIZONTAL, false));
        }

        FloatingActionButton sendBtn = (FloatingActionButton) view.findViewById(R.id.sendBtn);
        if(sendBtn != null)
            sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createBottomSheet();
            }
        });

        swipeRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshView);
        swipeRefreshView.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(DataLoader.REQUEST_LOAD_NEW_DATA);
            }
        });

        RecyclerView portfolioListView = (RecyclerView) view.findViewById(R.id.photographersList);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity()) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        portfolioListView.addOnScrollListener(scrollListener = new AutoUpdateOnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(llm.findLastVisibleItemPosition() == portfoliosAdapter.getItemCount() - 1 &&
                        !isLastItems()) {
                    DataLoader.getInstance().sendRequest(DataLoader.REQUEST_LOAD_NEW_DATA, Portfolio.CATEGORY_ID,
                            portfoliosAdapter.getItemCount(),
                            new DataLoader.ResultListener<Portfolio>() {
                                @Override
                                public void onResult(List<Portfolio> list) {
                                    if(list.size() == 0)
                                        setLastItems(true);
                                    portfoliosAdapter.loading(false);
                                    portfoliosAdapter.addData(list);
                                }
                            });
                    portfoliosAdapter.loading(true);
                }
            }
        });
        portfolioListView.setLayoutManager(llm);
        portfolioListView.setAdapter(portfoliosAdapter = new PortfoliosAdapter(getActivity(), llm));

        portfoliosAdapter.setOnListItemClicked(this);
    }

    private void refresh(int request) {
        DataLoader.getInstance().sendRequest(request, Portfolio.CATEGORY_ID,
                new DataLoader.ResultListener<Portfolio>() {
                    @Override
                    public void onResult(List<Portfolio> list) {
                        portfoliosAdapter.addNewPortfolios(list);
                        setPhotographersListVisibility(portfoliosAdapter.getItemCount() > 0);
                        scrollListener.setLastItems(false);
                        if(swipeRefreshView.isRefreshing()) swipeRefreshView.setRefreshing(false);
                    }
                });
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
        refresh(DataLoader.REQUEST_RESTORE_DATA);
    }

    private void createBottomSheet() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.options, null));
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        ((NavigationView) bottomSheetDialog.findViewById(R.id.navView))
                .setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.emailOnly : {
                                sendOrderEmail(false);
                                break;
                            }
                            case R.id.emailAndPhone : {
                                sendOrderEmail(true);
                                break;
                            }
                        }
                        bottomSheetDialog.cancel();
                        return true;
                    }
                });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
    }

    private void sendOrderEmail(boolean appendPhoneAndEmail) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(TARGET_URI);
        intent.putExtra(Intent.EXTRA_SUBJECT, ORDER_SUBJECT);
        intent.putExtra(Intent.EXTRA_TEXT, createText(appendPhoneAndEmail));
        startActivity(Intent.createChooser(intent, "Choice email client"));
    }

    private String createText(boolean appendPhoneAndEmail) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(PHOTOGRAPHER_FIELD)
                .append(portfoliosAdapter.getItem(lastShowedPhotographerId).getTitle()).append('\n');

        stringBuilder.append(CONTACTS_FIELD)
                .append("Email: ").append(getEmail().get(0)).append('\n');
        if (appendPhoneAndEmail) stringBuilder.append("Phone: ").append(getPhoneNumber()).append('\n');

        return stringBuilder.toString();
    }


    private String getPhoneNumber() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    private ArrayList<String> getEmail() {
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();

        ArrayList<String> emails = new ArrayList<>();

        for (Account account : accounts) {
            if(Patterns.EMAIL_ADDRESS.matcher(account.name).matches())
                emails.add(account.name);
        }

        return emails;
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
        Intent intent = new Intent(getActivity(), PortfolioActivity.class);
        intent.putExtra(SELECTED_PORTFOLIO, portfoliosAdapter.getItem(position));
        startActivity(intent);
    }


    private void setPhotographersListVisibility(boolean visible) {
        if(emptyListTextView != null) {
            emptyListTextView.setVisibility(visible?View.GONE:View.VISIBLE);
        }
    }

    private void setPhotographerVisibility(boolean visible) {
        if(photographerEmptyView != null && photographerContainer != null) {
            photographerEmptyView.setVisibility(visible?View.GONE:View.VISIBLE);
            photographerContainer.setVisibility(visible?View.VISIBLE:View.GONE);
        }
    }
}
