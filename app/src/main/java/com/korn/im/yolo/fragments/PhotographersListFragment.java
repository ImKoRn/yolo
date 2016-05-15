package com.korn.im.yolo.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.common.PhotographersAdapter;
import com.korn.im.yolo.objects.Photograph;
import com.korn.im.yolo.ui.PhotographerActivity;

/**
 * Fragment of photographers list
 */
public class PhotographersListFragment extends Fragment implements PhotographersAdapter.OnListItemClicked {
    public static final String PHOTOGRAPH_ITEM = "photographItem";
    private static final String LAST_SHOWED_ITEM_ID = "lastShowedItemId";
    private RecyclerView photographersList;
    private PhotographersAdapter photographersAdapter;

    private TextView photographersEmptyView;
    private TextView photographerEmptyView;
    private View photographerContainer;

    private int lastShowedPhotographerId = -1;
    private ImageView photographPhoto;
    private TextView descriptionView;
    private TextView nameSurnameView;

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

        setPhotographersListVisibility(!photographersAdapter.isEmpty());

        if(lastShowedPhotographerId != -1 &&
                getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            showPhotographerLocal(lastShowedPhotographerId);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState != null) {
            outState.putInt(LAST_SHOWED_ITEM_ID, lastShowedPhotographerId);
        }
    }

    private void initUi(View view) {
        photographPhoto = (ImageView) view.findViewById(R.id.photographPhoto);
        nameSurnameView = (TextView) view.findViewById(R.id.nameSurnameView);
        descriptionView = (TextView) view.findViewById(R.id.descriptionView);

        photographerContainer = view.findViewById(R.id.photographerContainer);
        photographersEmptyView = (TextView) view.findViewById(R.id.photographersEmptyView);
        photographerEmptyView = (TextView) view.findViewById(R.id.photographerEmptyView);


        photographersList = (RecyclerView) view.findViewById(R.id.photographersList);
        photographersList.setAdapter(photographersAdapter = new PhotographersAdapter(getActivity()));
        photographersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        photographersAdapter.setOnListItemClicked(this);

        photographersAdapter.addPhotograph(new Photograph("Александр Сергиенко", 100, "Александр Сергиенко - фотограф профессионал!\n" +
                "\n" +
                "Снимает в различных жанрах съемки: beauty, fashion, snapshot, modeltest, lookbook, lovestory, свадебное фото, семейное (беременные, дети, семьи), предметное фото, рекламное фото, каталоги, репортаж. \n" +
                "\n" +
                "Ответственно подходит к фотосъемке, прорабатывает концепцию фотосъемки, подбирает локацию, уделяет пристальное внимание свету, позе, эмоциям, положению тела для достижения высокого результата!"));
    }

    @Override
    public void onClick(int position) {
        showPhotographer(lastShowedPhotographerId = position);
    }

    private void showPhotographer(int position) {
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            showPhotographerLocal(position);
        else showPhotographerRemote(position);
    }

    private void showPhotographerRemote(int position) {
        Intent intent = new Intent(getActivity(), PhotographerActivity.class);
        intent.putExtra(PHOTOGRAPH_ITEM, photographersAdapter.getItem(position));
        startActivity(intent);
    }

    private void showPhotographerLocal(int position) {
        descriptionView.setText(photographersAdapter.getItem(position).getDescription());
        nameSurnameView.setText(photographersAdapter.getItem(position).getName());
        setPhotographerVisibility(true);
    }

    public void setPhotographersListVisibility(boolean visible) {
        if(photographersEmptyView != null) {
            photographersEmptyView.setVisibility(visible?View.GONE:View.VISIBLE);
            photographersList.setVisibility(visible?View.VISIBLE:View.GONE);
        }
    }

    public void setPhotographerVisibility(boolean visible) {
        if(photographerEmptyView != null && photographerContainer != null) {
            photographerEmptyView.setVisibility(visible?View.GONE:View.VISIBLE);
            photographerContainer.setVisibility(visible?View.VISIBLE:View.GONE);
        }
    }
}
