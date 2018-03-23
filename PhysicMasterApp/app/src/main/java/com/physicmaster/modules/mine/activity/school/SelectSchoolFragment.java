/* 
 * 系统名称：lswuyou
 * 类  名  称：SelectSchoolFragment.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-17 上午10:26:23
 * 功能说明： 选择学校
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.modules.mine.activity.school;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.user.FindSchoolService;
import com.physicmaster.net.service.user.UpdateSchoolInfoService;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;


public class SelectSchoolFragment extends Fragment implements SectionIndexer {
    /**
     * 根view
     */
    private View              rootView;
    private ListView          sortListView;
    private TextView          tvSelectArea;
    private SchoolListAdapter adapter;

    private LinearLayout titleLayout;
    private TextView     title;
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int              lastFirstVisibleItem = -1;
    private List<SchoolInfo> schoolList           = new ArrayList<SchoolInfo>();
    /**
     * 学校所在的地区
     */
    private String area;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    // private PinyinComparator pinyinComparator;
    /**
     * 区域ID
     */
    private String aid;

    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());
    private SelectSchoolActivity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_select_school, container, false);
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

    private void initList() {
        adapter = new SchoolListAdapter(getActivity(), schoolList);
        sortListView.setAdapter(adapter);

        // 根据a-z进行排序源数据
        sortListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount <= 0) {
                    return;
                }
                int section = getSectionForPosition(firstVisibleItem);
                int nextSection = getSectionForPosition(firstVisibleItem + 1);
                int nextSecPosition = getPositionForSection(+nextSection);
                if (firstVisibleItem != lastFirstVisibleItem) {
                    MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
                    params.topMargin = 0;
                    titleLayout.setLayoutParams(params);
                    title.setText(schoolList.get(getPositionForSection(section)).firstLetter);
                }
                if (nextSecPosition == firstVisibleItem + 1) {
                    View childView = view.getChildAt(0);
                    if (childView != null) {
                        int titleHeight = titleLayout.getHeight();
                        int bottom = childView.getBottom();
                        MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();
                        /** 实现下一个标题栏挤压上一个标题栏的效果 */
                        if (bottom < titleHeight) {
                            float pushedDistance = bottom - titleHeight;
                            params.topMargin = (int) pushedDistance;
                            titleLayout.setLayoutParams(params);
                        } else {
                            if (params.topMargin != 0) {
                                params.topMargin = 0;
                                titleLayout.setLayoutParams(params);
                            }
                        }
                    }
                }
                lastFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    private void initData() {
        Bundle bundle = getArguments();
        aid = bundle.getString("aid");
        area = bundle.getString("area");
        tvSelectArea.setText(area);
        FindSchoolService service = new FindSchoolService(getActivity());
        service.setCallback(new IOpenApiDataServiceCallback<SchoolResponse>() {
            @Override
            public void onGetData(SchoolResponse data) {
                if (data.data.schools != null) {
                    schoolList = data.data.schools;
                }
                initList();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                logger.error(errorMsg);
            }
        });
        service.postLogined("aid=" + aid, false);
    }

    private void initView() {

        mContext = (SelectSchoolActivity) getActivity();

        tvSelectArea = (TextView) rootView.findViewById(R.id.tv_select_area);
        titleLayout = (LinearLayout) rootView.findViewById(R.id.title_layout);
        title = (TextView) rootView.findViewById(R.id.title_layout_catalog);
        initTitle();
        sortListView = (ListView) rootView.findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                UpdateSchoolInfoService service = new UpdateSchoolInfoService(mContext);
                UpdateSchoolBean bean = new UpdateSchoolBean(aid, schoolList.get(position).name, schoolList.get(position).schoolId);
                service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {

                    @Override
                    public void onGetData(UserDataResponse data) {
                        logger.debug(data.data.loginVo.schoolName + "");
                        //Toast.makeText(mContext, data.data.loginVo.schoolName + "", Toast.LENGTH_SHORT).show();
                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO,data.data.loginVo);
                        BaseApplication.setUserData(data.data.loginVo);
                        Intent intent = new Intent("com.physicmaster.SELECT_SCHOOL");
                        intent.putExtra("schoolName",data.data.loginVo.schoolName );
                        mContext.sendBroadcast(intent);
                        mContext.finish();
                    }

                    @Override
                    public void onGetError(int errorCode, String errorMsg, Throwable error) {
                        logger.error(errorMsg);
                    }
                });
                service.postLogined(bean.toString(), false);
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
                .setMiddleTitleText("选择学校");

    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        if (position < schoolList.size()) {
            return schoolList.get(position).firstLetter.charAt(0);
        }
        return 0;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        int size = schoolList.size();
        for (int i = 0; i < size; i++) {
            String sortStr = schoolList.get(i).firstLetter;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
