package com.iask.yiyuanlegou1.home.person.reward;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseFragmentActivity;
import com.iask.yiyuanlegou1.widget.PagerSlidingTabStrip;
import com.iask.yiyuanlegou1.widget.TitleBarView;

public class RewardActivity extends BaseFragmentActivity {
    private TitleBarView title;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private OrderPagerAdapter adapter;

    @Override
    protected void findViewById() {
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_buy_record;
    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setTitleText(R.string.reward);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RewardActivity.this.finish();
            }
        });
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new OrderPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);
    }

    public class OrderPagerAdapter extends FragmentPagerAdapter {
        private final String[] TITLES = {"获得商品", "未中商品"};

        public OrderPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = new RewardFragment();
            } else {
                fragment = new UnRewardFragment();
            }
            return fragment;
        }
    }
}
