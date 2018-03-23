/* 
 * 系统名称：lswuyou
 * 类  名  称：AreaBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-17 下午12:58:35
 * 功能说明： 区域描述的实体
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class AreaBean implements Parcelable, Serializable {
	private static final long serialVersionUID = -4609773702434591777L;
	/** 区域ID */
	public String aid;
	/** 区域手写字母 */
	public char f;
	/** 区域全称 */
	public String name;
	/** 区域简称 */
	public String sn;
	/** 区域等级结构名称*/
	public String mn;

	public static final Creator<AreaBean> CREATOR = new Creator<AreaBean>() {

		@Override
		public AreaBean createFromParcel(Parcel source) {
			return new AreaBean();
		}

		@Override
		public AreaBean[] newArray(int size) {
			return new AreaBean[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(aid);
		dest.writeString(name);
		dest.writeString(sn);
		dest.writeString(f + "");
	}
}