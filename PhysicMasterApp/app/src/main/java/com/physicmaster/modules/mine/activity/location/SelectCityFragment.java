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
package com.physicmaster.modules.mine.activity.location;

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
import com.physicmaster.modules.mine.activity.school.AreaAdapter;
import com.physicmaster.modules.mine.activity.school.AreaBean;
import com.physicmaster.modules.mine.activity.school.CityBean;
import com.physicmaster.modules.mine.activity.school.Item;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;


public class SelectCityFragment extends Fragment {
	/** 根view */
	private View         rootView;
	/** 展示城市数据 */
	private ListView     lvCities;
	/** 数据为空时显示的View */
	private TextView     emptyView;
	/** 显示选择的省、市 */
	private TextView     tvShowLocation;

	/** 城市名称列表 */
	private List<Item> list;

	/** 城市数据列表 */
	private CityBean[] cities;

	/** 省份名称 */
	private String province;
	private SelectLocationActivity mContext;

	//	private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

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
		province = bundle.getString("province");
		tvShowLocation.setText(province);
		cities = (CityBean[]) bundle.getParcelableArray("cityList");
		if (cities != null && cities.length > 0) {
			list = new ArrayList<Item>();
			for (int i = 0; i < cities.length; i++) {
				list.add(new Item(cities[i].sn));
			}
			AreaAdapter adapter = new AreaAdapter(getActivity(), list);
			lvCities.setAdapter(adapter);
		}
	}

	private void initView() {

		mContext = (SelectLocationActivity) getActivity();
		tvShowLocation = (TextView) rootView.findViewById(R.id.tv_select_area);
		lvCities = (ListView) rootView.findViewById(R.id.lv_area);
		emptyView = (TextView) rootView.findViewById(R.id.empty);
		initTitle();
		lvCities.setEmptyView(emptyView);
		lvCities.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String location = "";
				location = (province.equals(cities[position].fn)) ? province : (province + cities[position].fn);
				tvShowLocation.setText(location);
				AreaBean[] sub = cities[position].sub;
				if (sub != null && sub.length > 0) {
					Fragment fragment = new SelectAreaFragment();
					Bundle bundle = new Bundle();
					bundle.putString("proAndCity", location);
					bundle.putParcelableArray("areaList", sub);
					fragment.setArguments(bundle);
					getFragmentManager().beginTransaction().addToBackStack("area").replace(R.id.location_frame_lt, fragment).commit();
				}
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
				.setMiddleTitleText("选择城市");

	}
}
