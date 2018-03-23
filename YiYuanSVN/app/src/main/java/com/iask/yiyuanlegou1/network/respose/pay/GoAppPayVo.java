package com.iask.yiyuanlegou1.network.respose.pay;

/**
 * Created by Administrator on 2016/5/31.
 */
public class GoAppPayVo {
    // 支付名称
    private String name;
    // 支付参数
    private String type;
    // 支付图片
    private String img;
    // 支付描述
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
