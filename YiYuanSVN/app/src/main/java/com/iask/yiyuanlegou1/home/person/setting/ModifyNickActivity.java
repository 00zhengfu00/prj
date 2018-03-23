package com.iask.yiyuanlegou1.home.person.setting;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.home.person.record.BuyRecordActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.account.LoginResponse;
import com.iask.yiyuanlegou1.network.service.account.ChangeNickService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

public class ModifyNickActivity extends BaseActivity {
    private TitleBarView title;
    private EditText etNick;

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setTitleText(R.string.set_nick);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyNickActivity.this.finish();
            }
        });
        etNick = (EditText) findViewById(R.id.nickName);
        String nick = getIntent().getStringExtra("nick");
        etNick.setText(nick);
        findViewById(R.id.sure_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nick = etNick.getText().toString();
                doChangeNick(nick);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_nick;
    }

    private void doChangeNick(String nick) {
        if (TextUtils.isEmpty(nick)) {
            Toast.makeText(ModifyNickActivity.this, "昵称输入不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        ChangeNickService service = new ChangeNickService(this);
        service.setCallback(new IOpenApiDataServiceCallback<LoginResponse>() {
            @Override
            public void onGetData(LoginResponse data) {
                try {
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .USERINFO_LOGINVO, data.data.user);
                    LocalBroadcastManager.getInstance(ModifyNickActivity.this).sendBroadcast(new
                            Intent(SettingActivity.INFO_CHANGED));
                    ModifyNickActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ModifyNickActivity.this, "修改成功~", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                    ModifyNickActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, final String errorMsg, Throwable error) {
                ModifyNickActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ModifyNickActivity.this, errorMsg, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        });
        try {
            nick = URLEncoder.encode(nick, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.post("username=" + nick, true);
    }
}
