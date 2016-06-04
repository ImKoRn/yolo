package com.korn.im.yolo.common;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

/**
 * Manage dividing between items depending orientation
 */
public class ItemDecorator extends RecyclerView.ItemDecoration {
    private final int PORTRAIT_MARGIN_PX;
    private final int LANDSCAPE_MARGIN_PX;
    private final Context context;

    public ItemDecorator(Context context, int portraitMarginDp, int landscapeMarginDp) {
        this.context = context;
        PORTRAIT_MARGIN_PX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                portraitMarginDp, context.getResources().getDisplayMetrics());
        LANDSCAPE_MARGIN_PX = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                landscapeMarginDp, context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ?
                LANDSCAPE_MARGIN_PX : PORTRAIT_MARGIN_PX;
    }
}