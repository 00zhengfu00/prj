package com.physicmaster.widget;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

/**
 * Created by huashigen on 2017-10-27.
 */

public class CustomFragmentTabHost extends FragmentTabHost {
    public void setOnTabChagedListener(OnTabChagedListener onTabChagedListener) {
        this.onTabChagedListener = onTabChagedListener;
    }

    private OnTabChagedListener onTabChagedListener;

    public CustomFragmentTabHost(Context context) {
        super(context);
    }

    public CustomFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onTabChanged(String tabId) {
        if (onTabChagedListener != null) {
            if (onTabChagedListener.onTabChanged(tabId)) {
                super.onTabChanged(tabId);
            }
        } else {
            super.onTabChanged(tabId);
        }
    }

    public interface OnTabChagedListener {
        public boolean onTabChanged(String tabId);
    }
}
