package com.korn.im.yolo.common;

import android.support.v7.widget.RecyclerView;

/**
 * Using for downloading data when recycler view at bottom
 */
public class AutoUpdateOnScrollListener extends RecyclerView.OnScrollListener {
    private boolean isLastItems = false;

    public boolean isLastItems() {
        return isLastItems;
    }

    public void setLastItems(boolean lastItems) {
        isLastItems = lastItems;
    }
}
