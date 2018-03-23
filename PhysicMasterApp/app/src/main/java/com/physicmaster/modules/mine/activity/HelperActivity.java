package com.physicmaster.modules.mine.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.excercise.HelperResponse;
import com.physicmaster.net.service.excercise.HelperService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HelperActivity extends BaseActivity {


    private static final String TAG = "HelperActivity";
    private Button   btnSubmit;
    private EditText etHelper;

    @Override
    protected void findViewById() {
        etHelper = (EditText) findViewById(R.id.et_helper);
        btnSubmit = (Button) findViewById(R.id.btn_submit);


        initTitle();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("帮助与反馈");

    }

    @Override
    protected void initView() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHelper();

            }
        });
    }

    private void submitHelper() {

        String content = etHelper.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            UIUtils.showToast(this,"请输入您的意见或者建议，谢谢！");
            return;
        }

        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final HelperService service = new HelperService(HelperActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<HelperResponse>() {
            @Override
            public void onGetData(HelperResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                loadingDialog.dismissDialog();
                etHelper.setText("");
                UIUtils.showToast(HelperActivity.this,"提交成功,谢谢您的建议。");

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(HelperActivity.this,errorMsg);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        try {
            service.postLogined("content=" + URLEncoder.encode(content, Constant.CHARACTER_ENCODING), false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_helper;
    }
}
