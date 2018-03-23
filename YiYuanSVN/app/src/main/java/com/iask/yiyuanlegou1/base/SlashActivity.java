package com.iask.yiyuanlegou1.base;

import android.content.Intent;
import android.os.Handler;

import com.iask.yiyuanlegou1.home.HomeActivity;
import com.iask.yiyuanlegou1.R;


public class SlashActivity extends BaseActivity {

	@Override
	protected void findViewById() {

	}

	@Override
	protected void initView() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				start();
			}
		}, 1000);
	}

	private void start() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected int getContentLayout() {
		return R.layout.activity_slash;
	}

}
