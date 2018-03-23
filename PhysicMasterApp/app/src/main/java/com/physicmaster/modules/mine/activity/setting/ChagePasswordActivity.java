package com.physicmaster.modules.mine.activity.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.modules.account.ConfirmRegisterActivity;
import com.physicmaster.modules.account.FindPwdActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.ForgetPwdResponse;
import com.physicmaster.net.service.account.ForgetPwdService;
import com.physicmaster.utils.MD5;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

public class ChagePasswordActivity extends BaseActivity {

    private static final String TAG = "ChagePasswordActivity";

    private EditText etBeforePwd;
    private String   beforePwd;
    private TextView tvForgetPwd;
    private Button   btnNext;

    @Override
    protected void findViewById() {
        etBeforePwd = (EditText) findViewById(R.id.et_before_pwd);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
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
                .setMiddleTitleText("修改密码");

    }

    @Override
    protected void initView() {
        tvForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChagePasswordActivity.this, FindPwdActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chagePwdByService();
            }
        });
    }

    private void chagePwdByService() {

        beforePwd = etBeforePwd.getText().toString().trim();
        if (TextUtils.isEmpty(beforePwd)) {
            UIUtils.showToast(ChagePasswordActivity.this, "请输入原密码");
            return;
        }

        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final ForgetPwdService service = new ForgetPwdService(ChagePasswordActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<ForgetPwdResponse>() {
            @Override
            public void onGetData(ForgetPwdResponse data) {
                Log.d(TAG, "onGetData: " + data.msg);
                Intent intent = new Intent(ChagePasswordActivity.this, ConfirmRegisterActivity.class);
                intent.putExtra("data", "revise_pwd");
                intent.putExtra("beforePwd", beforePwd);
                startActivity(intent);
                finish();
                loadingDialog.dismissDialog();

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "onGetData: " + errorMsg.toString());
                UIUtils.showToast(ChagePasswordActivity.this,errorMsg);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("opwd=" + MD5.hexdigest(beforePwd), false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_chage_password;
    }
}
