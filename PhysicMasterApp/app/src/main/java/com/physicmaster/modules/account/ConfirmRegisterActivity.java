package com.physicmaster.modules.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.MainActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.account.basics.SelectPetActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.bean.ChagePwdBean;
import com.physicmaster.net.bean.Register4PhoneBean;
import com.physicmaster.net.response.account.ChagePwdResponse;
import com.physicmaster.net.response.account.FindPwdResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.ChagePwdService;
import com.physicmaster.net.service.account.FindPwd4PhoneService;
import com.physicmaster.net.service.account.Register4PhoneService;
import com.physicmaster.utils.MD5;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.StringUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

public class ConfirmRegisterActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG        = "ConfirmRegisterActivity";
    private              String mTitleText = "注册";
    private Button btnConfirm;
    private int pwdEye     = 0;//默认不显示密码
    private int accountEye = 0;//默认不显示密码
    private ImageView ivAccountEyE;
    private ImageView ivPwdEyE;
    private EditText  etPwd;
    private EditText  etAgainPwd;
    private int       accountType;
    private String    mToken;
    private String    mPhoneNum;

    public static final String EXTRA_TOKEN = "token";
    public static final String EXTRA_PHONE = "phone";
    private String                                    passwd;
    private String                                    beforePwd;
    private UserDataResponse.UserDataBean.LoginVoBean mDataBean;
    boolean isbindPhone = false;

    @Override
    protected void findViewById() {

        ivAccountEyE = (ImageView) findViewById(R.id.iv_account_eye);
        ivPwdEyE = (ImageView) findViewById(R.id.iv_pwd_eye);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etAgainPwd = (EditText) findViewById(R.id.et_again_pwd);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        beforePwd = intent.getStringExtra("beforePwd");

        if ("find_pwd".equals(data)) {
            mTitleText = "设置新密码";
        } else if ("revise_pwd".equals(data)) {
            mTitleText = "设置登录密码";
        } else {
            mTitleText = "注册";
        }


        initTitle();
        initData();
    }

    private void initTitle() {

        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText(mTitleText);

    }

    private void initData() {
        Bundle b = getIntent().getExtras();
        if (null == b) {
            return;
        }
        accountType = b.getInt(Constant.ACCOUNT_TYPE, 0);
        /** 手机注册的时候才会传入这些数据 */
        if (Constant.ACCOUNT_TYPE_LSWY == accountType) {
            mToken = b.getString(EXTRA_TOKEN);
            mPhoneNum = b.getString(EXTRA_PHONE);
        }
    }

    @Override
    protected void initView() {
        btnConfirm.setOnClickListener(this);
        ivAccountEyE.setOnClickListener(this);
        ivPwdEyE.setOnClickListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_confirm_register;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_account_eye:
                accountEye = accountEye == 0 ? 1 : 0;
                setAccountEye();
                break;
            case R.id.iv_pwd_eye:
                pwdEye = pwdEye == 0 ? 1 : 0;
                setAgainEye();
                break;
            case R.id.btn_confirm:
                doVerityCode();
                break;
        }
    }

    private void doVerityCode() {

        passwd = etPwd.getText().toString().trim();
        if (passwd.length() < 6) {
            UIUtils.showToast(ConfirmRegisterActivity.this, "密码长度不能少于6位");
            return;
        }

        String againPwd = etAgainPwd.getText().toString().trim();
        if (StringUtils.isEmpty(againPwd)) {
            UIUtils.showToast(ConfirmRegisterActivity.this, "请再次输入密码");
            return;
        }

        if (!againPwd.equals(passwd)) {
            UIUtils.showToast(ConfirmRegisterActivity.this, "两次密码不一致");
            return;
        }

        if ("注册".equals(mTitleText)) {
            register4phone();
        } else if ("设置登录密码".equals(mTitleText)) {
            chagePwdByService();
        } else {
            findpwd();

        }
    }

    private void chagePwdByService() {
        final String opwd = (TextUtils.isEmpty(beforePwd)) ? "" : MD5.hexdigest(beforePwd);
        ChagePwdBean bean = new ChagePwdBean(MD5.hexdigest(passwd), opwd);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final ChagePwdService service = new ChagePwdService(ConfirmRegisterActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<ChagePwdResponse>() {
            @Override
            public void onGetData(ChagePwdResponse data) {
                Log.d(TAG, "onGetData: " + data.msg);

                if ("".equals(opwd)) {
                    UIUtils.showToast(ConfirmRegisterActivity.this, "设置成功，请重新登录");
                } else {
                    UIUtils.showToast(ConfirmRegisterActivity.this, "修改成功，请重新登录");
                }
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO);
                BaseApplication.setUserData(null);
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_USERKEY);
                BaseApplication.setUserKey(null);
                SpUtils.remove(ConfirmRegisterActivity.this, "isSoundSwitch");
                Intent intent = new Intent(ConfirmRegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                loadingDialog.dismissDialog();

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "onGetData: " + errorMsg.toString());
                UIUtils.showToast(ConfirmRegisterActivity.this, errorMsg);
                finish();
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined(bean.toString(), false);
    }

    private void findpwd() {

        Register4PhoneBean bean = new Register4PhoneBean(mPhoneNum, mToken, MD5.hexdigest(passwd)
                , "");
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final FindPwd4PhoneService service = new FindPwd4PhoneService(ConfirmRegisterActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<FindPwdResponse>() {
            @Override
            public void onGetData(FindPwdResponse data) {
                Log.d(TAG, "onGetData: " + data.msg);
                UIUtils.showToast(ConfirmRegisterActivity.this, "修改成功，请重新登录");
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO);
                BaseApplication.setUserData(null);
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_USERKEY);
                BaseApplication.setUserKey(null);
                SpUtils.remove(ConfirmRegisterActivity.this, "isSoundSwitch");
                Intent intent = new Intent(ConfirmRegisterActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                loadingDialog.dismissDialog();

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "onGetData: " + errorMsg.toString());
                UIUtils.showToast(ConfirmRegisterActivity.this, errorMsg);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postUnLogin(bean.toString(), false);
    }

    private void register4phone() {
        String miPushId = SpUtils.getString(this, SpUtils.MI_PUSH_ID, "");
        Register4PhoneBean bean = new Register4PhoneBean(mPhoneNum, mToken, MD5.hexdigest(passwd)
                , miPushId);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final Register4PhoneService service = new Register4PhoneService(ConfirmRegisterActivity
                .this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                Log.d(TAG, "注册成功：onGetData: " + data);
                UIUtils.showToast(ConfirmRegisterActivity.this, "注册成功");
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                        .USERINFO_USERKEY, data.data.keyVo);
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                        .USERINFO_LOGINVO, data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                BaseApplication.setUserKey(data.data.keyVo);
                if (data.data.loginVo.isInitial == 0) {
//                    Intent intent = new Intent(ConfirmRegisterActivity.this, BasicsActivity.class);
                    Intent intent = new Intent(ConfirmRegisterActivity.this, SelectPetActivity.class);
                    UIUtils.showToast(ConfirmRegisterActivity.this, "请完善基本信息");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ConfirmRegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    hideInputSoft(ConfirmRegisterActivity.this, etAgainPwd);
                }
                finish();
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "注册失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(ConfirmRegisterActivity.this, errorMsg);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postUnLogin(bean.toString(), false);
    }


    //输入密码
    private void setAccountEye() {
        switch (accountEye) {
            case 0:
                etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());//显示密文密码
                ivAccountEyE.setBackground(getResources().getDrawable(R.mipmap.register_close));
                break;
            case 1:
                etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                //显示明文密码
                ivAccountEyE.setBackground(getResources().getDrawable(R.mipmap.register_open));
                break;
        }
        etPwd.setSelection(etPwd.getText().length());
    }

    //再次输入密码
    private void setAgainEye() {
        switch (pwdEye) {
            case 0:
                etAgainPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //显示密文密码
                ivPwdEyE.setBackground(getResources().getDrawable(R.mipmap.register_close));
                break;
            case 1:
                etAgainPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                ;//显示明文密码
                ivPwdEyE.setBackground(getResources().getDrawable(R.mipmap.register_open));
                break;
        }
        etAgainPwd.setSelection(etAgainPwd.getText().length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideInputSoft(this, etPwd);
    }
}
