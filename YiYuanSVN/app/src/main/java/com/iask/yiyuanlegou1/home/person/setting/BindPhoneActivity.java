package com.iask.yiyuanlegou1.home.person.setting;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.AppConfigure;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.home.person.record.BuyRecordActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.CommonResponse;
import com.iask.yiyuanlegou1.network.respose.account.LoginResponse;
import com.iask.yiyuanlegou1.network.service.account.BindPhoneService;
import com.iask.yiyuanlegou1.network.service.account.VerifyCodeService;
import com.iask.yiyuanlegou1.utils.StringUtils;
import com.iask.yiyuanlegou1.widget.TitleBarView;

public class BindPhoneActivity extends BaseActivity {
    private TitleBarView title;
    private String phone;

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setTitleText(R.string.bind_phone);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindPhoneActivity.this.finish();
            }
        });
        findViewById(R.id.sure_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBind();
            }
        });
        findViewById(R.id.getIdcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetVeriCode();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_phone;
    }

    private void submitBind() {
        EditText editPhone = (EditText) findViewById(R.id.phone);
        String phone = editPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(BindPhoneActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText editCode = (EditText) findViewById(R.id.vericode);
        String veriCode = editCode.getText().toString();
        if (TextUtils.isEmpty(veriCode)) {
            Toast.makeText(BindPhoneActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        BindPhoneService service = new BindPhoneService(this);
        service.setCallback(new IOpenApiDataServiceCallback<LoginResponse>() {
            @Override
            public void onGetData(LoginResponse data) {
                try {
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .USERINFO_LOGINVO, data.data.user);
                    LocalBroadcastManager.getInstance(BindPhoneActivity.this).sendBroadcast(new
                            Intent(SettingActivity.INFO_CHANGED));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BindPhoneActivity.this, "绑定成功~", Toast.LENGTH_SHORT)
                                    .show();
                            BindPhoneActivity.this.finish();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, final String errorMsg, Throwable error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BindPhoneActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        service.post("phone=" + phone + "&code=" + veriCode, true);
    }


    private void doGetVeriCode() {
        EditText editPhone = (EditText) findViewById(R.id.phone);
        String phone = editPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(BindPhoneActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isMobileNO(phone)) {
            Toast.makeText(BindPhoneActivity.this, "手机号码格式不正确！", Toast.LENGTH_SHORT).show();
            return;
        }
        VerifyCodeService service = new VerifyCodeService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                Toast.makeText(BindPhoneActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                if (!AppConfigure.officialVersion) {
                    EditText editCode = (EditText) findViewById(R.id.vericode);
                    editCode.setText(data.getMsg());
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(BindPhoneActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("phone=" + phone + "&type=2", true);
    }
}
