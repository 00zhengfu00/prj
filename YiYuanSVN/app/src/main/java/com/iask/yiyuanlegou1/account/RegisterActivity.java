package com.iask.yiyuanlegou1.account;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.AppConfigure;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.home.HomeActivity;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.CommonResponse;
import com.iask.yiyuanlegou1.network.respose.account.RegisterResponse;
import com.iask.yiyuanlegou1.network.service.account.RegisterService;
import com.iask.yiyuanlegou1.network.service.account.VerifyCodeService;
import com.iask.yiyuanlegou1.utils.StringUtils;
import com.iask.yiyuanlegou1.widget.TitleBarView;


public class RegisterActivity extends BaseActivity implements OnClickListener {
    private TitleBarView title;
    private EditText registerU, registerP, vericodeE;
    private Button btngetIdcode, btnSubmit;
    private String mobile;
    private Logger logger = AndroidLogger.getLogger(RegisterActivity.class);

    @Override
    protected void findViewById() {
        title = (TitleBarView) findViewById(R.id.title);
        registerU = (EditText) findViewById(R.id.register_u);
        registerP = (EditText) findViewById(R.id.register_p);
        vericodeE = (EditText) findViewById(R.id.vericode);

        btngetIdcode = (Button) findViewById(R.id.getIdcode);
        btnSubmit = (Button) findViewById(R.id.register_btn);

        btngetIdcode.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });
        title.setTitleText(R.string.register);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                doRegister();
                break;
            case R.id.getIdcode:
                doGetVeriCode();
                break;
        }
    }

    private void doRegister() {
        String pwd = registerP.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        String veriCode = vericodeE.getText().toString();
        if (TextUtils.isEmpty(veriCode)) {
            Toast.makeText(RegisterActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        RegisterService service = new RegisterService(this);
        service.setCallback(new IOpenApiDataServiceCallback<RegisterResponse>() {
            @Override
            public void onGetData(RegisterResponse data) {
                try {
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .USERINFO_LOGINVO, data.data.user);
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .OSS_SERVER_INFO, data.data.aliyun);
                    CacheManager.saveString(CacheManager.TYPE_PUBLIC, CacheKeys
                            .APPDATA_CURR_REGIONURL, data.data.aliyun.getRegionUrl());
                    BaseApplication.getInstance().setUserId(data.data.user.getUserId() + "");
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    RegisterActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("phone=" + mobile + "&pwd=" + pwd + "&code=" + veriCode, true);
    }

    private void doGetVeriCode() {
        mobile = registerU.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(RegisterActivity.this, "手机号码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isMobileNO(mobile)) {
            Toast.makeText(RegisterActivity.this, "手机号码格式不正确！", Toast.LENGTH_SHORT).show();
            return;
        }
        VerifyCodeService service = new VerifyCodeService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                logger.debug(data.getMsg());
                Toast.makeText(RegisterActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                if (!AppConfigure.officialVersion) {
                    vericodeE.setText(data.getMsg());
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                logger.debug(errorMsg);
                Toast.makeText(RegisterActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("phone=" + mobile + "&type=0", true);
    }
}
