package com.physicmaster.net.response.user;

import android.os.Parcel;
import android.os.Parcelable;

import com.physicmaster.net.response.Response;
import com.physicmaster.net.response.explore.GetExploreResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2016/11/26.
 */
public class MemebersResponse extends Response {


    /**
     * data : {"memberList":[{"isMember":0,"items":[{"isRecommend":1,"memberItemId":112,"monthPrice":"59.66","title":"358元/6个月","validDays":180},{"isRecommend":0,"memberItemId":111,"title":"69元/1个月","validDays":30},{"isRecommend":0,"memberItemId":113,"monthPrice":"58.16","title":"698元/12个月","validDays":365}],"poster":"http://img.thelper.cn/app/member/1d.jpg","subjectId":1,"title":"初中物理会员","expiryDate":"2024-06-24"},{"isMember":0,"items":[{"isRecommend":1,"memberItemId":122,"monthPrice":"49.66","title":"298元/6个月","validDays":180},{"isRecommend":0,"memberItemId":121,"title":"59元/1个月","validDays":30},{"isRecommend":0,"memberItemId":123,"monthPrice":"49.00","title":"588元/12个月","validDays":365}],"poster":"http://img.thelper.cn/app/member/2d.jpg","subjectId":2,"title":"初中化学会员"},{"expiryDate":"2024-06-24","isMember":1,"items":[{"isRecommend":1,"memberItemId":142,"monthPrice":"0.00","title":"0元/6个月","validDays":180},{"isRecommend":0,"memberItemId":141,"title":"0元/1个月","validDays":30},{"isRecommend":0,"memberItemId":143,"monthPrice":"0.00","title":"0元/12个月","validDays":365}],"poster":"http://img.thelper.cn/app/member/4e.jpg","subjectId":4,"title":"初中数学会员"}],"memberBanner":{"memberItemId":51,"monthPrice":"139.83","poster":"http://img.thelper.cn/app/member/item51.jpg","title":"1678元/12个月"}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * memberList : [{"isMember":0,"items":[{"isRecommend":1,"memberItemId":112,"monthPrice":"59.66","title":"358元/6个月","validDays":180},{"isRecommend":0,"memberItemId":111,"title":"69元/1个月","validDays":30},{"isRecommend":0,"memberItemId":113,"monthPrice":"58.16","title":"698元/12个月","validDays":365}],"poster":"http://img.thelper.cn/app/member/1d.jpg","subjectId":1,"title":"初中物理会员"},{"isMember":0,"items":[{"isRecommend":1,"memberItemId":122,"monthPrice":"49.66","title":"298元/6个月","validDays":180},{"isRecommend":0,"memberItemId":121,"title":"59元/1个月","validDays":30},{"isRecommend":0,"memberItemId":123,"monthPrice":"49.00","title":"588元/12个月","validDays":365}],"poster":"http://img.thelper.cn/app/member/2d.jpg","subjectId":2,"title":"初中化学会员"},{"expiryDate":"2024-06-24","isMember":1,"items":[{"isRecommend":1,"memberItemId":142,"monthPrice":"0.00","title":"0元/6个月","validDays":180},{"isRecommend":0,"memberItemId":141,"title":"0元/1个月","validDays":30},{"isRecommend":0,"memberItemId":143,"monthPrice":"0.00","title":"0元/12个月","validDays":365}],"poster":"http://img.thelper.cn/app/member/4e.jpg","subjectId":4,"title":"初中数学会员"}]
         * memberBanner : {"memberItemId":51,"monthPrice":"139.83","poster":"http://img.thelper.cn/app/member/item51.jpg","title":"1678元/12个月"}
         */

        public GetExploreResponse.DataBean.SuperMemberBean superMember;
        public List<MemberListBean> memberList;
    }
}
