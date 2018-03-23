package com.lswuyou.tv.pm.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.adapter.CommonGridItem;
import com.lswuyou.tv.pm.adapter.TvCommonGridAdapter;
import com.lswuyou.tv.pm.adapter.TvKeyboardGridAdapter;
import com.lswuyou.tv.pm.adapter.TvRecommandCoursesListAdapter;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.response.course.GetRecommandCoursesResponse;
import com.lswuyou.tv.pm.net.response.course.GetSearchedCoursesResponse;
import com.lswuyou.tv.pm.net.response.course.RecommandCourseVo;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse.DataBean.BookListBean.ItemListBean;
import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;
import com.lswuyou.tv.pm.net.service.GetRecommandService;
import com.lswuyou.tv.pm.net.service.GetSearchedLoginedService;
import com.lswuyou.tv.pm.net.service.GetSearchedUnLoginedService;
import com.lswuyou.tv.pm.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import reco.frame.tv.view.TvButton;
import reco.frame.tv.view.TvGridView;
import reco.frame.tv.view.TvListView;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private TvGridView tgvKeyboard;
    private TvKeyboardGridAdapter adapter;
    private TvButton btnDelete, btnClear, btnSearch;
    private TextView tvSearchContent;
    private TvListView tlvRecommand;
    private TvGridView tgvSearchCourse;
    private List<RecommandCourseVo> recommandCourses;
    String[] keyList = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4",
            "5", "6", "7", "8", "9", "0"};
    private List<ItemListBean> allListVos;

    @Override
    protected void findViewById() {
        tgvKeyboard = (TvGridView) findViewById(R.id.tgv_keyboard);
        btnDelete = (TvButton) findViewById(R.id.btn_delete);
        btnClear = (TvButton) findViewById(R.id.btn_clear);
        btnSearch = (TvButton) findViewById(R.id.btn_search);
        tvSearchContent = (TextView) findViewById(R.id.tv_search);
        tlvRecommand = (TvListView) findViewById(R.id.tlv_recommand);
        tgvSearchCourse = (TvGridView) findViewById(R.id.tgv_search_courses);
        btnClear.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        getRecommandContent();
        load();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_search;
    }

    private void load() {
        adapter = new TvKeyboardGridAdapter(getApplicationContext(), keyList);
        tgvKeyboard.setAdapter(adapter);

        tgvKeyboard.setOnItemSelectListener(new TvGridView.OnItemSelectListener() {
            @Override
            public void onItemSelect(View item, int position) {
            }
        });

        tgvKeyboard.setOnItemClickListener(new TvGridView.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                String content = tvSearchContent.getText().toString();
                tvSearchContent.setText(content + keyList[position]);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                String keyword = tvSearchContent.getText().toString();
                if (TextUtils.isEmpty(keyword)) {
                    Toast.makeText(SearchActivity.this, "请输入拼音首字母！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getSearchContent(keyword);
                break;
            case R.id.btn_clear:
                tvSearchContent.setText("");
                break;
            case R.id.btn_delete:
                String content = tvSearchContent.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                String newContent = content.substring(0, content.length() - 1);
                tvSearchContent.setText(newContent);
                break;
        }
    }

    /**
     * 从服务端获取搜索内容
     */
    private void getSearchContent(String keyword) {
        OpenApiDataServiceBase service;
        if (Utils.isUserLogined()) {
            service = new GetSearchedLoginedService(this);
        } else {
            service = new GetSearchedUnLoginedService(this);
        }
        service.setCallback(new IOpenApiDataServiceCallback<GetSearchedCoursesResponse>() {
            @Override
            public void onGetData(GetSearchedCoursesResponse data) {
                try {
                    refreshSearchCoursesUI(data.data);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SearchActivity.this, "解析数据异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(SearchActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        if (Utils.isUserLogined()) {
            service.postAES("keyword=" + keyword, true);
        } else {
            service.post("keyword=" + keyword, true);
        }
    }

    /**
     * 从服务器获取推荐内容
     */
    private void getRecommandContent() {
        GetRecommandService service = new GetRecommandService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetRecommandCoursesResponse>() {
            @Override
            public void onGetData(GetRecommandCoursesResponse data) {
                try {
                    recommandCourses = data.data.mostPopulars;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshRecommandUI();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SearchActivity.this, "数据解析异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.post("", true);
    }

    /**
     * 刷新推荐课程列表
     */
    private void refreshRecommandUI() {
        TvRecommandCoursesListAdapter adapter = new TvRecommandCoursesListAdapter(this, recommandCourses);
        tlvRecommand.setAdapter(adapter);

        tlvRecommand.setOnItemClickListener(new TvListView.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                String pinYin = recommandCourses.get(position).titleFirstPinyin;
                getSearchContent(pinYin);
                tvSearchContent.setText(pinYin.toUpperCase());
            }
        });
    }

    /**
     * 刷新搜索课程列表
     */
    private void refreshSearchCoursesUI(GetSearchedCoursesResponse.DataBean data) {
        allListVos = new ArrayList<>();
        allListVos.addAll(data.chapterList);
        allListVos.addAll(data.videoList);
        TvCommonGridAdapter adapter = new TvCommonGridAdapter(this, allListVos);
        tgvSearchCourse.setAdapter(adapter);
        tgvSearchCourse.setOnItemClickListener(new TvGridView.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                ItemListBean itemBean = allListVos.get(position);
                if (itemBean.chapterId != 0) {
                    Intent intent = new Intent(SearchActivity.this, ChapterDetailActivity.class);
                    intent.putExtra("title", itemBean.title);
                    intent.putExtra("chapterId", itemBean.chapterId);
                    startActivity(intent);
                } else if (itemBean.videoId != 0) {
                    if (itemBean.tvVideoType == 1) {
                        Intent intent = new Intent(SearchActivity.this, VideoIntroActivity.class);
                        intent.putExtra("title", itemBean.title);
                        intent.putExtra("videoId", itemBean.videoId);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SearchActivity.this, VideoIntroForExcActivity
                                .class);
                        intent.putExtra("title", itemBean.title);
                        intent.putExtra("videoId", itemBean.videoId);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    /**
     * 查看课程包/课程/视频详情
     *
     * @param info
     */
    private void gotoContentDetail(CourseInfo info) {
        if (info.isCoursePack == 1) {
            //如果未登录，直接进入课程包未购买界面
            if (!Utils.isUserLogined()) {
                Intent intent = new Intent(this, CoursePackDetailActivity.class);
                intent.putExtra("courseId", info.chapterId);
                startActivity(intent);
                //如果已购买，进入课程包详情界面
            } else {
                if (info.alreadyBuy == 1) {
                    Intent intent = new Intent(this, CoursePackBoughtActivity.class);
                    intent.putExtra("courseId", info.chapterId);
                    intent.putExtra("title", info.title);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, CoursePackDetailActivity.class);
                    intent.putExtra("courseId", info.chapterId);
                    startActivity(intent);
                }
            }
        } else {
            Intent intent = new Intent(this, ChapterDetailActivity.class);
            intent.putExtra("courseId", info.chapterId);
            intent.putExtra("title", info.title);
            startActivity(intent);
        }
    }
}
