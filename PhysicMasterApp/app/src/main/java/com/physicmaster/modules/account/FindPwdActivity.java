package com.physicmaster.modules.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.physicmaster.R;
import com.physicmaster.base.AppConfigure;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.bean.VerificationBean;
import com.physicmaster.net.bean.VerifyCodeBean;
import com.physicmaster.net.response.account.VerificationResponse;
import com.physicmaster.net.response.account.VerifyCodeResponse;
import com.physicmaster.net.service.account.VerificationService;
import com.physicmaster.net.service.account.VerifyCodeService;
import com.physicmaster.utils.StringUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TimerButton;
import com.physicmaster.widget.TitleBuilder;

public class FindPwdActivity extends BaseActivity {


    private Button      btnNext;
    private TimerButton btnGetTesting;
    private EditText    etTesting;
    private EditText    etPhoneNum;
    private String      mPhoneInput;

    @Override
    protected void findViewById() {

        etPhoneNum = (EditText) findViewById(R.id.et_phone_num);
        etTesting = (EditText) findViewById(R.id.et_testing);
        btnGetTesting = (TimerButton) findViewById(R.id.btn_get_testing);
        btnNext = (Button) findViewById(R.id.btn_next);

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
                .setMiddleTitleText("密保手机");

    }

    @Override
    protected void initView() {
        FindPwdActivity.TextChangedWatcher watcher = new FindPwdActivity.TextChangedWatcher();
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


        btnNext.setOnClickListener(new View.OnClickListener() {
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
            UIUtils.showToast(FindPwdActivity.this, "请输入手机号码或验证码");

            return;
        }


        VerifyCodeBean bean = new VerifyCodeBean(mPhoneInput, "1", mtesting);
        final VerifyCodeService service = new VerifyCodeService(FindPwdActivity.this);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        service.setCallback(new IOpenApiDataServiceCallback<VerifyCodeResponse>() {

            private String mToken;

            @Override
            public void onGetData(VerifyCodeResponse data) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(FindPwdActivity.this, "验证码验证成功");
                mToken = data.data.getVerifyToken();
                Intent intent = new Intent(FindPwdActivity.this, ConfirmRegisterActivity.class);
                Bundle b = new Bundle();
                b.putString(ConfirmRegisterActivity.EXTRA_TOKEN, mToken);
                b.putString(ConfirmRegisterActivity.EXTRA_PHONE, mPhoneInput);
                b.putInt(Constant.ACCOUNT_TYPE, Constant.ACCOUNT_TYPE_LSWY);
                intent.putExtras(b);
                intent.putExtra("data", "find_pwd");
                startActivity(intent);
                finish();

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(FindPwdActivity.this, errorMsg);
                loadingDialog.dismissDialog();
                if (0==errorCode) {
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
        service.postUnLogin(bean.toString(), false);


    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_find_pwd;
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
        VerificationBean bean = new VerificationBean(mPhoneInput, "1");
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final VerificationService service = new VerificationService(this);
        service.setCallback(new IOpenApiDataServiceCallback<VerificationResponse>() {

            @Override
            public void onGetData(VerificationResponse data) {
                btnGetTesting.onTimerStart();

                if (false == AppConfigure.officialVersion) {
                    UIUtils.showToast(FindPwdActivity.this, data.msg);
                }
                loadingDialog.dismissDialog();

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(FindPwdActivity.this, errorMsg);

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
                btnNext.setEnabled(true);
            } else {
                btnNext.setEnabled(false);
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideInputSoft(this, etPhoneNum);
    }
}
