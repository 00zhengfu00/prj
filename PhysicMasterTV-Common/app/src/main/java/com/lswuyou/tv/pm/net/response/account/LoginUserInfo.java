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
package com.lswuyou.tv.pm.net.response.account;

import java.io.Serializable;
import java.util.List;

public class LoginUserInfo implements Serializable {
    private static final long serialVersionUID = 3561195615969329647L;
    public String cellphone;
    public int dtUserId;
    public int gender;
    public String nickname;
    public String portrait;
    public String userKey;
    public String userSecret;
    public String memberUrl;//http://m.thelper.cn/c/p/tv/member/f56L",    //新增会员购买页地址   客户端用于生成二维码
    public int userStatus;
    public int isBindWx;
}
