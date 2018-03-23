package com.physicmaster.modules.mine.activity.setting;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.SplashActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.account.LoginWebActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.BindService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.List;
import java.util.Map;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;


public class BindingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout        rlBindPhone;
    private TextView              tvBindPhone;
    private RelativeLayout        rlBindWeixin;
    private TextView              tvWeinxinName;
    private TextView              tvBindWeixin;
    private RelativeLayout        rlBindQq;
    private TextView              tvQqName;
    private TextView              tvBindQq;
    private RelativeLayout        rlBindWeibo;
    private TextView              tvBindWeibo;
    private TextView              tvWeiboName;
    private TextView              tvPhoneName;
    private ProgressDialog dialog;

    @Override
    protected void findViewById() {

        rlBindPhone = (RelativeLayout) findViewById(R.id.rl_bind_phone);
        rlBindWeixin = (RelativeLayout) findViewById(R.id.rl_bind_weixin);
        rlBindQq = (RelativeLayout) findViewById(R.id.rl_bind_qq);
        rlBindWeibo = (RelativeLayout) findViewById(R.id.rl_bind_weibo);

        tvBindPhone = (TextView) findViewById(R.id.tv_bind_phone);
        tvBindWeixin = (TextView) findViewById(R.id.tv_bind_weixin);
        tvBindQq = (TextView) findViewById(R.id.tv_bind_qq);
        tvBindWeibo = (TextView) findViewById(R.id.tv_bind_weibo);

        tvWeinxinName = (TextView) findViewById(R.id.tv_weinxin_name);
        tvPhoneName = (TextView) findViewById(R.id.tv_phone_name);
        tvQqName = (TextView) findViewById(R.id.tv_qq_name);
        tvWeiboName = (TextView) findViewById(R.id.tv_weibo_name);

        rlBindPhone.setOnClickListener(this);
        rlBindWeixin.setOnClickListener(this);
        rlBindQq.setOnClickListener(this);
        rlBindWeibo.setOnClickListener(this);

        initTitle();
    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("账号绑定");
    }

    @Override
    protected void initView() {
    }


    /**
     * 微信绑定
     */
    private void bindWeixin() {
        if (Utils.checkApkExist(this, "com.tencent.mm")) {
            UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);
        } else {
            Toast.makeText(this, "您还未安装微信，请先安装~", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * QQ绑定
     */
    private void bindQQ() {
        if (Utils.checkApkExist(this, "com.tencent.mobileqq")) {
            UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.QQ, authListener);
        } else {
            Toast.makeText(this, "您还未安装QQ，请先安装~", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * QQweb绑定
     */
    private void bind2QQNOWL() {
        StartupResponse.DataBean dataBean = BaseApplication.getStartupDataBean();
        if (null != dataBean) {
            String webQqGetCodeUrl = dataBean.webQqGetCodeUrl;
            if (!TextUtils.isEmpty(webQqGetCodeUrl)) {
                Intent intent = new Intent(BindingActivity.this, LoginWebActivity.class);
                intent.putExtra("webQqGetCodeUrl", webQqGetCodeUrl);
                intent.putExtra("isbind",true);
                startActivity(intent);
            }
        } else {
            startActivity(new Intent(BindingActivity.this, SplashActivity.class));
            UIUtils.showToast(BindingActivity.this, "数据异常");
            finish();
        }
    }

    /**
     * 微博绑定
     */
    private void bindWeibo() {
        if (Utils.checkApkExist(this, "com.sina.weibo") || Utils.checkApkExist(this, "com.sina" +
                ".weibog3")) {
            UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.SINA, authListener);
        } else {
            Toast.makeText(this, "您还未安装微博，请先安装~", Toast.LENGTH_SHORT).show();
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            dialog = new ProgressDialog(BindingActivity.this);
            dialog.setTitle("正在绑定");
            dialog.setMessage("请等候……");
            dialog.setCancelable(true);
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String openId;
            String token = data.get("access_token");
            if (platform.equals(SHARE_MEDIA.SINA)) {
                openId = data.get("uid");
            } else {
                openId = data.get("openid");
            }
            int type = -1;
            if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                type = 0;
            } else if (platform.equals(SHARE_MEDIA.QQ)) {
                type = 1;
            } else if (platform.equals(SHARE_MEDIA.SINA)) {
                type = 2;
            }
            openLogin(openId, type, token);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        UserDataResponse.UserDataBean.LoginVoBean dataBean = BaseApplication.getUserData();
        if (dataBean == null) {
            gotoLoginActivity();
            return;
        }
        showBind(dataBean);
    }

    public class BindChageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String openId = intent.getStringExtra("openid");
            String token = intent.getStringExtra("token");
            int type = intent.getIntExtra("type", 0);
            openLogin(openId, type, token);
        }
    }

    private void showBind(UserDataResponse.UserDataBean.LoginVoBean databean) {
        List<UserDataResponse.UserDataBean.LoginVoBean.UserBindsBean> userBinds = databean
                .userBinds;
        if (null == userBinds || userBinds.size() == 0) {
            tvBindPhone.setTextColor(getResources().getColor(R.color.colorTitleBlue));
            tvBindPhone.setText("绑定");
            tvBindWeixin.setTextColor(getResources().getColor(R.color.colorTitleBlue));
            tvBindWeixin.setText("绑定");
            tvBindQq.setTextColor(getResources().getColor(R.color.colorTitleBlue));
            tvBindQq.setText("绑定");
            tvBindWeibo.setTextColor(getResources().getColor(R.color.colorTitleBlue));
            tvBindWeibo.setText("绑定");

            tvWeinxinName.setText("");
            tvQqName.setText("");
            tvWeiboName.setText("");
            tvPhoneName.setText("");

            rlBindWeixin.setEnabled(true);
            rlBindQq.setEnabled(true);
            rlBindPhone.setEnabled(true);
            rlBindWeibo.setEnabled(true);

        } else {
            for (int i = 0; i < userBinds.size(); i++) {
                if (userBinds.get(i).bindType == 0) {
                    tvBindWeixin.setTextColor(getResources().getColor(R.color.colorBindGray));
                    tvBindWeixin.setText("已绑定");
                    rlBindWeixin.setEnabled(false);
                    if (TextUtils.isEmpty(userBinds.get(i).tpNickname)) {
                        tvWeinxinName.setText("");
                    } else {
                        tvWeinxinName.setText("(" + userBinds.get(i).tpNickname + ")");
                    }
                } else if (userBinds.get(i).bindType == 1) {
                    tvBindQq.setTextColor(getResources().getColor(R.color.colorBindGray));
                    tvBindQq.setText("已绑定");
                    rlBindQq.setEnabled(false);
                    if (TextUtils.isEmpty(userBinds.get(i).tpNickname)) {
                        tvQqName.setText("");
                    } else {
                        tvQqName.setText("(" + userBinds.get(i).tpNickname + ")");
                    }
                } else if (userBinds.get(i).bindType == 2) {
                    tvBindWeibo.setTextColor(getResources().getColor(R.color.colorBindGray));
                    tvBindWeibo.setText("已绑定");
                    rlBindWeibo.setEnabled(false);
                    if (TextUtils.isEmpty(userBinds.get(i).tpNickname)) {
                        tvWeiboName.setText("");
                    } else {
                        tvWeiboName.setText("(" + userBinds.get(i).tpNickname + ")");
                    }
                } else if (userBinds.get(i).bindType == 4) {
                    tvBindPhone.setTextColor(getResources().getColor(R.color.colorBindGray));
                    tvBindPhone.setText("已绑定");
                    rlBindPhone.setEnabled(false);
                    if (TextUtils.isEmpty(userBinds.get(i).tpNickname)) {
                        tvPhoneName.setText("");
                    } else {
                        tvPhoneName.setText("(" + userBinds.get(i).tpNickname + ")");
                    }
                }

            }

        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_binding;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_bind_phone:
                startActivity(new Intent(BindingActivity.this, BindPhoneActivity.class));
                break;
            case R.id.rl_bind_weixin:
                bindWeixin();
                break;
            case R.id.rl_bind_qq:
                String packageName = mContext.getPackageName();
                if (Constant.PHYSICMASTER.equals(packageName)) {
                    bindQQ();
                } else if (Constant.CHYMISTMASTER.equals(packageName)) {
                    bind2QQNOWL();
                } else if (Constant.MATHMASTER.equals(packageName)) {
                    bind2QQNOWL();
                }
                break;
            case R.id.rl_bind_weibo:
                bindWeibo();
                break;
        }
    }

    /**
     * 第三方绑定
     */
    private void openLogin(String openId, int type, String token) {
        final BindService service = new BindService(this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                loginAfterAction(data.data);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(BindingActivity.this, errorMsg);
            }
        });
        StringBuilder builder = new StringBuilder();
        builder.append("openid=" + openId);
        builder.append("&btype=" + type);
        builder.append("&token=" + token);
        service.postLogined(builder.toString(), false);
    }

    /**
     * 绑定成功操作
     * @param data
     */
    private void loginAfterAction(UserDataResponse.UserDataBean data) {
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, data.loginVo);
        BaseApplication.setUserData(data.loginVo);
        showBind(data.loginVo);
        UIUtils.showToast(this, "绑定成功");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }
}
