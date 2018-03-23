package com.iask.yiyuanlegou1.home.person;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.account.LoginActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.base.BaseFragment;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.home.main.product.pay.RechargeActivity;
import com.iask.yiyuanlegou1.home.person.record.BuyRecordActivity;
import com.iask.yiyuanlegou1.home.person.reward.RewardActivity;
import com.iask.yiyuanlegou1.home.person.setting.BalanceDetailActivity;
import com.iask.yiyuanlegou1.home.person.setting.SettingActivity;
import com.iask.yiyuanlegou1.home.person.share.MyShareActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.account.RefreshUserInfoResponse;
import com.iask.yiyuanlegou1.network.respose.account.UserInfo;
import com.iask.yiyuanlegou1.network.respose.product.CacheShoppingCarData;
import com.iask.yiyuanlegou1.network.service.account.RefreshUserInfoService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class PersonFragment extends BaseFragment implements View.OnClickListener {
    private TitleBarView title;
    private Button btnLogin;
    private LinearLayout buyRecordLayout, nologinLayout, loginedLayout, settingLayout,aboutUsLayout,
            myShareLayout, balanceDetailLayout, myRewardLayout;
    public static final String ACCOUNT_INFO_CHANGED = "account_info_changed";
    private LocalBroadcastManager localBroadcastManager;
    private PtrClassicFrameLayout mPtrFrame;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_person;
    }

    @Override
    protected void initView() {
        btnLogin = (Button) rootView.findViewById(R.id.toLR);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        buyRecordLayout = (LinearLayout) rootView.findViewById(R.id.my_buy_btn);
        nologinLayout = (LinearLayout) rootView.findViewById(R.id.nologin_view);
        loginedLayout = (LinearLayout) rootView.findViewById(R.id.logined_view);
        settingLayout = (LinearLayout) rootView.findViewById(R.id.setting_btn);
        myRewardLayout = (LinearLayout) rootView.findViewById(R.id.my_prize_btn);
        balanceDetailLayout = (LinearLayout) rootView.findViewById(R.id.my_balance_detail_btn);
        myShareLayout = (LinearLayout) rootView.findViewById(R.id.my_share_btn);
        rootView.findViewById(R.id.about_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        rootView.findViewById(R.id.user_recharge_btn).setOnClickListener(this);
        buyRecordLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
        balanceDetailLayout.setOnClickListener(this);
        myShareLayout.setOnClickListener(this);
        myRewardLayout.setOnClickListener(this);
        initPtrFrame();
    }

    @Override
    public void onResume() {
        refreshUserInfo();
        Object list = BaseApplication.getInstance().getShoppingCarData();
        if (list != null && ((CacheShoppingCarData) list).shoppingCartBeans != null && (
                (CacheShoppingCarData) list).shoppingCartBeans.size() > 0) {
            CacheShoppingCarData cacheShoppingCarData = (CacheShoppingCarData) list;
            int size = cacheShoppingCarData.shoppingCartBeans.size();
        }
        super.onResume();
    }

    private void initPtrFrame() {
        mPtrFrame = (PtrClassicFrameLayout) rootView.findViewById(R.id.refresh_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                // mPtrFrame.autoRefresh();
            }
        }, 100);
    }

    private void updateData() {
        refreshUserInfo();
    }

    private boolean isLogined() {
        String userId = BaseApplication.getInstance().getUserId();
        if (TextUtils.isEmpty(userId)) {
            return false;
        }
        return true;
    }

    private void refreshUserInfo() {
        if (!isLogined()) {
            mPtrFrame.refreshComplete();
            return;
        }
        RefreshUserInfoService service = new RefreshUserInfoService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<RefreshUserInfoResponse>() {
            @Override
            public void onGetData(RefreshUserInfoResponse data) {
                mPtrFrame.refreshComplete();
                try {
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO,
                            data.data.userInfo);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, final String errorMsg, Throwable error) {
                mPtrFrame.refreshComplete();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        service.post("", false);
    }

    private void refreshUI() {
        initPersonData();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        String userId = BaseApplication.getInstance().getUserId();
        if (TextUtils.isEmpty(userId)) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        } else {
            switch (v.getId()) {
                case R.id.my_buy_btn:
                    intent.setClass(getActivity(), BuyRecordActivity.class);
                    startActivity(intent);
                    break;
                case R.id.setting_btn:
                    intent.setClass(getActivity(), SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.my_balance_detail_btn:
                    intent.setClass(getActivity(), BalanceDetailActivity.class);
                    startActivity(intent);
                    break;
                case R.id.my_share_btn:
                    intent.setClass(getActivity(), MyShareActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_recharge_btn:
                    intent.setClass(getActivity(), RechargeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.my_prize_btn:
                    intent.setClass(getActivity(), RewardActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(loginSuccReceiver, new IntentFilter
                (ACCOUNT_INFO_CHANGED));
    }

    private BroadcastReceiver loginSuccReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initPersonData();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loginSuccReceiver != null) {
            localBroadcastManager.unregisterReceiver(loginSuccReceiver);
        }
    }

    private void initPersonData() {
        UserInfo info = (UserInfo) CacheManager.getObject(CacheManager.TYPE_USER_INFO,
                CacheKeys.USERINFO_LOGINVO, UserInfo.class);
        if (info == null) {
            nologinLayout.setVisibility(View.VISIBLE);
            loginedLayout.setVisibility(View.GONE);
        } else {
            nologinLayout.setVisibility(View.GONE);
            loginedLayout.setVisibility(View.VISIBLE);
            String headUrl = info.getImg();
            ImageView headImage = (ImageView) rootView.findViewById(R.id.user_head_icon);
            TextView tvUserName = (TextView) rootView.findViewById(R.id.nickName);
            TextView tvUserWealth = (TextView) rootView.findViewById(R.id.user_wealth);
            Glide.with(getActivity()).load(headUrl).into(headImage);
            if (!TextUtils.isEmpty(info.getUserName())) {
                tvUserName.setText(info.getUserName());
            } else {
                tvUserName.setText(info.getMobile());
            }
            if (info.getMoney() != null) {
                tvUserWealth.setText(info.getMoney().toString());
            } else {
                tvUserWealth.setText("0");
            }
        }
    }
}
