package com.korn.im.yolo.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.korn.im.yolo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows pictures
 */
public class PortfolioPicturesAdapter extends RecyclerView.Adapter<PortfolioPicturesAdapter.PictureHolder> {
    public List<String> imageUrlList = new ArrayList<>();

    private final Context context;

    public PortfolioPicturesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PictureHolder(LayoutInflater.from(context).inflate(R.layout.rv_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
        holder.clear();
        holder.url = imageUrlList.get(position);
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    public class PictureHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        public String url;

        public PictureHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.photo);
        }

        public void clear() {
            image.setImageResource(R.drawable.image_filter_hdr);
        }

        public void bind() {
            ImageLoader.getInstance().displayImage(url, image);
        }
    }
}
