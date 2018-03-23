/* 
 * 系统名称：lswuyou
 * 类  名  称：LoginUserInfo.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-19 上午10:51:13
 * 功能说明：  
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.respose.account;

import java.io.Serializable;
import java.util.List;

public class LoginUserInfo implements Serializable {
	private static final long serialVersionUID = 3561195615969329647L;
	/** 当前用户已经绑定的信息 */
	private List<UserBindInfo> userBinds;
	/** 性别：未知:0；男性:1；女性:2 */
	private String gender;
	/** 纬度 */
	private String gisLatitude;
	/** 经度 */
	private String gisLongitude;
	/** 地理位置 */
	private String gisLocation;
	/** 用于环信登录的密码（后续会接入环信聊天） */
	private String hxPassword;
	/** 当前用户是否设置过密码 */
	private String isSetPassword;
	/** 用户真实姓名 */
	private String realName;
	/** 用于环信登录的用户名（后续会接入环信聊天） */
	private String openId;
	/** 头像URL */
	private String portrait;
	/** 用户id */
	private String userId;
	/** 用户userKey */
	private String userKey;
	/** 当前用户的类型：-1：未知（允许用户后续修改）；0：老师；1：学生；2：家长。必填 */
	private int userType;
	/** 当前用户的无忧号（唯一），意义类似于微信号 */
	private String wyId;
	/** 无忧号设置的次数，无忧号只允许用户修改一次（含注册时，自动生成的无忧号） */
	private int wyIdSetCount;
	/** 用户手机号 */
	private String cellPhone;
	/** 用户出生日期 */
	private String birthday;
	/** 省份ID */
	private String provinceId;
	/** 城市ID */
	private String cityId;
	/** 区域ID */
	private String areaId;
	/** 学校ID */
	private String schoolId;
	/** 学校名称 */
	private String schoolName;
	/** 年级和科目ID */
	private String stageAndSubject;
	/** 教育阶段 (老师特有) (-1：未知；1：小学；2：初中；3：高中) */
	private int eduStage;
	/** 学科ID (老师特有) */
	private int subjectId;
	/** 头像缩略图URL，可能为空白字符串 */
	private String portraitSmall;

	public static final int USER_TYPE_STUDENT = 1;
	public static final int USER_TYPE_TEACHER = 0;
	public static final int USER_TYPE_PARENT = 2;

	public String getPortraitSmall() {
		return portraitSmall;
	}

	public void setPortraitSmall(String portraitSmall) {
		this.portraitSmall = portraitSmall;
	}

	public int getEduStage() {
		return eduStage;
	}

	public void setEduStage(int eduStage) {
		this.eduStage = eduStage;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	/** provinceId / cityId / areaId / schoolId / schoolName / stageAndSubject */

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getStageAndSubject() {
		return stageAndSubject;
	}

	public void setStageAndSubject(String stageAndSubject) {
		this.stageAndSubject = stageAndSubject;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public List<UserBindInfo> getUserBinds() {
		return userBinds;
	}

	public void setUserBinds(List<UserBindInfo> userBinds) {
		this.userBinds = userBinds;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getGisLatitude() {
		return gisLatitude;
	}

	public void setGisLatitude(String gisLatitude) {
		this.gisLatitude = gisLatitude;
	}

	public String getGisLongitude() {
		return gisLongitude;
	}

	public void setGisLongitude(String gisLongitude) {
		this.gisLongitude = gisLongitude;
	}

	public String getGisLocation() {
		return gisLocation;
	}

	public void setGisLocation(String gisLocation) {
		this.gisLocation = gisLocation;
	}

	public String getHxPassword() {
		return hxPassword;
	}

	public void setHxPassword(String hxPassword) {
		this.hxPassword = hxPassword;
	}

	public String getIsSetPassword() {
		return isSetPassword;
	}

	public void setIsSetPassword(String isSetPassword) {
		this.isSetPassword = isSetPassword;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getWyId() {
		return wyId;
	}

	public void setWyId(String wyId) {
		this.wyId = wyId;
	}

	public int getWyIdSetCount() {
		return wyIdSetCount;
	}

	public void setWyIdSetCount(int wyIdSetCount) {
		this.wyIdSetCount = wyIdSetCount;
	}

}
