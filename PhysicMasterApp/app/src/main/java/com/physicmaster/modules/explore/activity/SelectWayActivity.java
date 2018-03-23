package com.physicmaster.modules.explore.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.WebviewActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.ParentsPayLogService;
import com.physicmaster.net.service.account.SharedSuccService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class SelectWayActivity extends BaseActivity implements OnClickListener {

    //微信分享成功广播接收
    public static final String WEIXIN_SHARED_SUCC = "weixin_shared_success";
    private LocalBroadcastManager                     localBroadcastManager;
    private ShareAction                               shareAction;
    private UserDataResponse.UserDataBean.LoginVoBean mDataBean;
    private String                                    memberHpUrl;

    @Override
    protected void findViewById() {
        Button btnQQ = (Button) findViewById(R.id.btn_qq);
        Button btnWeixin = (Button) findViewById(R.id.btn_weixin);
        Button btnDaifu = (Button) findViewById(R.id.btn_daifu);

        btnQQ.setOnClickListener(this);
        btnWeixin.setOnClickListener(this);
        btnDaifu.setOnClickListener(this);
        initTitle();

        int subjectId = getIntent().getIntExtra("subjectId", 0);
        int memberItemId = getIntent().getIntExtra("memberItemId", 0);

        mDataBean = BaseApplication.getUserData();
        if (mDataBean == null) {
            gotoLoginActivity();
            return;
        } else {
            memberHpUrl = mDataBean.memberHpUrl;
            if (subjectId != 0) {
                memberHpUrl = memberHpUrl + subjectId;
            } else {
                if (memberItemId == -1) {
                    memberHpUrl = memberHpUrl + 0;
                }
            }
        }

    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("让家人帮我购买");
    }

    @Override
    protected void initView() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(weixinSharedReceiver, new IntentFilter
                (WEIXIN_SHARED_SUCC));

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (weixinSharedReceiver != null) {
            localBroadcastManager.unregisterReceiver(weixinSharedReceiver);
        }
        if (shareAction != null) {
            shareAction.close();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_qq:
                MobclickAgent.onEvent(SelectWayActivity.this, "member_open_parents_pay_qq");
                if (Utils.checkApkExist(SelectWayActivity.this, "com.tencent.mobileqq")) {
                    sharedToMedia(SHARE_MEDIA.QQ);
                } else {
                    Toast.makeText(SelectWayActivity.this, "您还未安装QQ，请先安装~", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.btn_weixin:
                MobclickAgent.onEvent(SelectWayActivity.this, "member_open_parents_pay_weixin");
                if (Utils.checkApkExist(SelectWayActivity.this, "com.tencent.mm")) {
                    sharedToMedia(SHARE_MEDIA.WEIXIN);
                } else {
                    Toast.makeText(SelectWayActivity.this, "您还未安装微信，请先安装~", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.btn_daifu:
                MobclickAgent.onEvent(SelectWayActivity.this, "member_open_parents_pay_scan");
                Intent intent = new Intent(SelectWayActivity.this, WebviewActivity.class);
                intent.putExtra("url", memberHpUrl);
                startActivity(intent);
                break;

        }
    }

    private BroadcastReceiver weixinSharedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sharedSuccess(1);
        }
    };

    /**
     * 分享
     */
    private void sharedToMedia(SHARE_MEDIA media) {
        String desc = null;
        String shareImageUrl = null;
        String shareTitle = null;
        String shareUrl = memberHpUrl;
        UMImage image = null;
        String packageName = getPackageName();
        if (packageName.equals(Constant.PHYSICMASTER)) {
            shareTitle = "快帮我开通物理大师会员吧！";
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            shareTitle = "快帮我开通化学大师会员吧！";
        } else if (packageName.equals(Constant.MATHMASTER)) {
            shareTitle = "快帮我开通数学大师会员吧！";
        }
        if (!TextUtils.isEmpty(shareImageUrl)) {
            image = new UMImage(SelectWayActivity.this, shareImageUrl);
        } else {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                image = new UMImage(SelectWayActivity.this, R.mipmap.icon_physic);
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                image = new UMImage(SelectWayActivity.this, R.mipmap.icon_chemistory);
            } else if (packageName.equals(Constant.MATHMASTER)) {
                image = new UMImage(SelectWayActivity.this, R.mipmap.icon_math);
            }
        }
        if (TextUtils.isEmpty(desc)) {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                desc = "中国好家长都帮孩子开通了物理大师会员，5万学校老师共同推荐，细致讲解，马上提分！";
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                desc = "中国好家长都帮孩子开通了化学大师会员，5万学校老师共同推荐，细致讲解，马上提分！";
            } else if (packageName.equals(Constant.MATHMASTER)) {
                desc = "中国好家长都帮孩子开通了数学大师会员，5万学校老师共同推荐，细致讲解，马上提分！";
            }
        }
        if (TextUtils.isEmpty(shareTitle)) {
            shareTitle = "";
        }
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);
        web.setThumb(image);
        web.setDescription(desc);
        shareAction = new ShareAction(SelectWayActivity.this);
        shareAction.setPlatform(media).setCallback(new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);
                SelectWayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.showToast(SelectWayActivity.this, "分享成功啦");
                    }
                });
                if (platform == SHARE_MEDIA.QQ) {
                    sharedSuccess(0);
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                SelectWayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.showToast(SelectWayActivity.this, "分享失败啦");
                    }
                });
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                SelectWayActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.showToast(SelectWayActivity.this, "分享取消了");
                    }
                });
            }
        }).withMedia(web).share();
    }

    /**
     * 分享成功调用接口
     */
    private void sharedSuccess(int type) {
        final SharedSuccService service = new SharedSuccService(this);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        service.setCallback(new IOpenApiDataServiceCallback() {
            @Override
            public void onGetData(Object data) {
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("shareType=" + type, false);
    }

    /**
     * 分享日志接口
     */
    private void sharedSuccess(String orderNum, int type) {
        final ParentsPayLogService service = new ParentsPayLogService(this);
        service.setCallback(new IOpenApiDataServiceCallback() {
            @Override
            public void onGetData(Object data) {
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
            }
        });
        service.postLogined("orderId=" + orderNum + "&shareType=" + type, false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_way;
    }
}
