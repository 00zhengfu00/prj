/* 
 * 系统名称：lswuyou
 * 类  名  称：OpenLoginBean.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-19 上午9:38:44
 * 功能说明： 第三方登录参数bean
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.bean.account;

import java.net.URLEncoder;

import android.content.Context;

import com.iask.yiyuanlegou1.common.Constant;


public class OpenLoginBean {
    public OpenLoginBean(Context context) {

    }

    /**
     * 第三方登录获得的uid。必填
     */
    private String uid;
    /**
     * 登录类型：2：微信，3：QQ，4：微博。必填
     */
    private String token;

    private String btype;
    /**
     * 昵称。必填
     */
    private String nick;
    /**
     * 头像。必填
     */
    private String portrait;
    /**
     * 性别。支持：男/女/male/female
     */
    private String gender;


    public static final String ACCOUNT_NICKNAME = "nickname";
    public static final String ACCOUNT_GENDER = "gender";
    public static final String ACCOUNT_PORTRAIT = "portrait";
    public static final String ACCOUNT_LOCATION = "location";
    public static final String ACCOUNT_UID = "uid";

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBtype() {
        return btype;
    }

    public void setBtype(String btype) {
        this.btype = btype;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("&uid="
                    + URLEncoder.encode(uid, Constant.CHARACTER_ENCODING));
            sb.append("&token="
                    + URLEncoder.encode(token, Constant.CHARACTER_ENCODING));
            sb.append("&btype="
                    + URLEncoder.encode(btype, Constant.CHARACTER_ENCODING));
            sb.append("&nick="
                    + URLEncoder.encode(nick, Constant.CHARACTER_ENCODING));
            sb.append("&portrait="
                    + URLEncoder.encode(portrait, Constant.CHARACTER_ENCODING));
            sb.append("&gender="
                    + URLEncoder.encode(gender, Constant.CHARACTER_ENCODING));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString() + super.toString();
    }
}
