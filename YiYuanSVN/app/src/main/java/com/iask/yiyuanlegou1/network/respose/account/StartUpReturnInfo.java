/* 
 * 系统名称：lswuyou
 * 类  名  称：StartUpReturnInfo.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-28 下午9:12:48
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.respose.account;

public class StartUpReturnInfo {
	private String latestReleaseVersion;
	private String latestReleaseVersionUrl;
	private String regionUrl;

	public StartUpReturnInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getLatestReleaseVersion() {
		return latestReleaseVersion;
	}

	public void setLatestReleaseVersion(String latestReleaseVersion) {
		this.latestReleaseVersion = latestReleaseVersion;
	}

	public String getLatestReleaseVersionUrl() {
		return latestReleaseVersionUrl;
	}

	public void setLatestReleaseVersionUrl(String latestReleaseVersionUrl) {
		this.latestReleaseVersionUrl = latestReleaseVersionUrl;
	}

	public String getRegionUrl() {
		return regionUrl;
	}

	public void setRegionUrl(String regionUrl) {
		this.regionUrl = regionUrl;
	}
}
