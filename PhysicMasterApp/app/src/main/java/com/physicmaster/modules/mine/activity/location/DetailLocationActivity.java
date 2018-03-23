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

public class DetailLocationActivity extends BaseActivity {


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
                .setMiddleTitleText("详细地址")
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
            UIUtils.showToast(this, "请输入详细地址");
            return;
        }
        Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$");
        Matcher m = p.matcher(mName);
        if (!m.matches()) {
            UIUtils.showToast(this, "不可以包含特殊符号哦！");
            return;
        }

        UIUtils.showToast(DetailLocationActivity.this, "修改成功");
        CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_LOCATION2, mName);
        Intent intent = new Intent();
        intent.putExtra("location2", mName);
        setResult(30, intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String name = CacheManager.getString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_LOCATION2);
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
        return R.layout.activity_detail_location;
    }
}
