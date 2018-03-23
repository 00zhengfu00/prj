/* 
 * 系统名称：lswuyou
 * 类  名  称：SelectCityFragment.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-17 上午10:25:27
 * 功能说明： 选择市
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.home.person.address;

import android.app.Activity;
import android.content.Intent;
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


import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.network.bean.account.AreaBean;
import com.iask.yiyuanlegou1.network.bean.account.CityBean;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

public class SelectCityFragment extends Fragment {
    /**
     * 根view
     */
    private View rootView;
    private TitleBarView mTitleBarView;
    /**
     * 展示城市数据
     */
    private ListView lvCities;
    /**
     * 数据为空时显示的View
     */
    private TextView emptyView;
    /**
     * 显示选择的省、市
     */
    private TextView tvShowLocation;

    /**
     * 城市名称列表
     */
    private List<Item> list;

    /**
     * 城市数据列表
     */
    private CityBean[] cities;

    /**
     * 省份名称
     */
    private String province;

//	private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    private void initData() {
        Bundle bundle = getArguments();
        province = bundle.getString("province");
        tvShowLocation.setText(province);
        cities = (CityBean[]) bundle.getParcelableArray("cityList");
        if (cities != null && cities.length > 0) {
            list = new ArrayList<Item>();
            for (int i = 0; i < cities.length; i++) {
                list.add(new Item(cities[i].name));
            }
            AreaAdapter adapter = new AreaAdapter(getActivity(), list);
            lvCities.setAdapter(adapter);
        }
    }

    private void initView() {
        tvShowLocation = (TextView) rootView.findViewById(R.id.tv_select_area);
        lvCities = (ListView) rootView.findViewById(R.id.lv_area);
        mTitleBarView = (TitleBarView) rootView.findViewById(R.id.title);
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE);
        mTitleBarView.setTitleTextStr("添加地址");
        mTitleBarView.setBtnLeft(R.mipmap.back, R.string.whitespace);
        mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                getActivity().onBackPressed();
            }
        });
        emptyView = (TextView) rootView.findViewById(R.id.empty);
        lvCities.setEmptyView(emptyView);
        lvCities.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location = "";
                location = (province.equals(cities[position].name)) ? province : (province +
                        cities[position].name);
                tvShowLocation.setText(location);
                AreaBean[] sub = cities[position].sub;
                if (sub != null && sub.length > 0) {
                    Fragment fragment = new SelectAreaFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("province", province);
                    bundle.putString("city", cities[position].name);
                    bundle.putParcelableArray("areaList", sub);
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().addToBackStack("area").replace(R.id
                            .person_school_frame_lt, fragment).commit();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("province", province);
                    intent.putExtra("city", cities[position].name);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
            }

        });
    }
}
