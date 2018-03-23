package com.physicmaster.modules.mine.activity.setting;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.physicmaster.R;
import com.physicmaster.base.AppConfigure;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.account.ConfirmRegisterActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.BindPhoneVerificationResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.BindPhoneVerificationService;
import com.physicmaster.net.service.account.BindPhoneVerifyCodeService;
import com.physicmaster.utils.StringUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TimerButton;
import com.physicmaster.widget.TitleBuilder;

public class BindPhoneActivity extends BaseActivity {


    private TimerButton btnGetTesting;
    private Button btnBind;
    private EditText etPhoneNum;
    private EditText etTesting;
    private String mPhoneInput;
    private String verifyCode;
    private UserDataResponse.UserDataBean.LoginVoBean mDataBean;

    @Override
    protected void findViewById() {
        btnGetTesting = (TimerButton) findViewById(R.id.btn_get_testing);
        btnBind = (Button) findViewById(R.id.btn_bind);
        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        etTesting = (EditText) findViewById(R.id.et_testing);

        mDataBean = BaseApplication.getUserData();
        if (mDataBean == null) {
            gotoLoginActivity();
            return;
        }
        initTitle();
    }

    private void initTitle() {

        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("绑定手机号");

    }

    @Override
    protected void initView() {
        TextChangedWatcher watcher = new TextChangedWatcher();
        etPhoneNum.addTextChangedListener(watcher);
        etTesting.addTextChangedListener(watcher);
        btnGetTesting.setButtonStyle(R.color.colorWhite, R.drawable.button_register_selector,
                R.color.colorWhite, R.drawable.button_register_selector);

        showInputSoft(etPhoneNum);


        btnGetTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestVerificationCode();
            }
        });


        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                veriftCode();

            }
        });
    }

    private void veriftCode() {

        mPhoneInput = getPhoneNum();
        String mtesting = etTesting.getText().toString().trim();

        if (TextUtils.isEmpty(mPhoneInput) || TextUtils.isEmpty(mtesting)) {
            UIUtils.showToast(BindPhoneActivity.this, "请输入手机号码或验证码");
            return;
        }

        final BindPhoneVerifyCodeService service = new BindPhoneVerifyCodeService
                (BindPhoneActivity.this);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                UIUtils.showToast(BindPhoneActivity.this, "绑定成功");
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO,
                        data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                if (mDataBean.isSetPassword == 0) {
                    Intent intent1 = new Intent(BindPhoneActivity.this, ConfirmRegisterActivity
                            .class);
                    intent1.putExtra("data", "revise_pwd");
                    startActivity(intent1);
                }
                finish();
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(BindPhoneActivity.this, errorMsg);
                loadingDialog.dismissDialog();
                if (0 == errorCode) {
                    btnGetTesting.clearStatus();
                }
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("cellphone=" + mPhoneInput + "&verifyCode=" + mtesting, false);


    }

    private String getPhoneNum() {
        String phoneInput = etPhoneNum.getText().toString().trim();
        return phoneInput;
    }

    private void requestVerificationCode() {
        mPhoneInput = getPhoneNum();
        if (!StringUtils.isMobileNO(mPhoneInput)) {
            UIUtils.showToast(this, R.string.register_input_phone_num_not_right);
            return;
        }
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final BindPhoneVerificationService service = new BindPhoneVerificationService(this);
        service.setCallback(new IOpenApiDataServiceCallback<BindPhoneVerificationResponse>() {

            @Override
            public void onGetData(BindPhoneVerificationResponse data) {
                btnGetTesting.onTimerStart();

                if (false == AppConfigure.officialVersion) {
                    verifyCode = data.msg;
                    UIUtils.showToast(BindPhoneActivity.this, data.msg);
                }
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(BindPhoneActivity.this, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("cellphone=" + mPhoneInput, false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_phone;
    }

    class TextChangedWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String strPhone = etPhoneNum.getText().toString();
            String strVerify = etTesting.getText().toString();

            boolean bPhoneNull = (null == strPhone || "".equals(strPhone));
            boolean bVerifyNull = (null == strVerify || "".equals(strVerify));

            boolean running = btnGetTesting.isRunning();
            if (false == running) {
                if (true == bPhoneNull) {
                    btnGetTesting.setEnabled(false);
                } else {
                    btnGetTesting.setEnabled(true);
                }
            }

            if (false == bPhoneNull && false == bVerifyNull) {
                btnBind.setEnabled(true);
            } else {
                btnBind.setEnabled(false);
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btnGetTesting.recycle();
        hideInputSoft(this, etPhoneNum);
    }
}
