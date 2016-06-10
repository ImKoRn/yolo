package com.korn.im.yolo.common;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.objects.News;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for showing news
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.Holder> {
    private static final int LOADER_TYPE = -1;
    private final List<News> list = new ArrayList<>();
    private final Context context;
    private OnListItemButtonClicked onListItemButtonClickedListener;
    private boolean isLoading;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == list.size()) return LOADER_TYPE;

        return super.getItemViewType(position);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == LOADER_TYPE)
            return new LoaderHolder(LayoutInflater.from(context).inflate(R.layout.loader_item, parent, false));

        return new NewsHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if(position != list.size()) {
            NewsHolder newsHolder = (NewsHolder) holder;

            newsHolder.news = list.get(position);
            newsHolder.clean();
            newsHolder.bind();
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + (isLoading? 1 : 0);
    }

    public void addNewData(List<News> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyItemRangeChanged(0, list.size());
    }

    public void addData(List<News> list) {
        this.list.addAll(list);
        notifyItemRangeInserted(this.list.size() - list.size(), list.size());
    }

    public void setOnListItemButtonClicked(OnListItemButtonClicked onListItemButtonClickedListener) {
        this.onListItemButtonClickedListener = onListItemButtonClickedListener;
    }

    public News getItem(int position) {
        return list.get(position);
    }

    public void loading(boolean loading) {
        if(isLoading == loading) return;

        isLoading = loading;
        if(isLoading) notifyItemInserted(getItemCount());
        else notifyItemRemoved(getItemCount() + 1);
    }



    public abstract class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    public class LoaderHolder extends Holder {
        public LoaderHolder(View itemView) {
            super(itemView);
        }
    }

    public class NewsHolder extends Holder {
        private final TextView titleView;
        private final TextView contentView;
        private final ImageView imageView;
        private final Button moreBtn;
        public News news;

        public NewsHolder(View itemView) {
            super(itemView);

            ((CardView) itemView).setPreventCornerOverlap(false);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onListItemButtonClickedListener.onListItemButtonClicked(getAdapterPosition());
                }
            };

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(listener);
            titleView = (TextView) itemView.findViewById(R.id.titleView);
            contentView = (TextView) itemView.findViewById(R.id.contentView);
            moreBtn = (Button) itemView.findViewById(R.id.moreBtn);
            moreBtn.setOnClickListener(listener);
        }

        public void clean() {
            imageView.setImageResource(R.drawable.image_filter_hdr);
        }

        public void bind() {
            if(news.getIconReference() != null)
                ImageLoader.getInstance().displayImage(news.getIconReference(), imageView);
            titleView.setText(news.getTitle());
            contentView.setText(news.getContent());
        }
    }

    public interface OnListItemButtonClicked {
        void onListItemButtonClicked(int position);
    }

}
