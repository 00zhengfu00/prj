package com.physicmaster.modules.mine.activity.school;/*
 * 系统名称：lswuyou
 * 类  名  称：SelectSchoolActivity.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2016-1-18 下午8:46:59
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */


import android.view.View;

import com.physicmaster.R;
import com.physicmaster.base.BaseFragmentActivity;
import com.physicmaster.widget.TitleBuilder;

public class SelectSchoolActivity extends BaseFragmentActivity {

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
		getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.person_school_frame_lt, fragment).commit();
	}

	@Override
	protected int getContentLayout() {
		return R.layout.activity_person_school;
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
