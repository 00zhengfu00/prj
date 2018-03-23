package com.lswuyou.tv.pm.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.UnLoginActivity;
import com.lswuyou.tv.pm.channel.login.LoginManager;
import com.lswuyou.tv.pm.channel.pay.TvChannelType;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.account.GetLoginCfgResponse;
import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;
import com.lswuyou.tv.pm.net.service.GetLoginCfgService;
import com.lswuyou.tv.pm.utils.Utils;
import com.lswuyou.tv.pm.view.PayDialog;

import java.util.Timer;

public class CourseIntroFragment extends BaseFragment {
    private CourseInfo courseInfo;
    private TextView tvContent, tvPrice, tvOriginalPrice;
    private RelativeLayout btnBuy;
    private ImageView ivCoursePoster;
    private Timer timer;
    private String orderId;
    private PayDialog dialog;
    private TextView tvBuy;
    private Drawable drawableLeftSelect, drawableLeftUnSelect;
    private LocalBroadcastManager broadcastManager;
    public static final String BOUGHT_SUCCESS = "bought_success";

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_course_intro;
    }

    @Override
    protected void initView() {
        tvContent = (TextView) rootView.findViewById(R.id.tv_content);
        tvPrice = (TextView) rootView.findViewById(R.id.tv_price);
        tvOriginalPrice = (TextView) rootView.findViewById(R.id.tv_original_price);
        ivCoursePoster = (ImageView) rootView.findViewById(R.id.iv_course);
        btnBuy = (RelativeLayout) rootView.findViewById(R.id.btn_buy);
        tvBuy = (TextView) rootView.findViewById(R.id.tv_buy);
        Bundle bundleData = getArguments();
        courseInfo = (CourseInfo) bundleData.getSerializable("courseInfo" +
                "");
        if (courseInfo.alreadyBuy == 1) {
            tvPrice.setVisibility(View.GONE);
            tvOriginalPrice.setVisibility(View.GONE);
            btnBuy.setVisibility(View.GONE);
            rootView.findViewById(R.id.tv_price_tag).setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(courseInfo.originalPriceYuan) && !courseInfo.priceYuan.equals
                    (courseInfo.originalPriceYuan)) {
                tvOriginalPrice.setText("原  价：￥" + courseInfo.originalPriceYuan);
                tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint
                        .ANTI_ALIAS_FLAG);
            }
            tvPrice.setText("￥" + courseInfo.priceYuan);
        }
        tvContent.setText(courseInfo.textIntro);
        Glide.with(this).load(courseInfo.posterUrl).placeholder(R.mipmap.loading).error(R
                .mipmap.loading).into(ivCoursePoster);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isUserLogined()) {
                } else {
                    doLogin();
                }
            }
        });
        btnBuy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvBuy.setCompoundDrawables(drawableLeftSelect, null, null, null);
                    tvBuy.setTextColor(getResources().getColor(R.color.white));
                } else {
                    tvBuy.setCompoundDrawables(drawableLeftUnSelect, null, null, null);
                    tvBuy.setTextColor(getResources().getColor(R.color.text_unselect_color));
                }
            }
        });
        drawableLeftSelect = getResources().getDrawable(R.mipmap.key_select);
        drawableLeftSelect.setBounds(0, 0, drawableLeftSelect.getMinimumWidth(),
                drawableLeftSelect
                        .getMinimumHeight());
        drawableLeftUnSelect = getResources().getDrawable(R.mipmap.key_unselect);
        drawableLeftUnSelect.setBounds(0, 0, drawableLeftUnSelect.getMinimumWidth(),
                drawableLeftUnSelect
                        .getMinimumHeight());
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastManager.registerReceiver(boughtSuccReceiver, new IntentFilter(BOUGHT_SUCCESS));
    }

    private void doLogin() {
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
                    Toast.makeText(getActivity(), "获取登录配置异常！", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(getActivity(), "获取登录配置信息失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        service.post("", true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (boughtSuccReceiver != null) {
            broadcastManager.unregisterReceiver(boughtSuccReceiver);
        }
    }

    public void setCourseInfo(CourseInfo info) {
        courseInfo = info;
    }

    private void refreshUI() {
        if (dialog != null) {
            dialog.dismiss();
        }
        rootView.findViewById(R.id.tv_price_tag).setVisibility(View.GONE);
        btnBuy.setVisibility(View.GONE);
        tvPrice.setVisibility(View.GONE);
        tvOriginalPrice.setVisibility(View.GONE);
    }

    private BroadcastReceiver boughtSuccReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            rootView.findViewById(R.id.tv_price_tag).setVisibility(View.GONE);
            btnBuy.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            tvOriginalPrice.setVisibility(View.GONE);
        }
    };
}
