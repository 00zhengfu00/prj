/* 
 * 系统名称：lswuyou
 * 类  名  称：AreaBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-17 下午12:58:35
 * 功能说明： 城市描述的实体
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;


public class CityBean implements Parcelable, Serializable {
	private static final long serialVersionUID = 360311408987541857L;
	/** 城市ID */
	public String cid;
	/** 城市手写字母 */
	public char f;
	/** 城市全称 */
	public String name;
	/** 城市简称 */
	public String sn;
	/** 城市下面的区域 */
	public AreaBean[] sub;

	@Override
	public int describeContents() {
		return 0;
	}
	public static final Creator<CityBean> CREATOR = new Creator<CityBean>() {

		@Override
		public CityBean createFromParcel(Parcel source) {
			return new CityBean();
		}

		@Override
		public CityBean[] newArray(int size) {
			return new CityBean[size];
		}

	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(cid);
		dest.writeString(name);
		dest.writeString(sn);
		dest.writeArray(sub);
	}
}
