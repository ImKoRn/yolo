package com.korn.im.yolo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.korn.im.yolo.R;
import com.korn.im.yolo.common.PhotographersAdapter;
import com.korn.im.yolo.objects.Photograph;

/**
 * Fragment of photographers list
 */
public class PhotographersListFragment extends Fragment {
    private RecyclerView photographersList;
    private PhotographersAdapter photographersAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if(view == null) {
            view = inflater.inflate(R.layout.fragment_photographers_list, container, false);
        }

        initUi(view);

        return view;
    }

    private void initUi(View view) {
        photographersList = (RecyclerView) view.findViewById(R.id.photographersList);
        photographersList.setAdapter(photographersAdapter = new PhotographersAdapter(getActivity(), PhotographersAdapter.SHORT_INFO_VIEW));
        photographersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
        photographersAdapter.addPhotograph(new Photograph("Im KoRn", 100, "Best ever"));
    }
}
