package com.physicmaster.modules.mine.activity.setting;

import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.netease.nim.uikit.NimUIKit;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.GuideActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.account.ConfirmRegisterActivity;
import com.physicmaster.modules.mine.activity.AboutActivity;
import com.physicmaster.modules.mine.activity.user.UserActivity;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.modules.videoplay.cache.service.DownloadService2;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.ExitLoginResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.ExitLoginService;
import com.physicmaster.utils.FileSizeUtil;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.RoundImageView;
import com.physicmaster.widget.TitleBuilder;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.util.List;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "SettingActivity";
    private ImageView ivRamind;
    private ImageView ivSoundSwitch;
    private TextView tvClearCache;
    private RelativeLayout rlAbout;
    private RelativeLayout rlClearCache;
    private RelativeLayout rlSoundSwitch;
    private RelativeLayout rlRamind;
    private RelativeLayout rlChagePassword;
    private Button btnExit;
    private boolean isRamind;
    private boolean isSoundSwitch;
    private RelativeLayout rlBindPhone;
    private TextView tvUser;
    private RoundImageView ivChagedHeaders;
    private TextView tvChangePwd;
    private boolean isPwdSet;
    private String packageName;
    private TextView tvAbout;
    private RelativeLayout rlChagedHeaders;
    private AsyncTask<Void, Integer, String> calculateTask;
    private AsyncTask<Void, Integer, Boolean> cleanTask;

    @Override
    protected void findViewById() {
        ivChagedHeaders = (RoundImageView) findViewById(R.id.iv_chaged_headers);
        rlChagedHeaders = (RelativeLayout) findViewById(R.id.rl_chaged_headers);
        rlChagePassword = (RelativeLayout) findViewById(R.id.rl_chage_password);
        rlBindPhone = (RelativeLayout) findViewById(R.id.rl_bind_phone);
        tvChangePwd = (TextView) findViewById(R.id.tv_change_pwd);
        //        rlRamind = (RelativeLayout) findViewById(R.id.rl_ramind);
        rlSoundSwitch = (RelativeLayout) findViewById(R.id.rl_sound_switch);
        rlClearCache = (RelativeLayout) findViewById(R.id.rl_clear_cache);
        tvUser = (TextView) findViewById(R.id.tv_user);
        //        ivRamind = (ImageView) findViewById(R.id.iv_ramind);
        ivSoundSwitch = (ImageView) findViewById(R.id.iv_sound_switch);
        tvClearCache = (TextView) findViewById(R.id.tv_clear_cache);

        rlAbout = (RelativeLayout) findViewById(R.id.rl_about);
        btnExit = (Button) findViewById(R.id.btn_exit);
        tvAbout = (TextView) findViewById(R.id.tv_about);

        initTitle();

        rlChagePassword.setOnClickListener(this);
        rlBindPhone.setOnClickListener(this);
        //        rlRamind.setOnClickListener(this);
        rlSoundSwitch.setOnClickListener(this);
        rlClearCache.setOnClickListener(this);
        rlAbout.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        rlChagedHeaders.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserDataResponse.UserDataBean.LoginVoBean mDataBean = BaseApplication.getUserData();
        if (mDataBean == null) {
            gotoLoginActivity();
            return;
        }
        if (!mDataBean.isLoginByPhone) {
            rlChagePassword.setVisibility(View.GONE);
        } else {
            rlChagePassword.setVisibility(View.VISIBLE);
            if (mDataBean.isSetPassword == 0) {
                tvChangePwd.setText("设置密码");
                isPwdSet = false;
            } else {
                isPwdSet = true;
                tvChangePwd.setText("修改密码");
            }

        }

        if (!TextUtils.isEmpty(mDataBean.nickname)) {
            tvUser.setText(mDataBean.nickname);
        } else {
            tvUser.setText("用户名");
        }
        Glide.with(this).load(mDataBean.portrait).into(ivChagedHeaders);

        //        isRamind = SpUtils.getBoolean(this, "isRamind", true);
        //        if (isRamind) {
        //            ivRamind.setImageResource(R.mipmap.shezhi_kai);
        //        } else {
        //            ivRamind.setImageResource(R.mipmap.shezhi_guan);
        //        }

        isSoundSwitch = SpUtils.getBoolean(this, "isSoundSwitch", true);
        if (isSoundSwitch) {
            ivSoundSwitch.setImageResource(R.mipmap.shezhi_kai);
        } else {
            ivSoundSwitch.setImageResource(R.mipmap.shezhi_guan);
        }
    }

    private void initTitle() {
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("设置");
    }

    @Override
    protected void initView() {
        packageName = mContext.getPackageName();
        if (Constant.PHYSICMASTER.equals(packageName)) {
            tvAbout.setText("关于物理大师");
        } else if (Constant.CHYMISTMASTER.equals(packageName)) {
            tvAbout.setText("关于化学大师");
        } else if (Constant.MATHMASTER.equals(packageName)) {
            tvAbout.setText("关于数学大师");
        }
        calcurateCacheSize();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void onClick(View view) {
        if (BaseApplication.getUserData().isTourist == 1) {
            Utils.gotoLogin(SettingActivity.this);
            return;
        }
        switch (view.getId()) {
            case R.id.rl_chage_password:
                if (isPwdSet) {
                    startActivity(new Intent(this, ChagePasswordActivity.class));
                } else {
                    Intent intent = new Intent(SettingActivity.this, ConfirmRegisterActivity.class);
                    intent.putExtra("data", "revise_pwd");
                    startActivity(intent);
                }
                break;
            case R.id.rl_bind_phone:
                startActivity(new Intent(this, BindingActivity.class));
                break;
            case R.id.rl_sound_switch:
                if (isSoundSwitch) {
                    isSoundSwitch = false;
                    ivSoundSwitch.setImageResource(R.mipmap.shezhi_guan);
                } else {
                    isSoundSwitch = true;
                    ivSoundSwitch.setImageResource(R.mipmap.shezhi_kai);
                }
                //存sp
                SpUtils.putBoolean(this, "isSoundSwitch", isSoundSwitch);
                break;
            case R.id.rl_clear_cache:
                clearCache();
                break;
            case R.id.rl_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.btn_exit:
                exitLogin();
                break;
            case R.id.rl_chaged_headers:
                startActivity(new Intent(this, UserActivity.class));
                break;
        }
    }

    private void exitLogin() {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final ExitLoginService service = new ExitLoginService(SettingActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<ExitLoginResponse>() {
            @Override
            public void onGetData(ExitLoginResponse data) {
                Log.d(TAG, "退出登录成功：onGetData: " + data.msg);
                loadingDialog.dismissDialog();
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO);
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_USERKEY);
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.OSS_SERVER_INFO);
                SpUtils.remove(SettingActivity.this, CacheKeys.SUBJECT_STUDY_INFO);
                SpUtils.remove(SettingActivity.this, CacheKeys.SUBJECT_ACTION);
                SpUtils.remove(SettingActivity.this, CacheKeys.GRAGE_ACTION);
                SpUtils.remove(SettingActivity.this, CacheKeys.SUBJECT_COURSE_INFO);
                SpUtils.remove(SettingActivity.this, CacheKeys.SUBJECT_COURSE_INFO_STR);
                SpUtils.remove(SettingActivity.this, CacheKeys.GRAGE_INFO);
                SpUtils.remove(SettingActivity.this, "isSoundSwitch");
                clearWebViewCache();
                BaseApplication.setUserKey(null);
                BaseApplication.setUserData(null);
                BaseApplication.setNimLogin(false);
                BaseApplication.getStartupDataBean().ossConfig = null;
                quitApplication();
                UMShareAPI.get(SettingActivity.this).deleteOauth(SettingActivity.this,
                        SHARE_MEDIA.WEIXIN, null);
                quitDownloading();
                NimUIKit.doLogout();
                Intent intent = new Intent(SettingActivity.this, GuideActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "退出登录失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(SettingActivity.this, errorMsg);
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

    private void quitDownloading() {
        sendBroadcast(new Intent(DownloadService2.QUIT_DOWNLOADING));
        AsyncTask<String, Integer, Boolean> deleteDownloadingVideoTask = new
                AsyncTask<String, Integer, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        VideoManager videoManager = new VideoManager(SettingActivity.this);
                        List<VideoInfo> videoInfoList = videoManager.getDownloadingVideos();
                        if (videoInfoList != null) {
                            for (VideoInfo videoInfo : videoInfoList) {
                                videoManager.deleteVideoByVideoId(videoInfo.getId());
                            }
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean bool) {
                        super.onPostExecute(bool);
                    }
                };
        deleteDownloadingVideoTask.execute("");
    }

    public void clearWebViewCache() {
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();
    }

    /**
     * 清理缓存
     */
    private void clearCache() {
        //清理Glide缓存图片
        cleanTask = new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                if (isCancelled()) {
                    return false;
                }
                deleteFile(Glide.getPhotoCacheDir(SettingActivity.this));
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (isCancelled()) {
                    return;
                }
                if (aBoolean) {
                    UIUtils.showToast(SettingActivity.this, "缓存已清理！");
                    calcurateCacheSize();
                }
            }
        };
        cleanTask.execute();
    }

    /**
     * 删除文件或文件夹
     *
     * @param file
     */
    private void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i]);
            }
        }
        file.delete();
    }

    /**
     * 计算缓存大小
     */
    private void calcurateCacheSize() {
        calculateTask = new AsyncTask<Void, Integer, String>() {
            @Override
            protected String doInBackground(Void... params) {
                if (isCancelled()) {
                    return "";
                }
                String cacheSize = FileSizeUtil.getAutoFileOrFilesSize(Glide.getPhotoCacheDir(SettingActivity.this).getAbsolutePath());
                return cacheSize;
            }

            @Override
            protected void onPostExecute(String s) {
                if (isCancelled()) {
                    return;
                }
                super.onPostExecute(s);
                tvClearCache.setText(s);
            }
        };
        calculateTask.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cleanTask != null) {
            cleanTask.cancel(true);
        }
        if (calculateTask != null) {
            calculateTask.cancel(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
