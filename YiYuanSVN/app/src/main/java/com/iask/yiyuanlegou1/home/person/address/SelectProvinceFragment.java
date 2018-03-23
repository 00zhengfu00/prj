/* 
 * 系统名称：lswuyou
 * 类  名  称：SelectProvinceFragment.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-17 上午10:24:57
 * 功能说明： 选择省份
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.home.person.address;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.bean.account.ProvinceBean;
import com.iask.yiyuanlegou1.network.bean.account.ProvincesBean;
import com.iask.yiyuanlegou1.widget.TitleBarView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class SelectProvinceFragment extends Fragment {
    private View rootView;
    /**
     * 数据为空时显示的View
     */
    private TextView emptyView;
    private TitleBarView mTitleBarView;
    /**
     * 展示省份数据
     */
    private ListView lvProvince;

    /**
     * 显示选择的省份
     */
    private TextView tvShowPro;

    /**
     * 省份数据
     */
    private List<Item> list;

    /** */
    private List<ProvinceBean> provincesList;

    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_select_area, container, false);
            initView();
            initData();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    private void initView() {
        tvShowPro = (TextView) rootView.findViewById(R.id.tv_select_area);
        lvProvince = (ListView) rootView.findViewById(R.id.lv_area);
        mTitleBarView = (TitleBarView) rootView.findViewById(R.id.title);
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE);
        mTitleBarView.setTitleTextStr("添加地址");
        mTitleBarView.setBtnLeft(R.mipmap.back, R.string.whitespace);
        mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getActivity().finish();
            }
        });

        emptyView = (TextView) rootView.findViewById(R.id.empty);
        lvProvince.setEmptyView(emptyView);
    }

    private void initData() {

        Object object = CacheManager.getObject(CacheManager.TYPE_PUBLIC,
                CacheKeys.SCHOOL_LOCATION, ProvincesBean.class);
        /** 区域数据URL */
        String lastJsonUrl = CacheManager.getString(CacheManager.TYPE_PUBLIC, CacheKeys
                .APPDATA_LAST_REGIONURL);
        final String currJsonUrl = CacheManager.getString(CacheManager.TYPE_PUBLIC, CacheKeys
                .APPDATA_CURR_REGIONURL);
        if (null == object || !currJsonUrl.equalsIgnoreCase(lastJsonUrl)) {
            final AsyncHttpClient client = new AsyncHttpClient();
            if (currJsonUrl == null) {
                emptyView.setText("获取数据失败！");
                return;
            }
            client.get(currJsonUrl, new TextHttpResponseHandler() {

                @Override
                public void onFailure(int arg0, Header[] arg1, String msg, Throwable arg3) {
                    if (msg != null) {
                        logger.error(msg);
                    }
                    emptyView.setText("获取数据失败！");
                }

                @Override
                public void onSuccess(int arg0, Header[] arg1, String data) {
                    logger.debug(data);
                    provincesList = JSON.parseArray(data, ProvinceBean.class);
                    ProvincesBean bean = new ProvincesBean();
                    bean.provinces = provincesList;
                    CacheManager.saveObject(CacheManager.TYPE_PUBLIC, CacheKeys.SCHOOL_LOCATION,
                            bean);
                    CacheManager.saveString(CacheManager.TYPE_PUBLIC, CacheKeys
                            .APPDATA_LAST_REGIONURL, currJsonUrl);
                    initListView();
                }

            });
        } else {
            ProvincesBean provinces = (ProvincesBean) object;
            provincesList = provinces.provinces;
            initListView();
        }
        lvProvince.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvShowPro.setText(provincesList.get(position).name);
                Fragment fragment = new SelectCityFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArray("cityList", provincesList.get(position).sub);
                bundle.putString("province", provincesList.get(position).name);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("city").replace(R.id
                        .person_school_frame_lt, fragment).commit();
            }
        });
    }

    /**
     * 初始化ListView
     */
    private void initListView() {
        list = new ArrayList<Item>();
        AreaAdapter adapter = new AreaAdapter(getActivity(), list);
        for (int i = 0; i < provincesList.size(); i++) {
            list.add(new Item(provincesList.get(i).name));
        }
        lvProvince.setAdapter(adapter);
    }
}
