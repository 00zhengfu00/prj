package com.iask.yiyuanlegou1.network.respose.product;

/**
 * Created by Administrator on 2016/6/13.
 */
public class NotObtainedItem {
    //商品id
    private Integer itemId;
    //商品缩略图
    private String thumb;
    //期数
    private Integer qishu;
    //商品名
    private String title;
    //中奖人名
    private String userName;
    //中奖码
    private String q_user_code;
    //揭晓时间(年月日,时分秒)
    private String q_end_time;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Integer getQishu() {
        return qishu;
    }

    public void setQishu(Integer qishu) {
        this.qishu = qishu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQ_user_code() {
        return q_user_code;
    }

    public void setQ_user_code(String q_user_code) {
        this.q_user_code = q_user_code;
    }

    public String getQ_end_time() {
        return q_end_time;
    }

    public void setQ_end_time(String q_end_time) {
        this.q_end_time = q_end_time;
    }
}
