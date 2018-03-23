package com.lswuyou.tv.pm.activity;

import android.os.Bundle;
import android.view.View;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.Constant;
import com.lswuyou.tv.pm.leanback.CollectFragment;
import com.lswuyou.tv.pm.net.response.account.CollectVideo;
import com.lswuyou.tv.pm.view.TitleBarView;

import reco.frame.tv.view.TvTabHost;

public class MyCollectActivity extends BaseFragmentActivity {
    private TitleBarView mTitleBarView;
    private CollectVideo favVideoVo;
    private TvTabHost tth_container;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.myCollect);
        loadFrag();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_collect;
    }

    private void loadFrag() {

        tth_container = (TvTabHost) findViewById(R.id.tth_container);

        //添加初中物理
        CollectFragment fragmentPhysics = new CollectFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("subjectId", Constant.SUB_PHYSICS);
        fragmentPhysics.setArguments(bundle2);
        tth_container.addPage(getFragmentManager(), fragmentPhysics, "初中物理");

        //添加初中化学
        CollectFragment fragmentChemistry = new CollectFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("subjectId", Constant.SUB_CHEMISTRY);
        fragmentChemistry.setArguments(bundle3);
        tth_container.addPage(getFragmentManager(), fragmentChemistry, "初中化学");

        //添加初中数学
        CollectFragment fragmentMath = new CollectFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("subjectId", Constant.SUB_MATH);
        fragmentMath.setArguments(bundle1);
        tth_container.addPage(getFragmentManager(), fragmentMath, "初中数学");
        //设监听
        tth_container.buildLayout();
        tth_container.setCurrentPage(0);
    }
}
