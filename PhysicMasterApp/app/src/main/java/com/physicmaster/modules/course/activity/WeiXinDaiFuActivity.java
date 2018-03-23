package com.physicmaster.modules.course.activity;

import android.view.View;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.widget.TitleBuilder;

public class WeiXinDaiFuActivity extends BaseActivity {


    @Override
    protected void findViewById() {

        initTitle();

    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this)
                .setLeftImageRes(R.mipmap.fanhui)
                .setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("代付")
                .setRightImageRes(R.mipmap.daifu)
                .setRightTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_wen_xin_dai_fu;
    }
}
