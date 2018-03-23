package com.physicmaster.net.response.game;


import com.physicmaster.net.response.Response;

import java.util.List;

public class EnergyResponse extends Response {


    /**
     * data : {"UserEnergyList":{"appReceiveVoList":[{"energyRequestId":16,"nickname":"老司机","portrait":"http://imgtest.thelper.cn/dt/u/9xcro/1481965347905"}],"appSendEnergVoList":[]}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * UserEnergyList : {"appReceiveVoList":[{"energyRequestId":16,"nickname":"老司机","portrait":"http://imgtest.thelper.cn/dt/u/9xcro/1481965347905"}],"appSendEnergVoList":[]}
         */

        public UserEnergyListBean UserEnergyList;

        public static class UserEnergyListBean {
            public List<AppReceiveVoListBean> appReceiveVoList;
            public List<AppReceiveVoListBean> appSendEnergVoList;

            public static class AppReceiveVoListBean {
                /**
                 * energyRequestId : 16
                 * nickname : 老司机
                 * portrait : http://imgtest.thelper.cn/dt/u/9xcro/1481965347905
                 */
                public int    dtUserId;
                public int    energyRequestId;
                public int    requestEnergyType;
                public String nickname;
                public String portrait;
                public String typeTitle;
            }
        }
    }
}
