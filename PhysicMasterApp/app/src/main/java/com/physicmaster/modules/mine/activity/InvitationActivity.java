package com.physicmaster.modules.mine.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.mine.fragment.dialogFragment.InvitationDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.GetSharedDataResponse;
import com.physicmaster.net.response.account.SharedData;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.GetSharedDataService;
import com.physicmaster.net.service.account.SharedSuccService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class InvitationActivity extends BaseActivity implements View.OnClickListener {

    private SharedData sharedData;
    //微信分享成功广播接收
    public static final String WEIXIN_SHARED_SUCC = "weixin_shared_success";
    private LocalBroadcastManager                     localBroadcastManager;
    private TextView                                  tvInviteCode;
    private Button                                    btnFriendInvite;
    private UserDataResponse.UserDataBean.LoginVoBean mDataBean;
    private ShareAction                               shareAction;

    @Override
    protected void findViewById() {

        tvInviteCode = (TextView) findViewById(R.id.tv_invite_code);

        findViewById(R.id.iv_qq).setOnClickListener(this);
        findViewById(R.id.iv_weixin).setOnClickListener(this);
        findViewById(R.id.iv_kongjian).setOnClickListener(this);
        findViewById(R.id.iv_weibo).setOnClickListener(this);
        btnFriendInvite = (Button) findViewById(R.id.btn_friend_invite);

        findViewById(R.id.btn_copy_invite).setOnClickListener(this);
        btnFriendInvite.setOnClickListener(this);

        initTitle();

    }

    //年级
    public void setbtnText(String grade) {
        btnFriendInvite.setText(grade);
        btnFriendInvite.setBackgroundResource(R.drawable.gray_background);
        btnFriendInvite.setEnabled(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mDataBean = BaseApplication.getUserData();
        if (mDataBean == null) {
            gotoLoginActivity();
            return;
        }
        tvInviteCode.setText(mDataBean.inviteCode);
        if (null == mDataBean.friendInviteCode) {
            btnFriendInvite.setText("使用好友的邀请码");
            btnFriendInvite.setEnabled(true);
            btnFriendInvite.setBackgroundResource(R.drawable.blue_btn_background);
        } else {
            btnFriendInvite.setText("已使用过好友邀请码");
            btnFriendInvite.setEnabled(false);
            btnFriendInvite.setBackgroundResource(R.drawable.gray_background);
        }
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示 ，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("邀请好友");

    }

    @Override
    protected void initView() {

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(weixinSharedReceiver, new IntentFilter
                (WEIXIN_SHARED_SUCC));
        getSharedData();
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
    protected int getContentLayout() {
        return R.layout.activity_invitation;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_copy_invite:
                String inviteCode = tvInviteCode.getText().toString().trim();
                android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(inviteCode);
                UIUtils.showToast(InvitationActivity.this, "已复制到剪切版");
                break;
            case R.id.btn_friend_invite:
                new InvitationDialogFragment()
                        .show(InvitationActivity.this.getSupportFragmentManager(),
                                "invitation_dialog");
                break;
            case R.id.iv_qq:
                if (Utils.checkApkExist(InvitationActivity.this, "com.tencent.mobileqq")) {
                    sharedToMedia(SHARE_MEDIA.QQ);
                } else {
                    Toast.makeText(InvitationActivity.this, "您还未安装QQ，请先安装~", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.iv_weixin:
                if (Utils.checkApkExist(InvitationActivity.this, "com.tencent.mm")) {
                    sharedToMedia(SHARE_MEDIA.WEIXIN);
                } else {
                    Toast.makeText(InvitationActivity.this, "您还未安装微信，请先安装~", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.iv_kongjian:
                if (Utils.checkApkExist(InvitationActivity.this, "com.tencent.mobileqq")) {
                    sharedToMedia(SHARE_MEDIA.QZONE);
                } else {
                    Toast.makeText(InvitationActivity.this, "您还未安装QQ，请先安装~", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.iv_weibo:
                if (Utils.checkApkExist(InvitationActivity.this, "com.sina.weibo") || Utils
                        .checkApkExist(this, "com" +
                                ".sina.weibog3")) {
                    sharedToMedia(SHARE_MEDIA.SINA);
                } else {
                    Toast.makeText(InvitationActivity.this, "您还未安装微博，请先安装~", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    /**
     * 获取分享数据
     */
    private void getSharedData() {
        final GetSharedDataService service = new GetSharedDataService(this);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetSharedDataResponse>() {
            @Override
            public void onGetData(GetSharedDataResponse data) {
                sharedData = data.data;
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(InvitationActivity.this, "获取分享数据失败");
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("", false);
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
        String desc = sharedData.shareDesc;
        String shareImageUrl = sharedData.shareImg;
        String shareTitle = sharedData.shareTitle;
        String shareUrl = sharedData.shareUrl;
        UMImage image = null;
        String packageName = getPackageName();
        if (!TextUtils.isEmpty(shareImageUrl)) {
            image = new UMImage(InvitationActivity.this, shareImageUrl);
        } else {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                image = new UMImage(InvitationActivity.this, R.mipmap.icon_physic);
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                image = new UMImage(InvitationActivity.this, R.mipmap.icon_chemistory);
            } else if (packageName.equals(Constant.MATHMASTER)) {
                image = new UMImage(InvitationActivity.this, R.mipmap.icon_math);
            }
        }
        if (TextUtils.isEmpty(desc)) {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                desc = "物理大师，科技、教育、艺术跨界融合，教师必备课堂教学大片。";
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                desc = "化学大师，科技、教育、艺术跨界融合，教师必备课堂教学大片。";
            } else if (packageName.equals(Constant.MATHMASTER)) {
                desc = "数学大师，科技、教育、艺术跨界融合，教师必备课堂教学大片。";
            }
        }
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(shareTitle);
        web.setThumb(image);
        web.setDescription(desc);
        shareAction = new ShareAction(InvitationActivity.this);
        shareAction.setPlatform(media).setCallback(new UMShareListener() {

            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA platform) {
                Log.d("plat", "platform" + platform);
                UIUtils.showToast(InvitationActivity.this, "分享成功啦");
                if (platform == SHARE_MEDIA.QQ) {
                    sharedSuccess(0);
                } else if (platform == SHARE_MEDIA.QZONE) {
                    sharedSuccess(2);
                } else if (platform == SHARE_MEDIA.SINA) {
                    sharedSuccess(3);
                }
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                UIUtils.showToast(InvitationActivity.this, "分享失败啦");
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                UIUtils.showToast(InvitationActivity.this, "分享取消了");
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
}
