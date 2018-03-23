package com.physicmaster.modules.ranking.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.net.response.ranking.GetRankingResponse;
import com.physicmaster.utils.ScreenUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

/**
 * Created by songrui on 17/6/29.
 */

public class FriendRankingFragment extends BaseFragment2 {

    private MagicIndicator magicIndicator;
    private ViewPager mViewPager;
    private String[] mTabNames;
    private GetRankingResponse.DataBean.RankBean friendTotal;
    private GetRankingResponse.DataBean.RankBean friendWeek;
    private FragmentActivity mContext;

    @Override
    protected void initView(View view) {
        mContext = getActivity();
        magicIndicator = (MagicIndicator) view.findViewById(R.id.magic_indicator);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        Bundle bundle = getArguments();
        friendTotal = bundle.getParcelable("friendTotal");
        friendWeek = bundle.getParcelable("friendWeek");
        upDataUI();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_total_ranking;
    }

    @Override
    public void fetchData() {
    }

    public void upDataUI() {
        mTabNames = ScreenUtils.getStringArray(R.array.ranking_tab_names);
        FragmentManager childFragmentManager = getChildFragmentManager();
        mViewPager.setAdapter(new RankingAdapter(childFragmentManager));

        final CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mTabNames == null ? 0 : mTabNames.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);

                simplePagerTitleView.setText(mTabNames[index]);
                simplePagerTitleView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.size_16));
                simplePagerTitleView.setNormalColor(Color.parseColor("#80ffffff"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#ffffff"));

                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });

                return simplePagerTitleView;
            }


            @Override
            public IPagerIndicator getIndicator(Context context) {
                TriangularPagerIndicator indicator = new TriangularPagerIndicator(context);
                indicator.setLineColor(Color.parseColor("#ffffff"));
                indicator.setLineHeight(1);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
        mViewPager.setCurrentItem(1);

    }

    class RankingAdapter extends FragmentPagerAdapter {

        private SparseArray<BaseFragment2> sFragments = new SparseArray<>();

        public RankingAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            BaseFragment2 fragment = sFragments.get(position);
            if (fragment == null) {
                switch (position) {
                    case 0:
                        fragment = new WeekRankingFragment();
                        Bundle bundle1 = new Bundle();
                        bundle1.putParcelable("bean", friendWeek);
                        bundle1.putBoolean("isHide", 1 == BaseApplication.getUserData().isTourist);
                        fragment.setArguments(bundle1);
                        break;
                    case 1:
                        fragment = new WeekRankingFragment();
                        Bundle bundle2 = new Bundle();
                        bundle2.putParcelable("bean", friendTotal);
                        bundle2.putBoolean("isHide", 1 == BaseApplication.getUserData().isTourist);
                        fragment.setArguments(bundle2);
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
}
