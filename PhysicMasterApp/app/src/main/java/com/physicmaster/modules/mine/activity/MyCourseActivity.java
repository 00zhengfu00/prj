package com.physicmaster.modules.mine.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.MainActivity;
import com.physicmaster.base.SplashActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.modules.mine.activity.school.Item;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.excercise.GetMyCourseResponse;
import com.physicmaster.net.service.excercise.GetMyCourseService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.MyPopupWindow;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;


public class MyCourseActivity extends BaseActivity {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_TITLE = 1;
    private static final String TAG = "MyCourseActivity";

    private GridView gvCourse;
    private RelativeLayout rlEmpty;
    private int subjectId;
    private int actionId;
    private int flag;
    private List<GetMyCourseResponse.DataBean.MyCourseListBean> myCourseList;
    private CourseItemAdapter courseAdapter;

    @Override
    protected void findViewById() {
        gvCourse = (GridView) findViewById(R.id.gv_course);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);
        initTitle();

    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("我的课程");
    }

    @Override
    protected void initView() {
        myCourseList = new ArrayList<>();
        courseAdapter = new CourseItemAdapter(myCourseList);
        gvCourse.setAdapter(courseAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag != 0) {
            if (1 == flag) {
                actionId = 0;
            } else if (2 == flag) {
                actionId = 1;
            } else if (4 == flag) {
                actionId = 2;
            }
            updateCourse(flag);
        } else {
            String subjectInfo = SpUtils.getString(MyCourseActivity.this, CacheKeys.SUBJECT_STUDY_INFO, "");
            if (TextUtils.isEmpty(subjectInfo)) {
                String packageName = mContext.getPackageName();
                if (Constant.PHYSICMASTER.equals(packageName)) {
                    subjectId = 1;
                } else if (Constant.CHYMISTMASTER.equals(packageName)) {
                    subjectId = 2;
                } else if (Constant.MATHMASTER.equals(packageName)) {
                    subjectId = 4;
                }
            } else {
                subjectId = Integer.parseInt(subjectInfo);
            }
            if (1 == subjectId) {
                actionId = 0;
            } else if (2 == subjectId) {
                actionId = 1;
            } else if (4 == subjectId) {
                actionId = 2;
            }
            updateCourse(subjectId);
        }

    }

    private void updateCourse(final int subjectId) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final GetMyCourseService service = new GetMyCourseService(MyCourseActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GetMyCourseResponse>() {
            @Override
            public void onGetData(GetMyCourseResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                loadingDialog.dismissDialog();
                if (1 == subjectId) {
                    actionId = 0;
                } else if (2 == subjectId) {
                    actionId = 1;
                } else if (4 == subjectId) {
                    actionId = 2;
                }
                flag = subjectId;
                myCourseList.clear();
                myCourseList.addAll(data.data.myCourseList);
                if ((null == myCourseList || myCourseList.size() == 0)) {
                    rlEmpty.setVisibility(View.VISIBLE);
                    gvCourse.setVisibility(View.GONE);
                } else {
                    rlEmpty.setVisibility(View.GONE);
                    gvCourse.setVisibility(View.VISIBLE);
                    courseAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                Toast.makeText(MyCourseActivity.this, errorMsg, Toast.LENGTH_SHORT)
                        .show();
                rlEmpty.setVisibility(View.VISIBLE);
                gvCourse.setVisibility(View.GONE);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("subjectId=" + subjectId, false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_course;
    }

    //    class CourseAdapter extends BaseAdapter {
    //
    //        @Override
    //        public int getCount() {
    //            return 4;  //两个标题栏和两个gridview
    //        }
    //
    //        @Override
    //        public String getItem(int position) {
    //            return null;
    //
    //        }
    //
    //        @Override
    //        public long getItemId(int position) {
    //            return position;
    //        }
    //
    //        @Override
    //        public int getViewTypeCount() {
    //            return 2;
    //        }
    //
    //        @Override
    //        public int getItemViewType(int position) {
    //            if (position == 0 || position == 2) {
    //                //标题栏
    //                return TYPE_TITLE;
    //            } else {
    //                return TYPE_NORMAL;
    //
    //            }
    //        }
    //
    //        @Override
    //        public View getView(int position, View convertView, ViewGroup parent) {
    //            //根据当前布局类型,来加载不同布局
    //            int type = getItemViewType(position);
    //
    //            switch (type) {
    //                case TYPE_NORMAL:
    //                    ViewHolder holder;
    //                        if (convertView == null) {
    //                            convertView = View.inflate(MyCourseActivity.this,
    //                                    R.layout.item_course, null);
    //
    //                        holder = new ViewHolder();
    //                        holder.gvCourse = (MoreGridView) convertView
    //                                .findViewById(R.id.gv_course);
    //                        convertView.setTag(holder);
    //                    } else {
    //                        holder = (ViewHolder) convertView.getTag();
    //                    }
    //                    if (position == 1) {
    //                        holder.gvCourse.setAdapter(new CourseItemAdapter
    //                                (appUserZjCourseListVoList));
    //                    } else {
    //                        holder.gvCourse.setAdapter(new CourseItemAdapter
    //                                (appUserZtCourseListVoList));
    //                    }
    //                    break;
    //
    //                case TYPE_TITLE:
    //                    //标题栏布局
    //                    HeaderViewHolder headerHolder;
    //                    if (convertView == null) {
    //                        convertView = View.inflate(MyCourseActivity.this,
    //                                R.layout.grid_item_title, null);
    //                        headerHolder = new HeaderViewHolder();
    //                        headerHolder.tvTitle = (TextView) convertView
    //                                .findViewById(R.id.tv_title);
    //
    //                        convertView.setTag(headerHolder);
    //                    } else {
    //                        headerHolder = (HeaderViewHolder) convertView.getTag();
    //                    }
    //                    if (appUserZtCourseListVoList.size() == 0) {
    //
    //                        if (position == 0) {
    //                            headerHolder.tvTitle.setText("章节课程");
    //                        } else {
    //                            headerHolder.tvTitle.setVisibility(View.GONE);
    //                        }
    //                    } else {
    //                        if (appUserZjCourseListVoList.size() == 0) {
    //                            if (position == 0) {
    //                                headerHolder.tvTitle.setVisibility(View.GONE);
    //                            } else {
    //                                headerHolder.tvTitle.setText("专题课程");
    //                            }
    //                        } else {
    //                            if (position == 0) {
    //                                headerHolder.tvTitle.setText("章节课程");
    //                            } else {
    //                                headerHolder.tvTitle.setText("专题课程");
    //                            }
    //                        }
    //
    //
    //                    }
    //
    //                    break;
    //            }
    //            return convertView;
    //        }
    //    }
    //
    //    static class ViewHolder {
    //        public MoreGridView gvCourse;
    //    }
    //
    //    static class HeaderViewHolder {
    //        public TextView tvTitle;
    //    }


    class CourseItemAdapter extends BaseAdapter {

        private List<GetMyCourseResponse.DataBean.MyCourseListBean> mlist;

        public CourseItemAdapter(List<GetMyCourseResponse.DataBean.MyCourseListBean> list) {
            mlist = list;
        }


        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public GetMyCourseResponse.DataBean.MyCourseListBean getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ItemViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(MyCourseActivity.this,
                        R.layout.grid_item_course, null);
                holder = new ItemViewHolder();

                holder.tvCourse = (TextView) convertView
                        .findViewById(R.id.tv_course);
                holder.ivCourse = (ImageView) convertView
                        .findViewById(R.id.iv_course);


                convertView.setTag(holder);
            } else {
                holder = (ItemViewHolder) convertView.getTag();
            }
            final GetMyCourseResponse.DataBean.MyCourseListBean
                    item = getItem(position);

            holder.tvCourse.setText(item.title + "");
            if (!TextUtils.isEmpty(item.posterUrl)) {
                Glide.with(MyCourseActivity.this).load(item.posterUrl).into(holder.ivCourse);
            }

            holder.ivCourse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyCourseActivity.this, MainActivity.class);
                    intent.putExtra("chapterId", item.chapterId + "");
                    intent.putExtra("chapterName", item.title);
                    startActivity(intent);
                }
            });

            return convertView;
        }


    }

    static class ItemViewHolder {
        TextView tvCourse;
        ImageView ivCourse;
    }

}
