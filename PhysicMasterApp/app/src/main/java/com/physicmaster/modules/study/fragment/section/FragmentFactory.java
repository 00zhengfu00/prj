package com.physicmaster.modules.study.fragment.section;

import com.physicmaster.base.BaseFragment;

import java.util.HashMap;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/18 13:31
 * 功能说明 : fragment工厂
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class FragmentFactory {

	private static HashMap<Integer, BaseFragment> sFragments = new HashMap<Integer, BaseFragment>();

	public static BaseFragment getFragment(int position) {

		BaseFragment fragment = sFragments.get(position);
		if (fragment == null) {
			switch (position) {
			case 0:
				fragment = new BeforehandFragment();
				break;
			case 1:
				fragment = new ReviewFragment();
				break;


			}
			sFragments.put(position, fragment);
		}
		
		return fragment;

	}

}
