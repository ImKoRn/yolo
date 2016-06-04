package com.korn.im.yolo.common;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.objects.Portfolio;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter for photographer imageUrlList has two views LANDSCAPE and PORTRAIT.
 */
public class PortfoliosAdapter extends RecyclerView.Adapter<PortfoliosAdapter.PhotographHolder> {
    private final LinearLayoutManager linearLayoutManager;
    private List<Portfolio> list = new ArrayList<>();

    private final Context context;

    private OnListItemClicked onListItemClickedListener;
    private int lastCheckedView = -1;

    /**
     * @param context - context in which adapter uses
     */
    public PortfoliosAdapter(Context context, LinearLayoutManager linearLayoutManager)
    {
        this.context = context;
        this.linearLayoutManager = linearLayoutManager;
    }

    public void addPortfolio(Portfolio portfolio) {
        list.add(portfolio);
    }

    public void addNewPortfolios(List<Portfolio> portfolios) {
        list.clear();
        list.addAll(portfolios);
        notifyDataSetChanged();
    }

    public void addAllPortfolios(List<Portfolio> portfolios) {
        list.addAll(portfolios);
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
        holder.clear();
        holder.setOnClickListener(onListItemClickedListener);
        holder.bind(list.get(position));
    }

    public void setOnListItemClicked(OnListItemClicked listener) {
        this.onListItemClickedListener = listener;
    }

    public Portfolio getItem(int position) {
        return list.get(position);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void setData(List<Portfolio> data) {
        this.list = data;
    }

    @Override
    public void onViewRecycled(PhotographHolder holder) {
        super.onViewRecycled(holder);
        holder.unbind();
    }

    public ArrayList<Portfolio> getData() {
        return (ArrayList<Portfolio>) list;
    }

    public int size() {
        return list.size();
    }

    public void setClickedItem(int clickedItem) {
        this.lastCheckedView = clickedItem;
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

        public abstract void bind(Portfolio portfolio);

        public abstract void clear();

        public abstract void unbind();

        @Override
        public void onClick(View v) {
            if(onListItemClickedListener != null)
                onListItemClickedListener.onClick(getAdapterPosition());
        }
    }

    public class ShortPhotographHolder extends PhotographHolder {
        private final TextView nameSurnameView;
        private final TextView descriptionView;

        private Portfolio portfolio;

        public ShortPhotographHolder(View itemView) {
            super(itemView);
            nameSurnameView = (TextView) itemView.findViewById(R.id.nameSurnameView);
            descriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
        }

        @Override
        public void bind(Portfolio portfolio) {
            this.portfolio = portfolio;
            if(lastCheckedView == getAdapterPosition())
                descriptionView.setVisibility(View.VISIBLE);
            nameSurnameView.setText(portfolio.getTitle());
            descriptionView.setText(portfolio.getContent());
        }

        @Override
        public void clear() {
            nameSurnameView.setText(null);
            descriptionView.setText(null);
            descriptionView.setVisibility(View.GONE);
        }

        @Override
        public void unbind() {

        }

        @Override
        public void onClick(View v) {
            super.onClick(v);
            if(lastCheckedView != -1) {
                View view = linearLayoutManager.findViewByPosition(lastCheckedView);
                if (view != null) view.findViewById(R.id.descriptionView).setVisibility(View.GONE);
            }
            lastCheckedView = getAdapterPosition();
            descriptionView.setVisibility(View.VISIBLE);
            linearLayoutManager.scrollToPositionWithOffset(lastCheckedView, 0);
        }
    }

    public class DetailPhotographHolder extends PhotographHolder {
        private final TextView nameSurnameView;
        private final TextView descriptionView;
        private final CircleImageView photographerPhotoView;

        private Portfolio portfolio;

        public DetailPhotographHolder(View itemView) {
            super(itemView);
            nameSurnameView = (TextView) itemView.findViewById(R.id.nameSurnameView);
            descriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
            photographerPhotoView = (CircleImageView) itemView.findViewById(R.id.photographPhoto);
        }

        @Override
        public void bind(Portfolio portfolio) {
            this.portfolio = portfolio;
            nameSurnameView.setText(portfolio.getTitle());
            descriptionView.setText(portfolio.getContent());
            if(portfolio.getIconReference() != null)
                ImageLoader.getInstance().displayImage(portfolio.getIconReference(), photographerPhotoView);
        }

        @Override
        public void clear() {
            nameSurnameView.setText(null);
            descriptionView.setText(null);
            photographerPhotoView.setImageBitmap(null);
        }

        @Override
        public void unbind() {
            ImageLoader.getInstance().cancelDisplayTask(photographerPhotoView);
        }
    }
}
