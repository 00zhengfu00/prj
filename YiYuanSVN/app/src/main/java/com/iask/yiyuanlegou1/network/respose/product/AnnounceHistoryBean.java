package com.iask.yiyuanlegou1.network.respose.product;

/**
 * Created by Administrator on 2016/5/26.
 */
public class AnnounceHistoryBean {
    // 商品id
    private Integer id;
    // 商品标题
    private String title;
    // 期数
    private Integer nper;
    // 中奖人头像
    private String photo;
    // 中奖码
    private String code;
    // 揭晓时间 yyyy-MM-dd HH:mm:ss
    private String announceTime;
    // 参与人次
    private Integer partakeTimes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
