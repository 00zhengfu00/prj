package com.lswuyou.tv.pm.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.utils.Utils;
import com.lswuyou.tv.pm.view.AlertDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;

public class LoginedActivity extends BaseActivity implements View.OnClickListener {
    private RoundedImageView ivWeixinHead;
    private TextView tvNick, tvClearCache, tvLogout;
    private AsyncTask<Void, Void, Boolean> clearCacheTask;

    @Override
    protected void findViewById() {
        ivWeixinHead = (RoundedImageView) findViewById(R.id.iv_weixin_head);
        tvNick = (TextView) findViewById(R.id.tv_nick);
        tvClearCache = (TextView) findViewById(R.id.tv_clear_cache);
        tvLogout = (TextView) findViewById(R.id.tv_logout);
        tvClearCache.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        if (Utils.isUserLogined()) {
            LoginUserInfo userInfo = (LoginUserInfo) CacheManager.getObject(CacheManager
                    .TYPE_USER_INFO, CacheKeys
                    .USERINFO_LOGINVO, LoginUserInfo.class);
            Glide.with(this).load(userInfo.portrait).into(ivWeixinHead);
            tvNick.setText(userInfo.nickname);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_logined;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_clear_cache) {
            AlertDialog dialog = new AlertDialog(this, "确定清除缓存吗？", new AlertDialog
                    .OnSubmitListener() {
                @Override
                public void onClick(boolean bool) {
                    if (bool) {
                        clearCache();
                    }
                }
            });
            dialog.show();
        } else {
            AlertDialog dialog = new AlertDialog(this, "确定退出登录吗？", new AlertDialog.OnSubmitListener() {
                @Override
                public void onClick(boolean bool) {
                    if (bool) {
                        BaseApplication.setLoginUserInfo(null);
                        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO);
                        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.PLAY_RECORD);
                        LocalBroadcastManager.getInstance(LoginedActivity.this).sendBroadcast(new Intent(UserFragment.USERINFO_UPDATE));
//                        startActivity(new Intent(LoginedActivity.this, MainActivity.class));
                        finish();
                    }
                }
            });
            dialog.show();
        }
    }

    /**
     * 清理缓存
     */
    private void clearCache() {
        //清理Glide缓存图片
        clearCacheTask = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                if (isCancelled()) {
                    return false;
                }
                deleteFile(Glide.getPhotoCacheDir(LoginedActivity.this));
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(LoginedActivity.this, "缓存已清理！", Toast.LENGTH_SHORT).show();
                }
                super.onPostExecute(aBoolean);
            }
        };
        clearCacheTask.execute();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clearCacheTask != null) {
            clearCacheTask.cancel(true);
        }
    }
}
