package com.iask.yiyuanlegou1.network.respose.product;

/**
 * Created by Administrator on 2016/5/25.
 */
public class BuyRecordBean {
    // 记录id
    private Integer id;
    // 商品id
    private Integer itemId;
    // 商品标题
    private String title;
    // 期数
    private Integer nper;
    // 中奖人姓名
    private String userName;
    // 中奖用户参与人次
    private Integer partakeTimes;
    // 最新一期的商品id
    private Integer newItemId;
    // 总人次
    private Integer totalNum;
    // 已参与人次
    private Integer partakeNum;
    // 剩余人次
    private Integer leftNum;
    // 商品封面图
    private String cover;
    // 状态 0-进行中,1-开奖中,2-已揭晓
    private Integer status;
    // 登录用户购买人次
    private Integer buyTimes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNper() {
        return nper;
    }

    public void setNper(Integer nper) {
        this.nper = nper;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getPartakeTimes() {
        return partakeTimes;
    }

    public void setPartakeTimes(Integer partakeTimes) {
        this.partakeTimes = partakeTimes;
    }

    public Integer getNewItemId() {
        return newItemId;
    }

    public void setNewItemId(Integer newItemId) {
        this.newItemId = newItemId;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getPartakeNum() {
        return partakeNum;
    }

    public void setPartakeNum(Integer partakeNum) {
        this.partakeNum = partakeNum;
    }

    public Integer getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(Integer leftNum) {
        this.leftNum = leftNum;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(Integer buyTimes) {
        this.buyTimes = buyTimes;
    }
}
