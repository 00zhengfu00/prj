package com.physicmaster.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.Checkable;

/**
 * Created by songrui on 18/1/26.
 */

public class CheckableTextView extends AppCompatTextView implements Checkable {


    private boolean mChecked;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public CheckableTextView(Context context) {
        super(context);
    }


    public CheckableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }


    @Override
    public boolean isChecked() {
        return mChecked;
    }


    @Override
    public void setChecked(boolean checked) {
        if (this.mChecked != checked) {
            this.mChecked = checked;
            refreshDrawableState();
        }
    }


    @Override
    public void toggle() {
        mChecked = !mChecked;
    }
}
