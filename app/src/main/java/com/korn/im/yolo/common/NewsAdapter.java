package com.korn.im.yolo.common;

import android.content.Context;
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
 * Created by korn on 04.06.16.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {
    private List<News> list = new ArrayList<>();
    private Context context;
    private OnListItemButtonClicked onListItemButtonClickedListener;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsHolder holder, int position) {
        holder.news = list.get(position);
        holder.clean();
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addNewData(List<News> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setOnListItemButtonClicked(OnListItemButtonClicked onListItemButtonClickedListener) {
        this.onListItemButtonClickedListener = onListItemButtonClickedListener;
    }

    public News getItem(int position) {
        return list.get(position);
    }

    public class NewsHolder extends RecyclerView.ViewHolder {
        private final TextView titleView;
        private final TextView contentView;
        private final ImageView imageView;
        private final Button moreBtn;

        private News news;

        public NewsHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            titleView = (TextView) itemView.findViewById(R.id.titleView);
            contentView = (TextView) itemView.findViewById(R.id.contentView);
            moreBtn = (Button) itemView.findViewById(R.id.moreBtn);
            moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onListItemButtonClickedListener.onListItemButtonClicked(getAdapterPosition());
                }
            });
        }

        public void clean() {
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
