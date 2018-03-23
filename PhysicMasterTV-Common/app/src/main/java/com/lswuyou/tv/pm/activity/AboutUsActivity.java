package com.lswuyou.tv.pm.activity;

import android.view.View;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.view.TitleBarView;

public class AboutUsActivity extends BaseActivity {
    private TitleBarView mTitleBarView;
    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.aboutUs);
        mTitleBarView.setBtnRight(0, R.string.aboutUs);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about_us;
    }
}
