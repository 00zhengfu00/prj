package com.lswuyou.tv.pm.channel.login;

import android.view.View;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.BaseActivity;
import com.lswuyou.tv.pm.view.TitleBarView;

public class AliplayLoginActivity extends BaseActivity {
    private TitleBarView mTitleBarView;
    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.login);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_aliplay_login;
    }
}
