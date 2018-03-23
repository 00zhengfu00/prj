package com.physicmaster.modules.mine.activity.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.user.ChageUserService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChageNameActivity extends BaseActivity {


    public static final String ABCDEFG = "abcdefg";
    public static final String ABCDEFGHIJKLMNOPQRSTUVWXYZ = "abcdefghijklmnopqrstuvwxyz";
    private ImageView ivClean;
    private EditText etChageName;
    private String mName;

    @Override
    protected void findViewById() {
        etChageName = (EditText) findViewById(R.id.et_chage_name);
        ivClean = (ImageView) findViewById(R.id.iv_clean);
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
                .setMiddleTitleText("修改昵称")
                .setRightText("保存")
                .setRightTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chageName();
                    }
                });
    }

    private void chageName() {
        mName = etChageName.getText().toString().trim();

        if (TextUtils.isEmpty(mName)) {
            UIUtils.showToast(this, "请输入昵称");
            return;
        }
        Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$");
        Matcher m = p.matcher(mName);
        if (!m.matches()) {
            UIUtils.showToast(this, "不可以包含特殊符号哦！");
            return;
        }

        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final ChageUserService service = new ChageUserService(this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {

            @Override
            public void onGetData(UserDataResponse data) {
                UIUtils.showToast(ChageNameActivity.this, "修改成功");
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                Intent intent = new Intent();
                intent.putExtra("name", data.data.loginVo.nickname);
                setResult(10, intent);
                finish();
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(ChageNameActivity.this, errorMsg);
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
            service.postLogined("nickname=" + URLEncoder.encode(mName, Constant.CHARACTER_ENCODING), false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserDataResponse.UserDataBean.LoginVoBean mDataBean = BaseApplication.getUserData();
        if (null != mDataBean) {
            if (!TextUtils.isEmpty(mDataBean.nickname)) {
                etChageName.setText(mDataBean.nickname);
            } else {
                etChageName.setText("昵称");
            }
            etChageName.setSelection(etChageName.getText().length());
        } else {
            gotoLoginActivity();
            return;
        }
    }

    @Override
    protected void initView() {
        ivClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etChageName.setText("");
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_chage_name;
    }
}
