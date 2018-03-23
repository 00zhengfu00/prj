package com.iask.yiyuanlegou1.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iask.yiyuanlegou1.common.SystemMethod;
import com.iask.yiyuanlegou1.R;

public class TitleBarView extends RelativeLayout {
	// private static final String TAG = "TitleBarView";
	private Context mContext;
	private Button btnLeft;
	private Button btnRight;
	private TextView tv_center;
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
		btnRight = (Button) findViewById(R.id.title_btn_right);
		tv_center = (TextView) findViewById(R.id.title_txt);
		titleBarBgLt = (RelativeLayout) findViewById(R.id.title_bar_bg);
	}

	public void setCommonTitle(int LeftVisibility, int rightVisibility) {
		btnLeft.setVisibility(LeftVisibility);
		btnRight.setVisibility(rightVisibility);
	}

	public void setBtnLeft(int icon, int txtRes) {
		btnLeft.setText(getResources().getString(txtRes));
		if (icon == 0) {
			btnLeft.setCompoundDrawables(null, null, null, null);
		} else {
			Drawable img = mContext.getResources().getDrawable(icon);
			int height = SystemMethod.dip2px(mContext, 35);
			int width = img.getIntrinsicWidth() * height
					/ img.getIntrinsicHeight();
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
			int height = SystemMethod.dip2px(mContext, 22);
			int width = img.getIntrinsicWidth() * height
					/ img.getIntrinsicHeight();
			img.setBounds(0, 0, width, height);
			btnRight.setCompoundDrawables(img, null, null, null);
		}
	}

	public void setBtnLeftStr(int txtRes) {
		btnLeft.setText(txtRes);
	}

	public void setBtnRightStr(int txtRes) {
		btnRight.setText(txtRes);
	}

	public void setBtnRight(int icon) {
		Drawable img = mContext.getResources().getDrawable(icon);
		int height = SystemMethod.dip2px(mContext, 22);
		int width = img.getIntrinsicWidth() * height / img.getIntrinsicHeight();
		img.setBounds(0, 0, width, height);
		btnRight.setCompoundDrawables(null, null, img, null);
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