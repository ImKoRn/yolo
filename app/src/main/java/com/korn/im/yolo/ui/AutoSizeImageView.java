package com.korn.im.yolo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.korn.im.yolo.R;

/**
 * Image view that auto corrects self size by height or width
 */
public class AutoSizeImageView extends ImageView {
    private static final int WRAP_BY_HEIGHT = 0;
    private static final int WRAP_BY_WIDTH = 1;

    private int wrapBy = 0;

    public AutoSizeImageView(Context context) {
        super(context);
    }

    public AutoSizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.AutoSizeImageView, 0, 0);

        try {
            wrapBy = typedArray.getInt(R.styleable.AutoSizeImageView_wrapBy, 0);
        } finally {
            typedArray.recycle();
        }
    }

    public AutoSizeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.AutoSizeImageView, 0, 0);

        try {
            wrapBy = typedArray.getInt(R.styleable.AutoSizeImageView_wrapBy, 0);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Bitmap bitmap = null;
        if(getDrawable() instanceof  BitmapDrawable)
            bitmap = ((BitmapDrawable) getDrawable()).getBitmap();

        if(bitmap != null) {
            float scale = (float) bitmap.getWidth() / (float) bitmap.getHeight();
            int height = 0;
            int width = 0;

            switch (wrapBy) {
                case WRAP_BY_HEIGHT : {
                    height = MeasureSpec.getSize(heightMeasureSpec);
                    width = MeasureSpec.getSize(Math.round(height * scale));
                    break;
                }
                case WRAP_BY_WIDTH : {
                    width = MeasureSpec.getSize(widthMeasureSpec);
                    height = MeasureSpec.getSize(Math.round(width / scale));
                    break;
                }
            }

            setMeasuredDimension(width, height);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
