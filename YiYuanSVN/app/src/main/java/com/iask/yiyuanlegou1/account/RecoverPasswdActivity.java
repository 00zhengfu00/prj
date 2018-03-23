package com.iask.yiyuanlegou1.account;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.AppConfigure;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.CommonResponse;
import com.iask.yiyuanlegou1.network.respose.account.UserInfo;
import com.iask.yiyuanlegou1.network.service.account.ResetPasswdService;
import com.iask.yiyuanlegou1.network.service.account.VerifyCodeService;
import com.iask.yiyuanlegou1.utils.StringUtils;
import com.iask.yiyuanlegou1.widget.TitleBarView;


public class RecoverPasswdActivity extends BaseActivity {

    private TitleBarView title;
    private EditText vericodeE, phoneE;
    private String mobile;
    private boolean phoneExist = false;
    private LinearLayout phoneLayout;
    @Override
    protected void findViewById() {
        title = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        title.setCommonTitle(View.VISIBLE, View.GONE);
        title.setTitleText(R.string.recoverpwd);
        title.setBtnRight(0, R.string.login);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                RecoverPasswdActivity.this.finish();
            }
        });
        vericodeE = (EditText) findViewById(R.id.vericode);
        phoneE = (EditText) findViewById(R.id.phone);
        phoneLayout = (LinearLayout) findViewById(R.id.phone_layout);
        findViewById(R.id.getIdcode).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doGetVeriCode();
            }
        });
        findViewById(R.id.sure_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doResetPassword();
            }
        });
        Object obj = CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, UserInfo.class);
        if (obj != null) {
            phoneLayout.setVisibility(View.GONE);
            UserInfo info = (UserInfo) obj;
            mobile = info.getMobile();
            phoneExist = true;
        } else {
            phoneLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_recoverpasswd;
    }

    private void doGetVeriCode() {
        if (!phoneExist) {
            String phone = phoneE.getText().toString();
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(RecoverPasswdActivity.this, "手机号不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!StringUtils.isMobileNO(phone)) {
                Toast.makeText(RecoverPasswdActivity.this, "手机号不合法！", Toast.LENGTH_SHORT).show();
                return;
            }
            mobile = phone;
        }
        VerifyCodeService service = new VerifyCodeService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                Toast.makeText(RecoverPasswdActivity.this, data.getMsg(), Toast.LENGTH_SHORT)
                        .show();
                if (!AppConfigure.officialVersion) {
                    vericodeE.setText(data.getMsg());
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(RecoverPasswdActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("phone=" + mobile + "&type=1", true);
    }

    private void doResetPassword() {
        EditText editPwd = (EditText) findViewById(R.id.passwd);
        String pwd = editPwd.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(RecoverPasswdActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText editRePwd = (EditText) findViewById(R.id.passwd_re);
        String rePwd = editRePwd.getText().toString();
        if (TextUtils.isEmpty(rePwd)) {
            Toast.makeText(RecoverPasswdActivity.this, "请重复密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pwd.equals(rePwd)) {
            Toast.makeText(RecoverPasswdActivity.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        EditText editVericode = (EditText) findViewById(R.id.vericode);
        String veriCode = editVericode.getText().toString();
        if (TextUtils.isEmpty(veriCode)) {
            Toast.makeText(RecoverPasswdActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        ResetPasswdService service = new ResetPasswdService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                Toast.makeText(RecoverPasswdActivity.this, data.getMsg(), Toast.LENGTH_SHORT)
                        .show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecoverPasswdActivity.this.finish();
//                        CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys
// .USERINFO_LOGINVO);
//                        Intent intent = new Intent();
//                        intent.setClass(RecoverPasswdActivity.this,LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(RecoverPasswdActivity.this, errorMsg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        service.post("phone=" + mobile + "&pwd=" + pwd + "&code=" + veriCode, true);
    }
}
