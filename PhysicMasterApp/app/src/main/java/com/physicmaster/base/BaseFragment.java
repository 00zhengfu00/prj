package com.physicmaster.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.physicmaster.utils.ViewUtils;


public abstract class BaseFragment extends Fragment {
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (null == rootView) {
            rootView = inflater.inflate(getContentLayout(), container, false);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    protected abstract int getContentLayout();

    protected abstract void initView();

    /**
     * 自动弹出编辑框
     */
    public void showInputSoft(final View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                ViewUtils.showInputSoft(getActivity(), view);
            }
        }, 500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }
}
