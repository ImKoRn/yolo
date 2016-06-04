package com.korn.im.yolo.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.korn.im.yolo.R;
import com.korn.im.yolo.objects.Portfolio;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by korn on 04.06.16.
 */
public class PortfolioPicturesAdapter extends RecyclerView.Adapter<PortfolioPicturesAdapter.PictureHolder> {
    public List<String> imageUrlList = new ArrayList<>();

    private Context context;

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
        private ImageView image;
        public String url;

        public PictureHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.photo);
        }

        public void clear() {
            image.setImageBitmap(null);
        }

        public void bind() {
            ImageLoader.getInstance().displayImage(url, image);
        }
    }
}
