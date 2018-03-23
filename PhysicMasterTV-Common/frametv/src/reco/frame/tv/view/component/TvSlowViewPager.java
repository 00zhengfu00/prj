package reco.frame.tv.view.component;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class TvSlowViewPager extends ViewPager {

    private int duration;

    public TvSlowViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean canScroll = true;

    public TvSlowViewPager(Context context, int duration) {
        super(context);
        this.duration = duration;
        setScroll();
    }

    public void setScroll() {

        Field mField;
        try {
            mField = ViewPager.class.getDeclaredField("mScroller");

            mField.setAccessible(true);
            FixedSpeedScroller mScroller = new FixedSpeedScroller(getContext(),
                    new AccelerateInterpolator());

            try {
                mField.set(this, mScroller);
                mScroller.setmDuration(duration);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    class FixedSpeedScroller extends Scroller {
        private int mDuration = 1500;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public void setmDuration(int time) {
            mDuration = time;
        }

        public int getmDuration() {
            return mDuration;
        }
    }

    public boolean isCanScroll() {
        return canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }
}
