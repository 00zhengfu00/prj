/* 
 * 系统名称：lswuyou
 * 类  名  称：DeviceUuidFactory.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-17 下午2:26:22
 * 功能说明： 获取或者生成设备唯一ID：首先获取AndroidID，获取失败然后随机生成一个设备ID
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.common;


import android.content.Context;
import android.provider.Settings.Secure;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class DeviceUuidFactory {
	protected static UUID uuid;

	public DeviceUuidFactory(Context context) {
		if (uuid == null) {
			synchronized (DeviceUuidFactory.class) {
				if (uuid == null) {
					final String androidId = Secure.getString(
							context.getContentResolver(), Secure.ANDROID_ID);
					try {
						// 部分手机厂商的bug，每个设备返回的都是9774d56d682e549c
						if (!"9774d56d682e549c".equals(androidId)) {
							uuid = UUID.nameUUIDFromBytes(androidId
									.getBytes("utf8"));
						} else {
							uuid = UUID.randomUUID();
						}
					} catch (UnsupportedEncodingException e) {
						uuid = UUID.randomUUID();
					}
				}
			}
		}
	}

	public UUID getDeviceUuid() {
		return uuid;
	}
}
