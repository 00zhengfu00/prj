package com.physicmaster.modules.study.fragment.widget.dynamicbg;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by huashigen on 2017-06-26.
 */

public class RecyclerViewScrollToPercentageListener extends RecyclerView.OnScrollListener {
    private final Listener listener;
    private float scrollRatio = -1.0F;
    private static final String TAG = "DynamicBackgroundDrawab";
    private int maxScrollRange = 0;
    private int totalDy = 0;

    RecyclerViewScrollToPercentageListener(Listener paramListener) {
        this.listener = paramListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        totalDy -= dy;
        int scrollOffset = recyclerView.computeVerticalScrollOffset();
        int scrollExtent = recyclerView.computeVerticalScrollExtent();
        int scrollRange = recyclerView.computeVerticalScrollRange();
        if (scrollRange > maxScrollRange) {
            maxScrollRange = scrollRange;
        }
        if ((scrollRange > 0) && (scrollExtent > 0)) {
            float scrollPercent = 0;
            if (scrollRange - scrollExtent != 0) {
                scrollPercent = (float) (100.0D * scrollOffset / (scrollRange - scrollExtent));
            }
            if ((scrollPercent >= 0.0F) && (scrollPercent != this.scrollRatio)) {
                this.scrollRatio = scrollPercent;
                this.listener.onScroll(this.scrollRatio, totalDy);
            }
        }
    }

    public interface Listener {
        void onScroll(float scrollRatio, int scrollOffset);
    }
}
