package com.iask.yiyuanlegou1.home.person.setting;

import android.view.View;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.home.person.record.BuyRecordActivity;
import com.iask.yiyuanlegou1.widget.TitleBarView;

public class ModifyEmailActivity extends BaseActivity {
    private TitleBarView title;
    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setTitleText(R.string.set_email);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyEmailActivity.this.finish();
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_email;
    }
}
