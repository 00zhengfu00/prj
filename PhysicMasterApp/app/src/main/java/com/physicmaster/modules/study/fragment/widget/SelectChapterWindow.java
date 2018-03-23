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
package com.physicmaster.modules.study.fragment.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.modules.guide.ExposureView;
import com.physicmaster.modules.mine.activity.user.UserActivity;
import com.physicmaster.net.response.excercise.GetChapterListResponse;

import java.util.List;

public class SelectChapterWindow extends PopupWindow {
    private Context mContext;
    private OnItemSelectListener listener;
    private OnDismissListener onDismissListener;
    private ListView lvSubjects;
    private List<GetChapterListResponse.DataBean.ChapterListBean> list;
    private TextView tvSwitch;
    private RelativeLayout rlParent;
    private View rootView;

    public SelectChapterWindow(Context mContext, OnItemSelectListener listener, OnDismissListener
            onDismissListener, List<GetChapterListResponse.DataBean.ChapterListBean> list, String gradeAndSubject, int height) {
        this.mContext = mContext;
        this.listener = listener;
        this.onDismissListener = onDismissListener;
        this.list = list;
        initPopupWindow(gradeAndSubject, height);
    }

    private void initPopupWindow(String gradeAndsubject, int height) {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.layout_select_chapter_window, null);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(rootView);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(height);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(false);

        setAnimationStyle(android.R.style.Animation_Toast);
        setTouchable(true);
        setFocusable(true);
        update();
        lvSubjects = (ListView) rootView.findViewById(R.id.lv_chapter);
        SubjectAdapter adapter = new SubjectAdapter(mContext);
        lvSubjects.setAdapter(adapter);
        lvSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onSubmit(list.get(position).chapterId + "", list.get(position).name);
                }
                dismiss();
            }
        });

        TextView tvGradeAndSubject = (TextView) rootView.findViewById(R.id.tv_grade_subject);
        tvGradeAndSubject.setText(gradeAndsubject);
        tvSwitch = (TextView) rootView.findViewById(R.id.tv_switch);
        tvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(mContext, UserActivity.class);
                mContext.startActivity(intent);
            }
        });
        rlParent = (RelativeLayout) rootView.findViewById(R.id.rl_parent);
    }

    /**
     * 仅用在引导页面
     *
     * @return
     */
    public ExposureView getSwitchViewPostion() {
        int parentTop = rlParent.getTop();
        int parentBottom = rlParent.getBottom();
        ExposureView exposureView = new ExposureView(tvSwitch.getLeft(), tvSwitch.getTop() + parentTop, tvSwitch.getRight(), tvSwitch.getBottom() + parentBottom);
        return exposureView;
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void showPopupWindow(View view) {
        if (!isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            showAsDropDown(view, view.getWidth() - getWidth(), 0);
//            showAtLocation(view, Gravity.CENTER_HORIZONTAL, location[0], location[1]);
        }
    }

    public interface OnItemSelectListener {
        void onSubmit(String chapterId, String name);
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    class SubjectAdapter extends BaseAdapter {
        private Context mContext;

        public SubjectAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.chapter_item, parent, false);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_chapter);
                holder.tvPercent = (TextView) convertView.findViewById(R.id.tv_percent);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(list.get(position).name);
            holder.tvPercent.setText(list.get(position).progress + "");
            return convertView;
        }

        class ViewHolder {
            TextView tvName;
            TextView tvPercent;
        }
    }
}