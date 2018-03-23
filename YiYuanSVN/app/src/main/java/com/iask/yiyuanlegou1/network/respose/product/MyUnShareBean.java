package com.iask.yiyuanlegou1.network.respose.product;

/**
 * Created by Administrator on 2016/6/20.
 */
public class MyUnShareBean {
    //商品id
    private Integer id;
    //缩略图
    private String thumb;
    //期数
    private Integer qishu;
    //标题
    private String  title;
    //中奖号码(幸运号码)
    private String  q_user_code;
    //同一种商品id
    private Integer sid;

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getQ_user_code() {
        return q_user_code;
    }

    public void setQ_user_code(String q_user_code) {
        this.q_user_code = q_user_code;
    }
}
