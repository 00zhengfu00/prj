package com.lswuyou.tv.pm.utils;

import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.lswuyou.tv.pm.R;

/**
 * Created by huashigen on 2017-09-06.
 */

public class AnimationUtil {
    /**
     * view 上下抖动
     *
     * @param shakeView
     */

    public static void upDownShake(View shakeView) {
        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(shakeView.getContext(), R.anim.shake_up_down);
        animationSet.setRepeatCount(3);
        shakeView.startAnimation(animationSet);
    }

    /**
     * view 上下抖动
     *
     * @param shakeView
     */
    public static void leftRightShake(View shakeView) {
        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(shakeView.getContext(), R.anim.shake_left_right);
        animationSet.setRepeatCount(3);
        shakeView.startAnimation(animationSet);
    }
}
