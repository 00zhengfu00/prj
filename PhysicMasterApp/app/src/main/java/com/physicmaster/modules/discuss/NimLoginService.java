package com.physicmaster.modules.discuss;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.discuss.config.preference.UserPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huashigen on 2017/5/8.
 */

public class NimLoginService extends IntentService {
    private static final String TAG = "NimLoginService";
    private AbortableFuture<LoginInfo> loginRequest;
    public static final String NIM_LOGIN_SUCC = "nim_login_succ";

    public NimLoginService() {
        super("nimls");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NimLoginService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String account = intent.getStringExtra("account");
        String token = intent.getStringExtra("token");
        login(account, token);
    }

    /**
     * ***************************************** 云信登录 **************************************
     */
    private void login(String account, String token) {
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        // 登录
        loginRequest = NimUIKit.doLogin(new LoginInfo(account, token), new
                RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        LogUtil.i(TAG, "login success");
                        // 初始化消息提醒配置
                        initNotificationConfig();
                        LocalBroadcastManager.getInstance(NimLoginService.this).sendBroadcast(new Intent(NIM_LOGIN_SUCC));
                        BaseApplication.setNimLogin(true);
                        updateUserPortrait();
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 302 || code == 404) {
                            Toast.makeText(NimLoginService.this, R.string.login_failed, Toast
                                    .LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(NimLoginService.this, "登录失败: " + code, Toast
                                    .LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Toast.makeText(NimLoginService.this, R.string.login_exception, Toast
                                .LENGTH_LONG)
                                .show();
                    }
                });
    }

    /**
     * 更新用户头像
     */
    private void updateUserPortrait() {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.AVATAR, BaseApplication.getUserData().portraitSmall);
        fields.put(UserInfoFieldEnum.Name, BaseApplication.getUserData().nickname);
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {

                    }
                });
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }
}
