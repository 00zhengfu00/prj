package com.lswuyou.tv.pm.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.AboutUsActivity;
import com.lswuyou.tv.pm.activity.BuyRecordActivity;
import com.lswuyou.tv.pm.activity.MemberActivity;
import com.lswuyou.tv.pm.activity.MyCollectActivity;
import com.lswuyou.tv.pm.activity.PlayRecordActivity;
import com.lswuyou.tv.pm.activity.SettingsActivity;
import com.lswuyou.tv.pm.activity.UnLoginActivity;
import com.lswuyou.tv.pm.cache.CachedPlayRecord;
import com.lswuyou.tv.pm.channel.login.LoginManager;
import com.lswuyou.tv.pm.channel.pay.TvChannelType;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.account.GetLoginCfgResponse;
import com.lswuyou.tv.pm.net.response.account.GetUserAccountResponse;
import com.lswuyou.tv.pm.net.service.GetLoginCfgService;
import com.lswuyou.tv.pm.net.service.GetUserAccountService;
import com.lswuyou.tv.pm.utils.Utils;

/**
 * Created by Administrator on 2016/7/21.
 */
public class UserFragment extends BaseFragment implements View.OnClickListener, View
        .OnFocusChangeListener {
    private RelativeLayout playRecordLay, collectLay, myCourseLay, buyRecordLay, setLay, aboutUsLay;
    private TextView tvPlayNum, tvCollectNum, tvCourseNum, tvBuyNum;
    protected View focusView;
    private GetUserAccountResponse.DataBean userAccountInfo;
    private LocalBroadcastManager localBroadcastManager;
    public static final String USERINFO_UPDATE = "userinfo_update";

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_user;
    }

    @Override
    protected void initView() {
        (playRecordLay = (RelativeLayout) rootView.findViewById(R.id.play_record_lay)).setOnClickListener(this);
        (collectLay = (RelativeLayout) rootView.findViewById(R.id.collect_lay)).setOnClickListener(this);
        (myCourseLay = (RelativeLayout) rootView.findViewById(R.id.my_member_lay)).setOnClickListener(this);
        (buyRecordLay = (RelativeLayout) rootView.findViewById(R.id.buy_record_lay)).setOnClickListener(this);
        (setLay = (RelativeLayout) rootView.findViewById(R.id.set_lay)).setOnClickListener(this);
        (aboutUsLay = (RelativeLayout) rootView.findViewById(R.id.about_us_lay)).setOnClickListener(this);

        tvPlayNum = (TextView) rootView.findViewById(R.id.tv_play_num);
        tvCollectNum = (TextView) rootView.findViewById(R.id.tv_collect_num);
        tvCourseNum = (TextView) rootView.findViewById(R.id.tv_course_num);
        tvBuyNum = (TextView) rootView.findViewById(R.id.tv_buy_num);

        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(userInfoUpdateReceiver, new IntentFilter(USERINFO_UPDATE));

        playRecordLay.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//                    AnimationUtil.leftRightShake(playRecordLay);
                    return true;
                }
                return false;
            }
        });
        collectLay.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        return true;
                    }
                }
                return false;
            }
        });
        buyRecordLay.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//                        AnimationUtil.leftRightShake(buyRecordLay);
                        return true;
                    }
                }
                return false;
            }
        });
        aboutUsLay.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//                        AnimationUtil.leftRightShake(aboutUsLay);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void getRecords() {
        Object objPlayRecord = CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys.PLAY_RECORD, CachedPlayRecord.class);
        int recordsNum = 0;
        if (objPlayRecord != null) {
            CachedPlayRecord records = (CachedPlayRecord) objPlayRecord;
            if (records.videoDetaiInfoList != null) {
                recordsNum += records.videoDetaiInfoList.size();
            }
        }
        tvPlayNum.setText(recordsNum + "");
    }

    @Override
    public void onClick(View v) {
        dispatch(v.getId());
    }

    @Override
    public void onResume() {
        if (focusView != null) {
            focusView.requestFocus();
        }
        getRecords();
        getData();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (userInfoUpdateReceiver != null) {
            localBroadcastManager.unregisterReceiver(userInfoUpdateReceiver);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            focusView = v;
        }
    }

    private void dispatch(int id) {
        if (Utils.isUserLogined()) {
            switch (id) {
                case R.id.play_record_lay:
                    startActivity(new Intent(getActivity(), PlayRecordActivity.class));
                    break;
                case R.id.collect_lay:
                    Intent intentCollect = new Intent(getActivity(), MyCollectActivity.class);
                    startActivity(intentCollect);
                    break;
                case R.id.my_member_lay:
                    Intent myCourseIntent = new Intent(getActivity(), MemberActivity.class);
                    startActivity(myCourseIntent);
                    break;
                case R.id.buy_record_lay:
                    Intent intentBuyRecord = new Intent(getActivity(), BuyRecordActivity.class);
                    if (userAccountInfo != null && userAccountInfo.historyOrders != null) {
                        intentBuyRecord.putExtra("buyRecords", userAccountInfo);
                    }
                    startActivity(intentBuyRecord);
                    break;
                case R.id.set_lay:
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    break;
                case R.id.about_us_lay:
                    startActivity(new Intent(getActivity(), AboutUsActivity.class));
                    break;
            }
        } else {
            switch (id) {
                case R.id.play_record_lay:
                    startActivity(new Intent(getActivity(), PlayRecordActivity.class));
                    break;
                case R.id.collect_lay:
                    showDialog();
                    break;
                case R.id.my_member_lay:
                    showDialog();
                    break;
                case R.id.buy_record_lay:
                    showDialog();
                    break;
                case R.id.set_lay:
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    break;
                case R.id.about_us_lay:
                    startActivity(new Intent(getActivity(), AboutUsActivity.class));
                    break;
            }

        }
    }

    private void showDialog() {
        GetLoginCfgService service = new GetLoginCfgService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<GetLoginCfgResponse>() {
            @Override
            public void onGetData(GetLoginCfgResponse data) {
                try {
                    String loginType = data.data.loginCfgVo.loginType;
                    if (loginType.equals(TvChannelType.none.name())) {
                        Intent intent = new Intent(getActivity(), UnLoginActivity.class);
                        startActivity(intent);
                    } else {
                        LoginManager.login(getActivity(), loginType);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "获取登录配置异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(getActivity(), "获取登录配置信息失败！", Toast.LENGTH_SHORT).show();
            }
        });
        service.post("", true);
    }

    private void getData() {
        if (!Utils.isUserLogined()) {
            return;
        }
        GetUserAccountService service = new GetUserAccountService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<GetUserAccountResponse>() {
            @Override
            public void onGetData(GetUserAccountResponse data) {
                try {
                    userAccountInfo = data.data;
                    BaseApplication.setLoginUserInfo(data.data.userInfo);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI();
                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "获取账户信息异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(getActivity(), "获取账户信息失败！", Toast.LENGTH_SHORT).show();
            }
        });
        service.postAES("", false);
    }

    private void refreshUI() {
        int ordersNum = 0;
        if (userAccountInfo.historyOrders != null) {
            ordersNum = userAccountInfo.historyOrders.size();
        }
        tvBuyNum.setText("" + ordersNum);
        int collectNum = userAccountInfo.favCount;
        tvCollectNum.setText("" + collectNum);
        int myCourseNum = userAccountInfo.memberCount;
        tvCourseNum.setText("" + myCourseNum);
    }

    private BroadcastReceiver userInfoUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Utils.isUserLogined()) {
                getData();
            } else {
                resetUiData();
            }
        }
    };
    private BroadcastReceiver userInfoResetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            resetUiData();
        }
    };

    /**
     * 设置初始化焦点
     * 0-第一个item
     * 1-最后一个item
     *
     * @param index
     */
    public void setInitFocus(int index) {
        if (index == 0) {
            playRecordLay.requestFocus();
        } else if (index == 1) {
            buyRecordLay.requestFocus();
        }
    }

    /**
     * 重置用户数据界面
     */
    private void resetUiData() {
        tvCollectNum.setText("");
        tvCourseNum.setText("");
        tvBuyNum.setText("");
        tvPlayNum.setText("");
    }
}
