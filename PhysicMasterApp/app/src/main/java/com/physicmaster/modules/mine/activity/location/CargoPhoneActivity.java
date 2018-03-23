package com.physicmaster.modules.mine.activity.location;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.TitleBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CargoPhoneActivity extends BaseActivity {

    public static final String ABCDEFG                    = "abcdefg";
    public static final String ABCDEFGHIJKLMNOPQRSTUVWXYZ = "abcdefghijklmnopqrstuvwxyz";
    private ImageView ivClean;
    private EditText  etChageName;
    private String    mName;

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
                .setMiddleTitleText("联系电话")
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
            UIUtils.showToast(this, "请输入联系电话");
            return;
        }
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(mName);
        if (!m.matches()) {
            UIUtils.showToast(this, "请输入正确的电话号码！");
            return;
        }

        UIUtils.showToast(CargoPhoneActivity.this, "修改成功");
        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_PHONE, mName);
        Intent intent = new Intent();
        intent.putExtra("phone", mName);
        setResult(20, intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String name = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_PHONE);
        if (!TextUtils.isEmpty(name)) {
            etChageName.setText(name);
            etChageName.setSelection(etChageName.getText().length());
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
        return R.layout.activity_cargo_phone;
    }
}
