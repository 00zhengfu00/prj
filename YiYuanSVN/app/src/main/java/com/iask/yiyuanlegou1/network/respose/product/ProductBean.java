package com.iask.yiyuanlegou1.network.respose.product;

/**
 * Created by Administrator on 2016/5/21.
 */
public class ProductBean {
    // 商品id
    private Integer id;
    //商品标题
    private String title;
    // 期数
    private Integer nper;
    // 商品封面
    private String cover;
    // 中奖人姓名
    private String userName;
    // 中奖码
    private String code;
    // 揭晓时间 yyyy-MM-dd HH:mm:ss
    private String announceTime;
    // 倒计时 毫秒数
    private Long countDown;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Long getCountDown() {
        return countDown;
    }

    public void setCountDown(Long countDown) {
        this.countDown = countDown;
    }

    public Integer getPartakeTimes() {
        return partakeTimes;
    }

    public void setPartakeTimes(Integer partakeTimes) {
        this.partakeTimes = partakeTimes;
    }
}
