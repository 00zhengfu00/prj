/* 
 * 系统名称：lswuyou
 * 类  名  称：SelectAreaFragment.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-17 上午10:26:02
 * 功能说明： 选择地区
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
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

public class SelectAreaFragment extends Fragment {
    /**
     * 根view
     */
    private View rootView;
    private TitleBarView mTitleBarView;
    /**
     * 展示区域数据
     */
    private ListView lvAreas;
    /**
     * 数据为空时显示的View
     */
    private TextView emptyView;
    /**
     * 显示选择的省、市
     */
    private TextView tvShowLocation;

    /**
     * 城市区域列表
     */
    private List<Item> list;

    /**
     * 城市数据列表
     */
    private AreaBean[] areas;

    /**
     * 省份+城市名称
     */
    private String proAndCity;
    private String province, city;

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

    private void initData() {
        Bundle bundle = getArguments();
        province = bundle.getString("province");
        city = bundle.getString("city");
        proAndCity = province + city;
        tvShowLocation.setText(proAndCity);
        areas = (AreaBean[]) bundle.getParcelableArray("areaList");
        if (areas != null && areas.length > 0) {
            list = new ArrayList<Item>();
            for (int i = 0; i < areas.length; i++) {
                list.add(new Item(areas[i].name));
            }
            AreaAdapter adapter = new AreaAdapter(getActivity(), list);
            lvAreas.setAdapter(adapter);
        }
    }

    private void initView() {
        tvShowLocation = (TextView) rootView.findViewById(R.id.tv_select_area);
        lvAreas = (ListView) rootView.findViewById(R.id.lv_area);
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
        lvAreas.setEmptyView(emptyView);
        lvAreas.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvShowLocation.setText(proAndCity + areas[position].name);
//				Fragment fragment = new TextInputSchoolFragment();
//				Bundle bundle = new Bundle();
//				bundle.putString("aid", areas[position].aid);
//				bundle.putString("area", proAndCity + areas[position].name);
//				fragment.setArguments(bundle);
//				getFragmentManager().beginTransaction().addToBackStack("school").replace(R.id.person_school_frame_lt, fragment).commit();
                Intent intent = new Intent();
                intent.putExtra("province", province);
                intent.putExtra("city", city);
                intent.putExtra("area", areas[position].name);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
    }
}
