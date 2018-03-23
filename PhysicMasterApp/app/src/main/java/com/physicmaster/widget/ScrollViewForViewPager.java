package com.physicmaster.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by huashigen on 2016/12/12.
 */

public class ScrollViewForViewPager extends ScrollView {
    private float mDownPosX;
    private float mDownPosY;
    public ScrollViewForViewPager(Context context) {
        super(context);
    }

    public ScrollViewForViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewForViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();

        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (deltaX > deltaY) {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
