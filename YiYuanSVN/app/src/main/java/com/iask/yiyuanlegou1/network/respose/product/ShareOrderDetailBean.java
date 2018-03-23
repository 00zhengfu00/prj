package com.iask.yiyuanlegou1.network.respose.product;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ShareOrderDetailBean {
    // 中奖人头像
    private String photo;
    // 商品id
    private Integer itemId;
    // 商品期数
    private Integer nper;
    // 商品标题
    private String title;
    // 中奖码
    private String code;
    // 揭晓时间 yyyy-MM-dd HH:mm:ss
    private String announceTime;
    // 参与人次
    private Integer partakeTimes;
    // 中奖人姓名
    private String userName;
    // 晒单标题
    private String orderTitle;
    // 晒单内容
    private String content;
    // 晒单图片
    private List<String> image;
    // 晒单时间 yyyy-MM-dd HH:mm:ss
    private String time;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getNper() {
        return nper;
    }

    public void setNper(Integer nper) {
        this.nper = nper;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAnnounceTime() {
        return announceTime;
    }

    public void setAnnounceTime(String announceTime) {
        this.announceTime = announceTime;
    }

    public Integer getPartakeTimes() {
        return partakeTimes;
    }

    public void setPartakeTimes(Integer partakeTimes) {
        this.partakeTimes = partakeTimes;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
