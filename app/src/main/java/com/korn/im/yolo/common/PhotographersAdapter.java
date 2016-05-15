package com.korn.im.yolo.common;

import android.content.Context;
import android.content.res.Configuration;
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
 * Adapter for photographer list has two views LANDSCAPE and PORTRAIT.
 */
public class PhotographersAdapter extends RecyclerView.Adapter<PhotographersAdapter.PhotographHolder> {
    private ArrayList<Photograph> list = new ArrayList<>();

    private Context context;

    private OnListItemClicked onListItemClickedListener;

    /**
     * @param context - context in which adapter uses
     */
    public PhotographersAdapter(Context context) {
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
        return context.getResources().getConfiguration().orientation;
    }

    @Override
    public PhotographHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotographHolder holder = null;

        switch (viewType) {
            case Configuration.ORIENTATION_LANDSCAPE: {
                holder = new ShortPhotographHolder(LayoutInflater.from(context)
                        .inflate(R.layout.short_photograph_item, parent, false));
                break;
            }
            case Configuration.ORIENTATION_PORTRAIT: {
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
        holder.setOnClickListener(onListItemClickedListener);
        holder.bind(list.get(position));
    }

    public void setOnListItemClicked(OnListItemClicked listener) {
        this.onListItemClickedListener = listener;
    }

    public Photograph getItem(int position) {
        return list.get(position);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public interface OnListItemClicked {
        void onClick(int position);
    }

    public abstract class PhotographHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnListItemClicked onListItemClickedListener;

        public PhotographHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void setOnClickListener(OnListItemClicked listener) {
            this.onListItemClickedListener = listener;
        }

        public abstract void bind(Photograph photograph);

        @Override
        public void onClick(View v) {
            if(onListItemClickedListener != null)
                onListItemClickedListener.onClick(getAdapterPosition());
        }
    }

    public class ShortPhotographHolder extends PhotographHolder {
        private final TextView nameSurnameView;

        private Photograph photograph;

        public ShortPhotographHolder(View itemView) {
            super(itemView);
            nameSurnameView = (TextView) itemView.findViewById(R.id.nameSurnameView);
        }

        @Override
        public void bind(Photograph photograph) {
            this.photograph = photograph;
            nameSurnameView.setText(photograph.getName());
        }
    }

    public class DetailPhotographHolder extends PhotographHolder {
        private final TextView nameSurnameView;
        private final TextView descriptionView;

        private Photograph photograph;

        public DetailPhotographHolder(View itemView) {
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
}
