package com.iask.yiyuanlegou1.network.respose.account;

public class OSSTokenInfo {

    public long expiration;
    public String securityToken;
    public String tempAk;
    public String tempSk;

    public OSSTokenInfo() {
        // TODO Auto-generated constructor stub
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getsecurityToken() {
        return securityToken;
    }

    public void setsecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String gettempAk() {
        return tempAk;
    }

    public void settempAk(String tempAk) {
        this.tempAk = tempAk;
    }

    public String gettempSk() {
        return tempSk;
    }

    public void settempSk(String tempSk) {
        this.tempSk = tempSk;
    }
}
