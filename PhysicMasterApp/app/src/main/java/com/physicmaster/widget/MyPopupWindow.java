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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.modules.mine.activity.school.Item;

import java.util.List;

public class MyPopupWindow extends PopupWindow {
    private Context mContext;
    private OnSubmitListener listener;
    private OnDismissListener onDismissListener;
    private ListView lvSubjects;
    private List<Item> list;

    public MyPopupWindow(Context mContext, OnSubmitListener listener, OnDismissListener
            onDismissListener, List<Item> list) {
        this.mContext = mContext;
        this.listener = listener;
        this.onDismissListener = onDismissListener;
        this.list = list;
        initPopupWindow();
    }

    private void initPopupWindow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow, null);
        setContentView(view);
        setWidth(dp2px(mContext, 120));
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);

        setAnimationStyle(android.R.style.Animation_Dialog);
        setTouchable(true);
        setFocusable(true);
        update();
        lvSubjects = (ListView) view.findViewById(R.id.lv_subjects);
        SubjectAdapter adapter = new SubjectAdapter(mContext);
        lvSubjects.setAdapter(adapter);
        lvSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onSubmit(position);
                dismiss();
            }
        });
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
            showAsDropDown(view, view.getWidth() - getWidth(), 0);
        }
    }

    public interface OnSubmitListener {
        void onSubmit(int position);
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.subject_item, parent, false);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_subject);
                holder.tvName.setText(list.get(position).name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                holder.tvName.setText(list.get(position).name);
            }
            return convertView;
        }

        class ViewHolder {
            TextView tvName;
        }
    }
}