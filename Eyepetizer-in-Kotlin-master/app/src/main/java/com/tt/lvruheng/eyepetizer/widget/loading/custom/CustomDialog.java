package com.tt.lvruheng.eyepetizer.widget.loading.custom;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.tt.lvruheng.eyepetizer.R;

/**
 * Created by huashigen on 2018-03-01.
 */

public class CustomDialog extends Dialog {

    public CustomDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        setContentView(R.layout.layout_custom);
        startAnimation();
    }

    private void startAnimation() {
        ImageView ivProress = findViewById(R.id.iv_progress);
        Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setRepeatCount(10000);
        rotateAnimation.setDuration(1000);
        ivProress.startAnimation(rotateAnimation);
    }
}
