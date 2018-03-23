package com.physicmaster.modules.study.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.modules.study.fragment.infromation.EnergyFragment;
import com.physicmaster.modules.study.fragment.infromation.FreshFragment;
import com.physicmaster.modules.study.fragment.infromation.FriendFragment;
import com.physicmaster.net.response.user.PetInfoResponse.DataBean.NewMsgCountVoBean;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.widget.PagerTab;
import com.physicmaster.widget.TitleBuilder;

public class InformationActivity extends BaseActivity implements MsgChangeListener {

    private ViewPager vpInformation;
    private PagerTab ptInformation;
    private NewMsgCountVoBean msgCountVoBean;
    private int[] msgs;

    @Override
    protected void findViewById() {
        ptInformation = (PagerTab) findViewById(R.id.pt_information);
        vpInformation = (ViewPager) findViewById(R.id.vp_information);
        initTitle();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                }).setMiddleTitleText("信息");
    }

    @Override
    protected void initView() {
        vpInformation.setAdapter(new InformationAdapter(getSupportFragmentManager()));
        ptInformation.setViewPager(vpInformation);
        final int position = getIntent().getIntExtra("position", 0);
        vpInformation.setCurrentItem(position);
        ptInformation.selectTab(position);
        msgCountVoBean = (NewMsgCountVoBean) getIntent().getSerializableExtra("newMsgs");
        if (msgCountVoBean == null) {
            return;
        }
        msgs = new int[3];
        msgs[0] = msgCountVoBean.freshNews;
        msgs[1] = msgCountVoBean.gameEnergyRequest;
        msgs[2] = msgCountVoBean.relationInvitation;
        ptInformation.setMsgs(msgs);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_information;
    }

    class InformationAdapter extends FragmentPagerAdapter {

        private String[] mTabNames;
        private SparseArray<BaseFragment2> sFragments = new SparseArray<>();

        public InformationAdapter(FragmentManager fm) {
            super(fm);
            mTabNames = ScreenUtils.getStringArray(R.array.information_tab_names);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment2 fragment = sFragments.get(position);
            if (fragment == null) {
                switch (position) {
                    case 0:
                        fragment = new FreshFragment();
                        ((FreshFragment) fragment).setMsgChangeListener(InformationActivity.this);
                        break;
                    case 1:
                        fragment = new EnergyFragment();
                        ((EnergyFragment) fragment).setMsgChangeListener(InformationActivity.this);
                        break;
                    case 2:
                        fragment = new FriendFragment();
                        ((FriendFragment) fragment).setMsgChangeListener(InformationActivity.this);
                        break;
                }
                sFragments.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mTabNames.length;
        }

        public CharSequence getPageTitle(int position) {
            return mTabNames[position];
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMsgChanged(int position, int disCount) {
        if (null == msgs) {
            return;
        }
        if (position == 0) {
            if (disCount == -1) {
                msgs[0] = 0;
            } else {
                msgs[0] -= 1;
            }
        } else if (position == 1) {
            if (disCount == -1) {
                msgs[1] = 0;
            } else {
                msgs[1] -= 1;
            }
        } else if (position == 2) {
            if (disCount == -1) {
                msgs[2] = 0;
            } else {
                msgs[2] -= 1;
            }
        }
        ptInformation.setMsgs(msgs);
    }
}
