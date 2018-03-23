/*
 * 系统名称：lswuyou
 * 类  名  称：MyPopupMenu.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2016-4-21 下午3:28:20
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.net.response.account.StartupResponse;

import java.util.List;

public class SelectPopupWindow extends PopupWindow {
    private Context           mContext;
    private OnSubmitListener  listener;
    private OnDismissListener onDismissListener;

    private int eduGradeId = 0;

    private List<StartupResponse.DataBean.EduGradeYearListBean> eduGradeYearList;
    private List<StartupResponse.DataBean.SubjectTypeListBean>  subjectTypeList;
    private GradeAdapter                                        gradeAdapter;
    private SubjectAdapter                                      subjectAdapter;

    public SelectPopupWindow(Context mContext, OnSubmitListener listener, OnDismissListener
            onDismissListener, List<StartupResponse.DataBean.EduGradeYearListBean>
                                     eduGradeYearList, List<StartupResponse.DataBean
            .SubjectTypeListBean> subjectTypeList, int subjectId, int eduGradeId) {
        this.mContext = mContext;
        this.listener = listener;
        this.onDismissListener = onDismissListener;

        this.eduGradeYearList = eduGradeYearList;
        this.subjectTypeList = subjectTypeList;


        if (eduGradeId == -2) {
            //年级选中状态全部重置
            for (StartupResponse.DataBean.EduGradeYearListBean eduGradeYearListBean :
                    eduGradeYearList) {
                eduGradeYearListBean.state = false;
            }
            eduGradeYearList.get(0).state = true;
            if (subjectId == -2) {
                //科目选择状态全部重置
                for (StartupResponse.DataBean.SubjectTypeListBean subjectTypeListBean : subjectTypeList) {
                    subjectTypeListBean.state = 1;
                }
                subjectTypeList.get(0).state = 2;
            } else {
                subjectTypeList.get(0).state = 1;
                for (int i = 0; i < subjectTypeList.size(); i++) {
                    if (subjectId == subjectTypeList.get(i).i) {
                        subjectTypeList.get(i).state = 2;
                    }
                }
            }
        } else {
            eduGradeYearList.get(0).state = false;
            for (int i = 0; i < eduGradeYearList.size(); i++) {
                if (eduGradeId == eduGradeYearList.get(i).i) {
                    eduGradeYearList.get(i).state = true;
                }
            }
            if (subjectId == -2) {
                //科目选择状态全部重置
                for (StartupResponse.DataBean.SubjectTypeListBean subjectTypeListBean : subjectTypeList) {
                    subjectTypeListBean.state = 1;
                }
                subjectTypeList.get(0).state = 2;
            } else {
                subjectTypeList.get(0).state = 1;
                for (int i = 0; i < subjectTypeList.size(); i++) {
                    if (subjectId == subjectTypeList.get(i).i) {
                        subjectTypeList.get(i).state = 2;
                    }
                }
            }
        }
        initPopupWindow();
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.select_popup_window, null);
        setContentView(view);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);

        setAnimationStyle(android.R.style.Animation_Dialog);
        setTouchable(true);
        setFocusable(true);
        update();

        GridView gvGrade = (GridView) view.findViewById(R.id.gv_grade);
        GridView gvSubject = (GridView) view.findViewById(R.id.gv_subject);


        gradeAdapter = new GradeAdapter();
        gvGrade.setAdapter(gradeAdapter);

        subjectAdapter = new SubjectAdapter();
        gvSubject.setAdapter(subjectAdapter);

        gvGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (StartupResponse.DataBean.EduGradeYearListBean eduGradeYearListBean :
                        eduGradeYearList) {
                    eduGradeYearListBean.state = false;
                }
                eduGradeYearList.get(position).state = true;
                for (int i = 0; i < subjectTypeList.size(); i++) {
                    if (isSelectEnable(position, subjectTypeList.get(i).i)) {
                        subjectTypeList.get(i).state = 1;
                    } else {
                        subjectTypeList.get(i).state = 0;
                    }
                }
                subjectTypeList.get(0).state = 1;
                gradeAdapter.notifyDataSetChanged();
                subjectAdapter.notifyDataSetChanged();
            }
        });
        gvSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (0 == subjectTypeList.get(position).state) {
                    return;
                }
                for (StartupResponse.DataBean.SubjectTypeListBean subjectTypeListBean :
                        subjectTypeList) {
                    if (2 == subjectTypeListBean.state) {
                        subjectTypeListBean.state = 1;
                    }
                }
                subjectTypeList.get(position).state = 2;
                subjectAdapter.notifyDataSetChanged();
                listener.onSubmit(eduGradeId, (int) id);
                dismiss();
            }
        });
    }

    private boolean isSelectEnable(int position, int subjectType) {
        List<Integer> subjectList = eduGradeYearList.get(position).s;
        for (Integer integer : subjectList) {
            if (subjectType == integer.intValue()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        onDismissListener.onDismiss();
    }

    public void showPopupWindow(View view) {
        if (!isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int width = getWidth();
            int height = getHeight();
            int viewHeight = view.getHeight();
            showAsDropDown(view, 0, viewHeight / 2);
        }
    }

    public interface OnSubmitListener {
        void onSubmit(int eduGradeId, int subjectId);
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context
                .getResources().getDisplayMetrics());
    }

    class GradeAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return eduGradeYearList.size();
        }

        @Override
        public StartupResponse.DataBean.EduGradeYearListBean getItem(int position) {
            return eduGradeYearList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return eduGradeYearList.get(position).i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.grid_item_grade_subject, null);
                holder = new ViewHolder();
                holder.tvGrade = (TextView) convertView.findViewById(R.id.tv_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            StartupResponse.DataBean.EduGradeYearListBean item = getItem(position);
            holder.tvGrade.setText(item.n + "");
            if (item.state) {
                holder.tvGrade.setSelected(true);
                eduGradeId = item.i;
            } else {
                holder.tvGrade.setSelected(false);
            }
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvGrade;
    }


    class SubjectAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return subjectTypeList.size();
        }

        @Override
        public StartupResponse.DataBean.SubjectTypeListBean getItem(int position) {
            return subjectTypeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return subjectTypeList.get(position).i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.grid_item_grade_subject, null);
                holder = new Holder();
                holder.tvsubject = (TextView) convertView.findViewById(R.id.tv_text);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            StartupResponse.DataBean.SubjectTypeListBean item = getItem(position);
            holder.tvsubject.setText(item.n + "");
            if (0 == item.state) {
                holder.tvsubject.setSelected(false);
                holder.tvsubject.setEnabled(false);
            } else if (1 == item.state) {
                holder.tvsubject.setEnabled(true);
                holder.tvsubject.setSelected(false);
            } else if (2 == item.state) {
                holder.tvsubject.setEnabled(true);
                holder.tvsubject.setSelected(true);
            }

            return convertView;
        }

    }

    static class Holder {
        TextView tvsubject;
    }
}