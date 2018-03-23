/* 
 * 系统名称：lswuyou
 * 类  名  称：OpenRegisterBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-16 下午5:38:15
 * 功能说明： 第三方注册
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;

import com.iask.yiyuanlegou1.common.Constant;


public class OpenRegisterBean{
	public OpenRegisterBean(Context context) {
	}

	/** int,用户类型（由于目前只有老师用户，所以此处为0）：-1：未知（允许用户后续修改）；0：老师；1：学生；2：家长。必填 */
	public String utype;
	/** string,第三方登录获得的uid，（在ShareSDK中此项应该叫uid）。必填 */
	public String uid;
	/** int,登录类型：2：微信，3：QQ，4：微博。必填 */
	public String btype;
	/** string,昵称。必填 */
	public String nick;
	/** string，必填，（即第三方平台返回的accessToken，服务端会依据此值校验第三方登录授权的真实性） */
	public String token;

	public String portrait;
	/** string,性别。支持：男/女/male/female */
	public String gender;
	/** 教育阶段，老师必填，1：小学；2：初中；3：高中 */
	public String eduStage;
	/** 科目ID，老师必填 见：教育阶段与科目数据 */
	public String subjectId;
	public String realName = "";


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("utype=" + utype);
		sb.append("&btype=" + btype);
		sb.append("&eduStage=" + eduStage);
		sb.append("&subjectId=" + subjectId);
		try {
			sb.append("&uid=" + URLEncoder.encode(uid, Constant.CHARACTER_ENCODING));
			sb.append("&nick=" + URLEncoder.encode(nick, Constant.CHARACTER_ENCODING));
			sb.append("&token=" + URLEncoder.encode(token, Constant.CHARACTER_ENCODING));
			sb.append("&portrait=" + URLEncoder.encode(portrait, Constant.CHARACTER_ENCODING));
			sb.append("&gender=" + URLEncoder.encode(gender, Constant.CHARACTER_ENCODING));
			sb.append("&realName=" + URLEncoder.encode(realName, Constant.CHARACTER_ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString() + super.toString();
	}
}
