/* 
 * 系统名称：lswuyou
 * 类  名  称：ImageUploadDialog.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-21 上午9:04:12
 * 功能说明： 图片上传Dialog
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.physicmaster.R;


public class ImageUploadDialog extends Dialog implements OnClickListener {
	/** 从相册选择图片按钮、拍摄图片按钮、取消按钮 */
	private Button btnSelectPic, btnTakePic, btnCancel;
	/** 点击回调接口 */
	private OnBack onBack;
	public ImageUploadDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.image_upload_dialog);
		findViewById();
		initView();
	}

	private void initView() {
		btnSelectPic.setOnClickListener(this);
		btnTakePic.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	private void findViewById() {
		btnSelectPic = (Button) findViewById(R.id.btn_select_picture);
		btnTakePic = (Button) findViewById(R.id.btn_take_picture);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_select_picture:
			onBack.click(R.id.btn_select_picture);
			break;
		case R.id.btn_take_picture:
			onBack.click(R.id.btn_take_picture);
			break;
		case R.id.btn_cancel:
			dismiss();
			break;

		default:
			break;
		}
	}

	public OnBack getOnBack() {
		return onBack;
	}

	public void setOnBack(OnBack onBack) {
		this.onBack = onBack;
	}

	@Override
	public void show() {
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.BOTTOM;
		getWindow().setAttributes(params);
		getWindow().setWindowAnimations(R.style.bottom_dialog_anim);
		super.show();
	}

	/** 点击回调接口 */
	public interface OnBack {
		void click(int btn);
	}
}
