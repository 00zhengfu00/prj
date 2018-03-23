package com.physicmaster.modules.study.fragment.widget.dynamicbg;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;


/**
 * Created by huashigen on 2017-06-26.
 */

public class MainPageRecyclerView extends RecyclerView {
    private static final String TAG = "MainPageRecyclerView";
    private Logger logger = AndroidLogger.getLogger(TAG);
    private float startPosition;

    public void setOnTopOverScrollListener(OnTopOverScrollListener onTopOverScrollListener) {
        this.onTopOverScrollListener = onTopOverScrollListener;
    }

    private OnTopOverScrollListener onTopOverScrollListener;

    public MainPageRecyclerView(Context context) {
        super(context);
        addScrollListener();
    }

    public MainPageRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addScrollListener();
    }

    public MainPageRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addScrollListener();
    }

    private void addScrollListener() {
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (!canScrollVertically(-1)) {
            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                startPosition = e.getY();
            } else if (e.getAction() == MotionEvent.ACTION_MOVE) {
                float moveDis = e.getY() - startPosition;
                if (moveDis > 0 && onTopOverScrollListener != null) {
                    onTopOverScrollListener.onTopOverScroll(moveDis);
                }
                logger.debug("onTouchEvent moveDis:" + moveDis);
            } else if (e.getAction() == MotionEvent.ACTION_UP) {
                if (onTopOverScrollListener != null) {
                    onTopOverScrollListener.onTopOverScroll(0);
                }
            }
        }
        return super.onTouchEvent(e);
    }

    public interface OnTopOverScrollListener {
        void onTopOverScroll(float scrollY);
    }
}
