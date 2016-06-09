package com.korn.im.yolo.common;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.korn.im.yolo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PicturesAdapter extends PagerAdapter {
    public List<String> imageUrlList = new ArrayList<>();
    private final Context context;

    public PicturesAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, container, false);

        ImageLoader.getInstance().displayImage(imageUrlList.get(position),
                (ImageView) view.findViewById(R.id.photographPhoto));

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        ImageLoader.getInstance().cancelDisplayTask((ImageView) ((View) object).findViewById(R.id.photographPhoto));
    }
}
