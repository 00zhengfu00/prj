package com.physicmaster.net.response.im;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2017/6/1.
 */

public class GetRecommendFriendsResponse extends Response {
    public DataBean data;

    public static class DataBean {
        public List<FindFriendV2Response.DataBean.UserVoListBean> userVoList;
    }
}
