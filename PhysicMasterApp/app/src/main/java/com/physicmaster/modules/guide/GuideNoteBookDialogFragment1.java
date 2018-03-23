package com.physicmaster.modules.guide;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.physicmaster.R;

/**
 * Created by huashigen on 2017-07-05.
 */

public class GuideNoteBookDialogFragment1 {

    private PaintViewNoteBook1 paintView;
    private FrameLayout mParentView;
    private final View rootView;

    public GuideNoteBookDialogFragment1(Activity activity, Bundle bundle) {
        mParentView = (FrameLayout) activity.getWindow().getDecorView();
        rootView = activity.getLayoutInflater().inflate(R.layout.dialogfragment_guide_notebook1, mParentView, false);
        rootView.setOnClickListener(v -> {

        });
        paintView = rootView.findViewById(R.id.view_paint);
        ExposureView view1 = bundle.getParcelable("view1");
        paintView.addView(view1);
        mParentView.addView(rootView);
    }

    public void setPaintViewOnClickListener(View.OnClickListener onClickListener) {
        paintView.setOnBtnOkClickListener(onClickListener);
    }

    public void dismiss() {
        if (rootView != null) {
            mParentView.removeView(rootView);
        }
    }
}