/* 
 * 系统名称：lswuyou
 * 类  名  称：TextInputSchoolFragment.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-22 上午9:44:23
 * 功能说明： 手动输入学校名称
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.home.person.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.bean.account.UpdateSchoolBean;
import com.iask.yiyuanlegou1.network.respose.account.LoginResponse;
import com.iask.yiyuanlegou1.network.service.account.UpdateSchoolInfoService;
import com.iask.yiyuanlegou1.utils.StringUtils;
import com.iask.yiyuanlegou1.utils.UIUtils;
import com.iask.yiyuanlegou1.utils.ViewUtils;
import com.iask.yiyuanlegou1.widget.ClearEditText;
import com.iask.yiyuanlegou1.widget.TitleBarView;


public class TextInputSchoolFragment extends Fragment {
	private View rootView;
	/** 显示地区 */
	private TextView tvArea;

	/** 输入学校 */
	private ClearEditText cetSchool;

	/** 提交按钮 */
	private Button btnSubmit;
	private TitleBarView mTitleBarView;

	/** 区域ID */
	private String aid;

	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_text_input_school, container, false);
			initView();
			initData();
		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
	}

	private void initData() {
		Bundle bundle = getArguments();
		String area = bundle.getString("area");
		aid = bundle.getString("aid");
		tvArea.setText(area);
	}

	private void initView() {
		tvArea = (TextView) rootView.findViewById(R.id.tv_select_area);
		btnSubmit = (Button) rootView.findViewById(R.id.submit_btn);
		cetSchool = (ClearEditText) rootView.findViewById(R.id.et_input_school);
		mTitleBarView = (TitleBarView) rootView.findViewById(R.id.title);
		mTitleBarView = (TitleBarView) rootView.findViewById(R.id.title);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE);
		mTitleBarView.setTitleTextStr("手动输入学校");
		mTitleBarView.setBtnLeft(R.mipmap.back, R.string.whitespace);
		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				backTypeWriting();
				getActivity().onBackPressed();
			}
		});
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				backTypeWriting();
				String school = cetSchool.getText().toString();
				if (StringUtils.isEmpty(school)) {
					UIUtils.showToast(getActivity(), "输入不能为空！");
					return;
				}
				UpdateSchoolBean bean = new UpdateSchoolBean(aid, school, "");
				UpdateSchoolInfoService service = new UpdateSchoolInfoService(getActivity());
				service.setCallback(new IOpenApiDataServiceCallback<LoginResponse>() {

					@Override
					public void onGetData(LoginResponse data) {
						UIUtils.showToast(getActivity(), "添加成功！");
						Intent intent = new Intent();
						getActivity().sendBroadcast(intent);
						getActivity().finish();
					}

					@Override
					public void onGetError(int errorCode, String errorMsg, Throwable error) {
						UIUtils.showToast(getActivity(), errorMsg);
					}
				});
				service.postAES(bean.toString(), true);
			}
		});
		cetSchool.addTextChangedListener(watcher);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				cetSchool.requestFocus();
				cetSchool.setFocusableInTouchMode(true);
				ViewUtils.showInputSoft(getActivity(), cetSchool);
			}
		}, 500);
	}

	/** 收起输入法 */
	private void backTypeWriting() {
		InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		View view = getActivity().getCurrentFocus();
		if (null != view) {
			manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			if (!StringUtils.isEmpty(str)) {
				btnSubmit.setEnabled(true);
			} else {
				btnSubmit.setEnabled(false);
			}
		}
	};
}
