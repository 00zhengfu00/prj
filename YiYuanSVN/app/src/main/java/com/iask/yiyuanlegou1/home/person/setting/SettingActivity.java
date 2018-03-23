package com.iask.yiyuanlegou1.home.person.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.account.RecoverPasswdActivity;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.home.person.PersonFragment;
import com.iask.yiyuanlegou1.home.person.address.AddressManageActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.account.AliyunServerBean;
import com.iask.yiyuanlegou1.network.respose.account.LoginResponse;
import com.iask.yiyuanlegou1.network.respose.account.OssTokenResponse;
import com.iask.yiyuanlegou1.network.respose.account.UserInfo;
import com.iask.yiyuanlegou1.network.service.account.ChangeHeadService;
import com.iask.yiyuanlegou1.network.service.account.OssTokenService;
import com.iask.yiyuanlegou1.utils.GetPictrueProcess;
import com.iask.yiyuanlegou1.utils.OssUtils;
import com.iask.yiyuanlegou1.utils.UIUtils;
import com.iask.yiyuanlegou1.widget.ImageUploadDialog;
import com.iask.yiyuanlegou1.widget.TitleBarView;
import com.makeramen.roundedimageview.RoundedImageView;

public class SettingActivity extends BaseActivity {
    private TitleBarView title;
    //临时AccessKeyId
    private String tempAk;
    //临时AccessKeySecret
    private String tempSK;
    //securityToken
    private String securityToken;
    /**
     * 处理图片选取、拍摄、裁剪、存储等任务
     */
    private GetPictrueProcess avatorProcess;
    public static final String INFO_CHANGED = "info_changed";
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void findViewById() {
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setTitleText(R.string.setting);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });
        findViewById(R.id.my_head_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAvator();
            }
        });
        findViewById(R.id.iv_head).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAvator();
            }
        });
        findViewById(R.id.my_address_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, AddressManageActivity.class));
            }
        });
        findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        findViewById(R.id.my_modifypw_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object obj = CacheManager.getObject(CacheManager.TYPE_USER_INFO,
                        CacheKeys.USERINFO_LOGINVO, UserInfo.class);
                if (obj == null) {
                    UIUtils.showToast(SettingActivity.this, "请先登录！");
                    return;
                }
                UserInfo info = (UserInfo) obj;
                if (TextUtils.isEmpty(info.getMobile())) {
                    UIUtils.showToast(SettingActivity.this, "请先绑定手机再修改密码！");
                    return;
                }
                startActivity(new Intent(SettingActivity.this, RecoverPasswdActivity.class));
            }
        });
        findViewById(R.id.my_mobile_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvPhone = (TextView) findViewById(R.id.tv_mobile);
                if (TextUtils.isEmpty(tvPhone.getText())) {
                    startActivity(new Intent(SettingActivity.this, BindPhoneActivity.class));
                }
            }
        });
        findViewById(R.id.my_nick_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ModifyNickActivity.class);
                String nick = ((TextView) findViewById(R.id.tv_nick)).getText().toString();
                intent.putExtra("nick", nick);
                startActivity(intent);
            }
        });
        findViewById(R.id.my_mail_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, ModifyEmailActivity.class));
            }
        });
        initPersonData();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(infoChangedReceiver, new
                IntentFilter(INFO_CHANGED));
    }

    @Override
    protected void onDestroy() {
        if (infoChangedReceiver != null) {
            localBroadcastManager.unregisterReceiver(infoChangedReceiver);
        }
        super.onDestroy();
    }

    private void logout() {
        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO);
        BaseApplication.getInstance().setUserId("");
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(PersonFragment.ACCOUNT_INFO_CHANGED));
        finish();
    }

    private BroadcastReceiver infoChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            initPersonData();
        }
    };

    private void initPersonData() {
        Object obj = CacheManager.getObject(CacheManager.TYPE_USER_INFO,
                CacheKeys.USERINFO_LOGINVO, UserInfo.class);
        if (obj == null) {
            return;
        }
        UserInfo info = (UserInfo) obj;
        RoundedImageView headView = (RoundedImageView) findViewById(R.id.iv_head);
        Glide.with(this).load(info.getImg()).into(headView);
        ((TextView) findViewById(R.id.tv_id)).setText(info.getDisplayId() + "");
        ((TextView) findViewById(R.id.tv_nick)).setText(info.getUserName());
        ((TextView) findViewById(R.id.tv_mail)).setText(info.getEmail());
        ((TextView) findViewById(R.id.tv_mobile)).setText(info.getMobile());
    }

    /**
     * 获取阿里云访问token
     */
    private void getOssToken() {
        OssTokenService service = new OssTokenService(this);
        service.setCallback(new IOpenApiDataServiceCallback<OssTokenResponse>() {
            @Override
            public void onGetData(OssTokenResponse data) {
                try {
                    tempAk = data.data.ossToken.tempAk;
                    tempSK = data.data.ossToken.tempSk;
                    securityToken = data.data.ossToken.securityToken;
                    startUpload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(SettingActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("", true);
    }

    private void startUpload() {
        String localPath = avatorProcess.getAfterCropImageUri().getPath().toString();
        Object object = CacheManager.getObject(CacheManager
                .TYPE_USER_INFO, CacheKeys.OSS_SERVER_INFO, AliyunServerBean.class);
        if (object == null) {
            Toast.makeText(SettingActivity.this, "服务暂不可使用！", Toast.LENGTH_SHORT).show();
            return;
        }
        final AliyunServerBean bean = (AliyunServerBean) object;
        OssUtils.initOss(getApplicationContext(), tempAk, tempSK, bean.getCdnName(), securityToken);
        final String avatorName = System.currentTimeMillis() + "";
        OssUtils.uploadFile(bean.getBucketName(), bean.getImgPath() + avatorName, localPath, new
                OssUtils.OnUploadListener() {

                    @Override
                    public void onUploadSucc(final String path) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettingActivity.this, "上传成功！", Toast.LENGTH_SHORT)
                                        .show();
                                notifyServer(bean.getCdnName() + bean.getImgPath() + avatorName);
                            }
                        });
                    }

                    @Override
                    public void onUploadFail(String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(SettingActivity.this, "上传失败！", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                    }

                    @Override
                    public void onUploadProgress(PutObjectRequest request, long currentSize, long
                            totalSize) {

                    }
                });
    }

    private void pickAvator() {
        final ImageUploadDialog dialog = new ImageUploadDialog(this, R.style.CustomStyle);
        avatorProcess = new GetPictrueProcess(this);
        dialog.setOnBack(new ImageUploadDialog.OnBack() {

            @Override
            public void click(int btn) {
                dialog.dismiss();
                Intent intent;
                if (R.id.btn_select_picture == btn) {
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_PICK_IMAGE);
                } else {
                    intent = avatorProcess.takePicture(null);
                    startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_TAKE_PICTURE);
                }
            }
        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == GetPictrueProcess.REQUEST_CODE_PICK_IMAGE) {
            Intent intent = avatorProcess.cropAvatar(data.getData());
            startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER);
        } else if (requestCode == GetPictrueProcess.REQUEST_CODE_TAKE_PICTURE) {
            Intent intent = avatorProcess.cropAvatar(null);
            startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER);
        } else if (requestCode == GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER) {
            updateAvator2Server();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传头像到服务器
     */
    private void updateAvator2Server() {
        getOssToken();
    }

    /**
     * 将上传图片信息通知服务器
     *
     * @param path
     */
    private void notifyServer(final String path) {
        ChangeHeadService service = new ChangeHeadService(this);
        service.setCallback(new IOpenApiDataServiceCallback<LoginResponse>() {
                                @Override
                                public void onGetData(final LoginResponse data) {
                                    try {
                                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO,
                                                CacheKeys.USERINFO_LOGINVO, data.data.user);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(SettingActivity.this, "修改成功~",
                                                        Toast.LENGTH_SHORT)
                                                        .show();
                                                LocalBroadcastManager.getInstance(SettingActivity
                                                        .this).sendBroadcast(new Intent
                                                        (PersonFragment.ACCOUNT_INFO_CHANGED));
                                                RoundedImageView headView = (RoundedImageView)
                                                        findViewById(R.id.iv_head);
                                                Glide.with(SettingActivity.this).load(path)
                                                        .into(headView);

                                            }
                                        });
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onGetError(int errorCode, final String errorMsg,
                                                       Throwable error) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SettingActivity.this, errorMsg, Toast
                                                    .LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                                }
                            }
        );
        service.post("imgurl=" + path, true);
    }

}
