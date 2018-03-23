/* 
 * 系统名称：lswuyou
 * 类  名  称：AreaBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-17 下午12:58:35
 * 功能说明： 省级描述的实体
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.modules.mine.activity.school;

import java.io.Serializable;


public class ProvinceBean implements Serializable {
	private static final long serialVersionUID = -5138113500481329989L;
	/** 省级ID */
	public String     pid;
	/** 省级手写字母 */
	public char       f;
	/** 区域全称 */
	public String     fn;
	/** 区域简称 */
	public String     sn;
	/** 省下的城市 */
	public CityBean[] sub;
}
