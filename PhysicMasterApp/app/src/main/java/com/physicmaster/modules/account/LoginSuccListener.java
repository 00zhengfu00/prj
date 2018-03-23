package com.physicmaster.modules.account;

/**
 * Created by huashigen on 2017/1/13.
 */

public interface LoginSuccListener {
    void onLoginSucc(String openId, int type, String token);
}
