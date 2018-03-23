package com.physicmaster.modules.mine.activity.location;

import android.view.View;

import com.physicmaster.R;
import com.physicmaster.base.BaseFragmentActivity;
import com.physicmaster.widget.TitleBuilder;

public class SelectLocationActivity extends BaseFragmentActivity {

    @Override
    protected void findViewById() {


        //	initTitle();
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
                .setMiddleTitleText("选择地区");

    }

    @Override
    protected void initView() {

        SelectProvinceFragment fragment = new SelectProvinceFragment();
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.location_frame_lt, fragment).commit();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_location;
    }

    @Override
    public void onBackPressed() {
        int index = getSupportFragmentManager().getBackStackEntryCount();
        if (index == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
