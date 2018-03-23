package com.iask.yiyuanlegou1.home.timing;

import android.view.View;

import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.widget.TitleBarView;
import com.iask.yiyuanlegou1.R;


public class PublishActivity extends BaseActivity {
	private TitleBarView title;
	@Override
	protected void findViewById() {
		title = (TitleBarView) findViewById(R.id.title);
	}

	@Override
	protected void initView() {
		title.setCommonTitle(View.GONE, View.GONE);
		title.setTitleText(R.string.app_name);
	}

	@Override
	protected int getContentLayout() {
		return R.layout.activity_publish;
	}

}
