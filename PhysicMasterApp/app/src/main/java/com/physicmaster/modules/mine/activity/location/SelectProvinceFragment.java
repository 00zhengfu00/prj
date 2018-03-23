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
package com.physicmaster.modules.mine.activity.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.SplashActivity;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.mine.activity.school.AreaAdapter;
import com.physicmaster.modules.mine.activity.school.Item;
import com.physicmaster.modules.mine.activity.school.ProvinceBean;
import com.physicmaster.modules.mine.activity.school.ProvincesBean;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.TitleBuilder;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;


public class SelectProvinceFragment extends Fragment {
    private View rootView;
    /**
     * 数据为空时显示的View
     */
    private TextView emptyView;
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
    private SelectLocationActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_select_area, container, false);
            initView();
        } else {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initView() {
        mContext = (SelectLocationActivity) getActivity();

        tvShowPro = (TextView) rootView.findViewById(R.id.tv_select_area);
        lvProvince = (ListView) rootView.findViewById(R.id.lv_area);
        emptyView = (TextView) rootView.findViewById(R.id.empty);
        lvProvince.setEmptyView(emptyView);
        initTitle();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(mContext).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mContext.finish();
                    }
                })
                .setMiddleTitleText("选择省份");

    }

    private void initData() {
        ProvincesBean provinces = (ProvincesBean) CacheManager.getObject(CacheManager
                .TYPE_USER_INFO, CacheKeys.SCHOOL_LOCATION, ProvincesBean.class);
        /** 区域数据URL */
        String lastJsonUrl = SpUtils.getString(getContext(), CacheKeys.APPDATA_LAST_REGIONURL, "");
        if (BaseApplication.getStartupDataBean() == null) {
            startActivity(new Intent(getActivity(), SplashActivity.class));
            getActivity().finish();
            UIUtils.showToast(getActivity(), "数据异常");
            return;
        }
        final String currJsonUrl = BaseApplication.getStartupDataBean().regionUrl;
        if (!currJsonUrl.equalsIgnoreCase(lastJsonUrl) || null == provinces) {
            final AsyncHttpClient client = new AsyncHttpClient();
            if (TextUtils.isEmpty(currJsonUrl)) {
                UIUtils.showToast(getContext(), "获取数据失败，请稍后再试");
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
                    if (TextUtils.isEmpty(data)) {
                        emptyView.setText("获取数据失败！");
                        return;
                    }
                    ProvincesBean provinces = JSON.parseObject(data, ProvincesBean.class);
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.SCHOOL_LOCATION, provinces);
                    provincesList = provinces.provinces;
                    initListView();
                    SpUtils.putString(getContext(), CacheKeys.APPDATA_LAST_REGIONURL, currJsonUrl);
                }

            });
        } else {
            provincesList = provinces.provinces;
            initListView();
        }
        lvProvince.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvShowPro.setText(provincesList.get(position).fn);
                Fragment fragment = new SelectCityFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArray("cityList", provincesList.get(position).sub);
                bundle.putString("province", provincesList.get(position).fn);
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().addToBackStack("city").replace(R.id
                        .location_frame_lt, fragment).commit();
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
            list.add(new Item(provincesList.get(i).sn));
        }
        lvProvince.setAdapter(adapter);
    }
}
