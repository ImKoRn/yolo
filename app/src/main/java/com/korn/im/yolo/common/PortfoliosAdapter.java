package com.korn.im.yolo.common;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class PortfoliosAdapter extends RecyclerView.Adapter<PortfoliosAdapter.Holder> {
    private static final int LOADER_TYPE = -1;
    private final LinearLayoutManager linearLayoutManager;
    private final List<Portfolio> list = new ArrayList<>();

    private final Context context;

    private OnListItemClicked onListItemClickedListener;
    private int lastCheckedView = -1;
    private boolean isLoading = false;

    /**
     * @param context - context in which adapter uses
     */
    public PortfoliosAdapter(Context context, LinearLayoutManager linearLayoutManager)
    {
        this.context = context;
        this.linearLayoutManager = linearLayoutManager;
    }

    public void addNewPortfolios(List<Portfolio> portfolios) {
        list.clear();
        list.addAll(portfolios);
        notifyItemRangeChanged(0, list.size());
    }

    public void addData(List<Portfolio> list) {
        this.list.addAll(list);
        notifyItemRangeInserted(this.list.size() - list.size(), list.size());
    }

    public void loading(boolean loading) {
        if(isLoading == loading) return;

        isLoading = loading;
        if(isLoading) notifyItemInserted(getItemCount());
        else notifyItemRemoved(getItemCount() + 1);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == list.size()) return LOADER_TYPE;

        return context.getResources().getConfiguration().orientation;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Holder holder = null;

        switch (viewType) {
            case Configuration.ORIENTATION_LANDSCAPE: {
                holder = new ShortPortfolioHolder(LayoutInflater.from(context)
                        .inflate(R.layout.short_portfolio_item, parent, false));
                break;
            }
            case Configuration.ORIENTATION_PORTRAIT: {
                holder = new DetailPortfolioHolder(LayoutInflater.from(context)
                        .inflate(R.layout.detail_portfolio_item, parent, false));
                break;
            }
            case LOADER_TYPE : {
                holder = new LoaderHolder(LayoutInflater.from(context).inflate(R.layout.loader_item, parent, false));
                break;
            }
        }

        return holder;
    }

    @Override
    public int getItemCount() {
        return list.size() + (isLoading? 1 : 0);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if(position != list.size()) {
            PortfolioHolder portfolioHolder = (PortfolioHolder) holder;
            portfolioHolder.clear();
            portfolioHolder.setOnClickListener(onListItemClickedListener);
            portfolioHolder.bind(list.get(position));
        }
    }

    public void setOnListItemClicked(OnListItemClicked listener) {
        this.onListItemClickedListener = listener;
    }

    public Portfolio getItem(int position) {
        return list.get(position);
    }

    @Override
    public void onViewRecycled(Holder holder) {
        super.onViewRecycled(holder);
        holder.unbind();
    }

    public interface OnListItemClicked {
        void onClick(int position);
    }

    public abstract class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }

        public abstract void unbind();
    }

    public class LoaderHolder extends Holder {
        public LoaderHolder(View itemView) {
            super(itemView);
        }

        public void unbind() {}
    }

    public abstract class PortfolioHolder extends Holder implements View.OnClickListener {
        private OnListItemClicked onListItemClickedListener;

        public PortfolioHolder(View itemView) {
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

    public class ShortPortfolioHolder extends PortfolioHolder {
        private final TextView nameSurnameView;
        private final TextView descriptionView;

        public ShortPortfolioHolder(View itemView) {
            super(itemView);
            nameSurnameView = (TextView) itemView.findViewById(R.id.nameSurnameView);
            descriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
        }

        @Override
        public void bind(Portfolio portfolio) {
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

    public class DetailPortfolioHolder extends PortfolioHolder {
        private final TextView nameSurnameView;
        private final TextView descriptionView;
        private final CircleImageView photographerPhotoView;

        public DetailPortfolioHolder(View itemView) {
            super(itemView);
            nameSurnameView = (TextView) itemView.findViewById(R.id.nameSurnameView);
            descriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
            photographerPhotoView = (CircleImageView) itemView.findViewById(R.id.photographPhoto);
        }

        @Override
        public void bind(Portfolio portfolio) {
            nameSurnameView.setText(portfolio.getTitle());
            descriptionView.setText(portfolio.getContent());
            if(portfolio.getIconReference() != null)
                ImageLoader.getInstance().displayImage(portfolio.getIconReference(), photographerPhotoView);
        }

        @Override
        public void clear() {
            nameSurnameView.setText(null);
            descriptionView.setText(null);
            photographerPhotoView.setImageResource(R.drawable.account_circle);
        }

        @Override
        public void unbind() {
            ImageLoader.getInstance().cancelDisplayTask(photographerPhotoView);
        }
    }
}
