/*
 * 系统名称：lswuyou
 * 类  名  称：TitleBarViews.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-7-28 上午11:21:04
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.lswuyou.tv.pm.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;


public class TitleBarView extends RelativeLayout {
	// private static final String TAG = "TitleBarView";
	private Context mContext;
	private Button btnLeft;
	private DigitalClock btnRight;
	private Button btn_titleLeft;
	private Button btn_titleRight;
	private TextView tv_center;
	private LinearLayout common_constact;
	private RelativeLayout titleBarBgLt;

	public TitleBarView(Context context) {
		super(context);
		mContext = context;
		initView();
	}

	public TitleBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(mContext).inflate(R.layout.common_title_bar, this);
		btnLeft = (Button) findViewById(R.id.title_btn_left);
		btnRight = (DigitalClock) findViewById(R.id.title_btn_right);
		btn_titleLeft = (Button) findViewById(R.id.constact_group);
		btn_titleRight = (Button) findViewById(R.id.constact_all);
		tv_center = (TextView) findViewById(R.id.title_txt);
		common_constact = (LinearLayout) findViewById(R.id.common_constact);
		titleBarBgLt = (RelativeLayout) findViewById(R.id.title_bar_bg);

//		btnRight.setFormat24Hour("HH:mm");
//		btnRight.setTimeZone("china/beijing");
	}

	public void setCommonTitle(int LeftVisibility, int centerVisibility, int center1Visibilter, int rightVisibility) {
		btnLeft.setVisibility(LeftVisibility);
		btnRight.setVisibility(rightVisibility);
		tv_center.setVisibility(centerVisibility);
		common_constact.setVisibility(center1Visibilter);
	}

	public void setBtnLeft(int icon, int txtRes) {
		btnLeft.setText(getResources().getString(txtRes));
		if (icon == 0) {
			btnLeft.setCompoundDrawables(null, null, null, null);
		} else {
			Drawable img = mContext.getResources().getDrawable(icon);
			int height = mContext.getResources().getDimensionPixelOffset(R.dimen.y40);
			int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
			img.setBounds(0, 0, width, height);
			btnLeft.setCompoundDrawables(img, null, null, null);
		}
	}

	public void setBtnRight(int icon, int txtRes) {
		btnRight.setText(getResources().getString(txtRes));
		if (icon == 0) {
			btnRight.setCompoundDrawables(null, null, null, null);
		} else {
			Drawable img = mContext.getResources().getDrawable(icon);
			int height = mContext.getResources().getDimensionPixelOffset(R.dimen.y40);
			int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
			img.setBounds(0, 0, width, height);
			btnRight.setCompoundDrawables(img, null, null, null);
		}
	}

	public void setBtnLeftStr(String txtRes) {
		btnLeft.setText(txtRes);
	}

	public void setBtnRightStr(int txtRes) {
		btnRight.setText(txtRes);
	}

	public void setBtnRight(int icon) {
		Drawable img = mContext.getResources().getDrawable(icon);
		int height = mContext.getResources().getDimensionPixelOffset(R.dimen.y40);
		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		btnRight.setCompoundDrawables(img, null, null, null);
	}

	public void setTitleLeft(int resId) {
		btn_titleLeft.setText(resId);
	}

	public void setTitleRight(int resId) {
		btn_titleRight.setText(resId);
	}

	@SuppressLint("NewApi")
	public void setPopWindow(PopupWindow mPopWindow, TitleBarView titleBaarView) {
	}

	public void setTitleText(int txtRes) {
		tv_center.setText(txtRes);
	}

	public void setTitleTextStr(String txtStr) {
		tv_center.setText(txtStr);
	}

	public void setBtnLeftOnclickListener(OnClickListener listener) {
		btnLeft.setOnClickListener(listener);
	}

	public void setBtnRightOnclickListener(OnClickListener listener) {
		btnRight.setOnClickListener(listener);
	}

	public Button getTitleLeft() {
		return btn_titleLeft;
	}

	public Button getTitleRight() {
		return btn_titleRight;
	}

	public TextView getTitleTextView() {
		return tv_center;
	}

	public void destoryView() {
		btnLeft.setText(null);
		btnRight.setText(null);
		tv_center.setText(null);
	}

	public void setTitleBarBg(int color) {
		titleBarBgLt.setBackgroundColor(color);
	}

	public void setBtnLeftTextColor(int color) {
		btnLeft.setTextColor(color);
	}

	public void setBtnRightTextColor(int color) {
		btnRight.setTextColor(color);
	}
}