package com.physicmaster.net.response.user;

import com.physicmaster.net.response.Response;

import java.util.List;

/**
 * Created by huashigen on 2016/11/21.
 */
public class ShowPetResponse extends Response{

    /**
     * data : {"showPetList":[{"petDesImg":"http://imgtest.thelper.cn/pm/tmp/caoxjl.png","petId":205},{"petDesImg":"http://imgtest.thelper.cn/pm/tmp/dxjl.png","petId":204},{"petDesImg":"http://imgtest.thelper.cn/pm/tmp/hxjl.png","petId":203},{"petDesImg":"http://imgtest.thelper.cn/pm/tmp/sxjl.png","petId":202},{"petDesImg":"http://imgtest.thelper.cn/pm/tmp/cxjl.png","petId":201}]}
     */

    public DataBean data;


    public static class DataBean {
        public List<ShowPetListBean> showPetList;


        public static class ShowPetListBean {
            /**
             * petDesImg : http://imgtest.thelper.cn/pm/tmp/caoxjl.png
             * petId : 205
             */

            public String petDesImg;
            public int petId;

        }
    }
}
