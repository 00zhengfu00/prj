package com.iask.yiyuanlegou1.network.respose.product;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ProductClassifyListBean {
    // 商品id
    private Integer id;
    // 商品标题
    private String title;
    // 商品封面
    private String thumb;
    // 总人次
    private Integer zongrenshu;
    // 已参与人次
    private Integer canyurenshu;
    // 剩余人次
    private Integer shenyurenshu;
    // 云购人次价格
    private BigDecimal yunjiage;

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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Integer getZongrenshu() {
        return zongrenshu;
    }

    public void setZongrenshu(Integer zongrenshu) {
        this.zongrenshu = zongrenshu;
    }

    public Integer getCanyurenshu() {
        return canyurenshu;
    }

    public void setCanyurenshu(Integer canyurenshu) {
        this.canyurenshu = canyurenshu;
    }

    public Integer getShenyurenshu() {
        return shenyurenshu;
    }

    public void setShenyurenshu(Integer shenyurenshu) {
        this.shenyurenshu = shenyurenshu;
    }

    public BigDecimal getYunjiage() {
        return yunjiage;
    }

    public void setYunjiage(BigDecimal yunjiage) {
        this.yunjiage = yunjiage;
    }
}
