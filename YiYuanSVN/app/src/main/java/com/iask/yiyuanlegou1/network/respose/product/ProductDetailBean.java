package com.iask.yiyuanlegou1.network.respose.product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/5/21.
 */
public class ProductDetailBean {
    // 商品id
    private Integer id;
    // 商品同一个id
    private Integer sid;
    // 商品标题
    private String title;
    // 副标题
    private String subtitle;
    // 期数
    private Integer nper;
    // 商品图片
    private List<String> image;
    // 中奖人ID
    private Integer userId;
    // 中奖人姓名
    private String userName;
    // 中奖人头像
    private String photo;
    // 中奖码
    private String code;
    // 揭晓时间 yyyy-MM-dd HH:mm:ss
    private String announceTime;
    // 中奖用户参与人次
    private Integer partakeTimes;
    // 最新一期的商品id
    private Integer newItemId;
    // 倒计时 毫秒数
    private Long countDown;
    // 总人次
    private Integer totalNum;
    // 已参与人次
    private Integer partakeNum;
    // 剩余人次
    private Integer leftNum;
    // 商品封面图
    private String cover;
    // 云购人次价格
    private BigDecimal unitPrice;
    // 状态 0-进行中,1-开奖中,2-已揭晓
    private Integer status;
    // 登录用户购买人次
    private Integer buyTimes;
    //展示用户ID
    private Integer displayId;

    public Integer getDisplayId() {
        return displayId;
    }

    public void setDisplayId(Integer displayId) {
        this.displayId = displayId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Integer getNper() {
        return nper;
    }

    public void setNper(Integer nper) {
        this.nper = nper;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public Integer getNewItemId() {
        return newItemId;
    }

    public void setNewItemId(Integer newItemId) {
        this.newItemId = newItemId;
    }

    public Long getCountDown() {
        return countDown;
    }

    public void setCountDown(Long countDown) {
        this.countDown = countDown;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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
