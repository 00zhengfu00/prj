package com.iask.yiyuanlegou1.network.respose.pay;

/**
 * Created by Administrator on 2016/6/1.
 */
public class MsgInfo {
    // 主信息
    private String mainMsg;
    // 副信息
    private String deputyMsg;

    public String getMainMsg() {
        return mainMsg;
    }

    public void setMainMsg(String mainMsg) {
        this.mainMsg = mainMsg;
    }

    public String getDeputyMsg() {
        return deputyMsg;
    }

    public void setDeputyMsg(String deputyMsg) {
        this.deputyMsg = deputyMsg;
    }
}
