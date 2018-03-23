package com.physicmaster.modules.ranking.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.ranking.GetRankingResponse;
import com.physicmaster.net.response.ranking.GetRankingResponse.DataBean.RankBean;
import com.physicmaster.net.service.ranking.GetRankingService;
import com.physicmaster.utils.UIUtils;

/**
 * Created by songrui on 17/6/29.
 */

public class RankingFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvFriendRanking;
    private TextView tvAllRanking;

    private FragmentActivity mContext;

    private RankBean friendTotal;
    private RankBean friendWeek;
    private RankBean total;
    private RankBean week;
    private FriendRankingFragment friendRankingFragment;
    private TotalRankingFragment totalRankingFragment;
    private ImageView ivRefresh;
    private Animation operatingAnim;
    private long startTime;
    private long leftTime;
    private int selectIndex = 1;

    private boolean refresh = false;

    private LocalBroadcastManager localBroadcastManager;
    public static final String SWITCH_ACCOUNT = "switch_account";
    private BroadcastReceiver switchAccountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh = true;
        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_ranking;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refresh) {
            getRankingData();
            refresh = !refresh;
        }
    }

    @Override
    protected void initView() {

        mContext = getActivity();

        tvFriendRanking = (TextView) rootView.findViewById(R.id.tv_friend_ranking);
        tvAllRanking = (TextView) rootView.findViewById(R.id.tv_all_ranking);
        ivRefresh = (ImageView) rootView.findViewById(R.id.iv_refresh);


        tvFriendRanking.setSelected(true);
        tvFriendRanking.setOnClickListener(this);
        tvAllRanking.setOnClickListener(this);
        ivRefresh.setOnClickListener(this);
        operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.xuanzhuan);

        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(switchAccountReceiver, new IntentFilter(SWITCH_ACCOUNT));

        getRankingData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_friend_ranking:
                setTabSelection(0);
                break;
            case R.id.tv_all_ranking:
                setTabSelection(1);
                break;
            case R.id.iv_refresh:
                getRankingData();
                break;
        }
    }

    public void setTabSelection(int index) {
        selectIndex = index;
        // 底部切换
        clearSelection(index);
        // 开启一个Fragment事务
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
//                if (friendRankingFragment == null) {
                friendRankingFragment = new FriendRankingFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("friendTotal", friendTotal);
                bundle.putParcelable("friendWeek", friendWeek);
                friendRankingFragment.setArguments(bundle);
                transaction.add(R.id.fm, friendRankingFragment);
//                } else {
//                    transaction.show(friendRankingFragment);
//                }
                break;

            case 1:
//                if (totalRankingFragment == null) {
                totalRankingFragment = new TotalRankingFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("week", week);
                bundle2.putParcelable("total", total);
                totalRankingFragment.setArguments(bundle2);
                transaction.add(R.id.fm, totalRankingFragment);
//                } else {
//                    transaction.show(totalRankingFragment);
//                }
                break;
        }
        transaction.commit();
        if (operatingAnim != null) {
            long endTime = System.currentTimeMillis();
            long time = endTime - startTime;
            if (time < 1000) {
                leftTime = 1000 - time;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivRefresh.clearAnimation();
                    }
                }, leftTime);
            } else {
                ivRefresh.clearAnimation();
            }
        }
    }

    private void clearSelection(int index) {

        if (index == 0) {
            tvFriendRanking.setSelected(true);
            tvAllRanking.setSelected(false);
        }

        if (index == 1) {
            tvAllRanking.setSelected(true);
            tvFriendRanking.setSelected(false);
        }

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (friendRankingFragment != null) {
            transaction.hide(friendRankingFragment);
        }
        if (totalRankingFragment != null) {
            transaction.hide(totalRankingFragment);
        }
    }

    private void getRankingData() {
        final GetRankingService service = new GetRankingService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<GetRankingResponse>() {

            @Override
            public void onGetData(GetRankingResponse data) {
                friendTotal = data.data.friendTotal;
                friendWeek = data.data.friendWeek;
                total = data.data.total;
                week = data.data.week;
                //tvLevel.setText(data.data.nextLevelPoint + "");
                if (isVisible()) {
                    setTabSelection(selectIndex);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
            }
        });
        service.postLogined("", false);
        ivRefresh.setAnimation(operatingAnim);
        ivRefresh.startAnimation(operatingAnim);
        startTime = System.currentTimeMillis();
    }
}

