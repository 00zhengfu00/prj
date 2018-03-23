package com.physicmaster.modules.guide;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.physicmaster.R;

/**
 * Created by huashigen on 2017-07-05.
 */

public class GuideDialogFragment4 {

    private PaintView4 paintView;
    private FrameLayout mParentView;
    private final View rootView;

    public GuideDialogFragment4(Activity activity, Bundle bundle) {
        mParentView = (FrameLayout) activity.getWindow().getDecorView();
        rootView = activity.getLayoutInflater().inflate(R.layout.dialogfragment_guide4, mParentView, false);
        rootView.setOnClickListener(v -> {

        });
        paintView = rootView.findViewById(R.id.view_paint);
        ExposureView view1 = bundle.getParcelable("view1");
        paintView.setOnBtnOkClickListener(v -> dismiss());
        paintView.addView(view1);
        mParentView.addView(rootView);
    }

    public void dismiss() {
        if (rootView != null) {
            mParentView.removeView(rootView);
        }
    }
}
