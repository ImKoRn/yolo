package com.korn.im.yolo.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.korn.im.yolo.R;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreHolder> {
    private Context context;
    private List<String> list;

    public GenreAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public GenreHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GenreHolder(LayoutInflater.from(context).inflate(R.layout.genre_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GenreHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list != null?list.size() : 0;
    }

    public class GenreHolder extends RecyclerView.ViewHolder {
        TextView genreTitleView;
        public GenreHolder(View itemView) {
            super(itemView);
            genreTitleView = (TextView) itemView.findViewById(R.id.genreTitleView);
        }

        public void bind(String genre) {
            genreTitleView.setText(genre);
        }
    }
}
