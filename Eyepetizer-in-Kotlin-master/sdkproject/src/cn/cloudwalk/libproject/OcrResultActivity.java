/**
 * Project Name:cwFaceForDev3
 * File Name:OcrResultActivity.java
 * Package Name:cn.cloudwalk.dev.mobilebank
 * Date:2016-5-11 9:18:25
 * Copyright @ 2010-2016 Cloudwalk Information Technology Co.Ltd All Rights Reserved.
 *
 */

package cn.cloudwalk.libproject;

import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import cn.cloudwalk.libproject.TemplatedActivity;
import cn.cloudwalk.libproject.util.ImgUtil;

/**
 * ClassName: OcrResultActivity <br/>
 * Description: 身份证识别结果页面 <br/>
 * date: 2016-5-11 9:18:25 <br/>
 * 
 * @author 284891377
 * @version
 * @since JDK 1.7
 */
public class OcrResultActivity extends TemplatedActivity {

	ImageView mIv_head;
	public static JSONObject frontJb;
	public static JSONObject backJb;
	TextView mTv_info, mTv_backinfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cloudwalk_activity_ocrresult);
		setTitle("身份证识别");
		initView();

	}

	private void initView() {
		mIv_head = (ImageView) findViewById(R.id.iv_head);
		mTv_info = (TextView) findViewById(R.id.tv_info);
		mTv_backinfo = (TextView) findViewById(R.id.tv_backinfo);
		try {
			setFrontInfo();
			setBackInfo();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private void setBackInfo() {
		if (backJb != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("签发机关    ");
			sb.append(backJb.optString("authority"));
			sb.append("\n有效期        ");
			sb.append(backJb.optString("validdate1"));
			sb.append("  -  ");
			sb.append(backJb.optString("validdate2"));
			mTv_backinfo.setText(sb.toString());
		}

	}

	private void setFrontInfo() {
		if (frontJb != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("姓名          ");
			sb.append(frontJb.optString("name"));
			sb.append("\n性别          ");
			sb.append(frontJb.optString("sex"));
			sb.append("\n民族           ");
			sb.append(frontJb.optString("folk"));
			sb.append("\n身份证号  ");
			sb.append(frontJb.optString("cardno"));
			sb.append("\n出生          ");
			sb.append(frontJb.optString("birthday"));
			sb.append("\n地址          ");
			sb.append(frontJb.optString("address"));
			mTv_info.setText(sb.toString());

			String headBase64Str = frontJb.optJSONObject("face").optString("image");
			mIv_head.setImageBitmap(ImgUtil.bytesToBimap(Base64.decode(headBase64Str, Base64.DEFAULT)));

		}

	}

}
