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
package com.physicmaster.modules.mine.activity.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.mine.activity.school.AreaAdapter;
import com.physicmaster.modules.mine.activity.school.AreaBean;
import com.physicmaster.modules.mine.activity.school.Item;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;


public class SelectAreaFragment extends Fragment {
    /**
     * 根view
     */
    private View rootView;
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
    private SelectLocationActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        proAndCity = bundle.getString("proAndCity");
        tvShowLocation.setText(proAndCity);
        areas = (AreaBean[]) bundle.getParcelableArray("areaList");
        if (areas != null && areas.length > 0) {
            list = new ArrayList<Item>();
            for (int i = 0; i < areas.length; i++) {
                list.add(new Item(areas[i].sn));
            }
            AreaAdapter adapter = new AreaAdapter(getActivity(), list);
            lvAreas.setAdapter(adapter);
        }
    }

    private void initView() {
        mContext = (SelectLocationActivity) getActivity();
        tvShowLocation = (TextView) rootView.findViewById(R.id.tv_select_area);
        lvAreas = (ListView) rootView.findViewById(R.id.lv_area);
        emptyView = (TextView) rootView.findViewById(R.id.empty);
        initTitle();
        lvAreas.setEmptyView(emptyView);
        lvAreas.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvShowLocation.setText(proAndCity + areas[position].fn);
                CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_LOCATION, proAndCity + list.get(position).name);
                Intent intent = new Intent("com.physicmaster.SELECT_LOCATION");
                String areaName = areas[position].mn.replace(",", "");
                intent.putExtra("aresName", areaName);
                intent.putExtra("areaId", areas[position].aid);
                CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys.USER_AREAID, areas[position].aid);
                mContext.sendBroadcast(intent);
                mContext.finish();
                //				Fragment fragment = new SelectSchoolFragment();
                //				Bundle bundle = new Bundle();
                //				bundle.putString("aid", areas[position].aid);
                //				bundle.putString("area", proAndCity + areas[position].fn);
                //				fragment.setArguments(bundle);
                //				getFragmentManager().beginTransaction().addToBackStack("school").replace(R.id.location_frame_lt, fragment).commit();
            }
        });
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
                        mContext.onBackPressed();
                    }
                })
                .setMiddleTitleText("选择区县");

    }
}
