package com.iask.yiyuanlegou1.network.respose.product;

/**
 * Created by Administrator on 2016/6/13.
 */
public class ObtainedItem {
    //商品id
    private Integer itemId;
    //缩略图
    private String thumb;
    //期数
    private Integer qishu;
    //名
    private String  title;
    //订单号
    private String  code;
    //订单状态
    private String  status;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
