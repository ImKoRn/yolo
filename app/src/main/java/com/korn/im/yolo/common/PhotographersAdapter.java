package com.korn.im.yolo.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.objects.Photograph;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for photographer list has two views SHORT_INFO_VIEW and DETAIL_INFO_VIEW.
 */
public class PhotographersAdapter extends RecyclerView.Adapter<PhotographersAdapter.PhotographHolder> {
    public static final int SHORT_INFO_VIEW = 0;
    public static final int DETAIL_INFO_VIEW = 1;

    private ArrayList<Photograph> list = new ArrayList<>();

    private int itemViewType = 0;
    private Context context;

    /**
     * @param context - context in which adapter uses
     * @param itemViewType - SHORT_INFO_VIEW or DETAIL_INFO_VIEW, else produce IllegalArgumentException
     */
    public PhotographersAdapter(Context context, int itemViewType) {
        if (itemViewType != SHORT_INFO_VIEW &&
                itemViewType != DETAIL_INFO_VIEW)
            throw new IllegalArgumentException("Wrong item view type");
        this.itemViewType = itemViewType;
        this.context = context;
    }

    public void addPhotograph(Photograph photograph) {
        list.add(photograph);
    }

    public void addAllPhotographs(List<Photograph> photographs) {
        list.addAll(photographs);
    }

    @Override
    public int getItemViewType(int position) {
        return itemViewType;
    }

    @Override
    public PhotographHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotographHolder holder = null;

        switch (viewType) {
            case SHORT_INFO_VIEW: {
                holder = new ShortPhotographHolder(LayoutInflater.from(context)
                        .inflate(R.layout.short_photograph_item, parent, false));
                break;
            }
            case DETAIL_INFO_VIEW: {
                holder = new DetailPhotographHolder(LayoutInflater.from(context)
                        .inflate(R.layout.detail_photograph_item, parent, false));
                break;
            }
        }

        return holder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(PhotographHolder holder, int position) {
        holder.bind(list.get(position));
    }

    public abstract class PhotographHolder extends RecyclerView.ViewHolder {
        public PhotographHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(Photograph photograph);
    }

    public class ShortPhotographHolder extends PhotographHolder {
        private final TextView nameSurnameView;
        private TextView descriptionView;

        private Photograph photograph;

        public ShortPhotographHolder(View itemView) {
            super(itemView);
            nameSurnameView = (TextView) itemView.findViewById(R.id.nameSurnameView);
            descriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
        }

        @Override
        public void bind(Photograph photograph) {
            this.photograph = photograph;
            nameSurnameView.setText(photograph.getName());
            descriptionView.setText(photograph.getDescription());
        }
    }

    public class DetailPhotographHolder extends PhotographHolder {
        public DetailPhotographHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Photograph photograph) {

        }
    }
}
