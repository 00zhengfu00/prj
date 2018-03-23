package com.physicmaster.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by huashigen on 2017-10-23.
 */

public abstract class AutoHideSoftDialog extends Dialog {
    public AutoHideSoftDialog(@NonNull Context context) {
        super(context);
    }

    public AutoHideSoftDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected AutoHideSoftDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isShowing() && shouldCloseOnTouch(getContext(),event)){
            hideKeyBoard();
        }
        return super.onTouchEvent(event);
    }
    public boolean shouldCloseOnTouch(Context context, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && isOutOfBounds(context, event) && getWindow().peekDecorView() != null) {
            return true;
        }
        return false;
    }
    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop)
                || (x > (decorView.getWidth()+slop))
                || (y > (decorView.getHeight()+slop));
    }
    public abstract void hideKeyBoard();
}
