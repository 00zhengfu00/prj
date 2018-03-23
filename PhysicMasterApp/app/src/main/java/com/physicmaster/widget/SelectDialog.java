package com.physicmaster.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.net.response.account.StartupResponse;

import java.util.ArrayList;
import java.util.List;


public class SelectDialog extends DialogFragment implements View.OnClickListener {

    private FragmentActivity mContext;
    private List<StartupResponse.DataBean.EduGradeYearListBean> eduGradeYearList;
    private List<StartupResponse.DataBean.SubjectTypeListBean> subjectTypeList;
    private int eduGradeId = 0;
    private String selectEdu = "";
    private String selectSubject = "";
    private OnBack listener;
    /**
     * 点击回调接口
     */
    private SubjectAdapter subjectAdapter;
    private GradeAdapter gradeAdapter;

    public void setListener(OnBack listener) {
        this.listener = listener;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        Dialog dialog = new Dialog(getActivity(), R.style.TopDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.select_dialog);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消
        dialog.getWindow().setWindowAnimations(R.style.AnimationPushOutIn);
        dialog.findViewById(R.id.btn_yes).setOnClickListener(this);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(this);
        GridView gvGrade = (GridView) dialog.findViewById(R.id.gv_grade);
        GridView gvSubject = (GridView) dialog.findViewById(R.id.gv_subject);


        Window window = dialog.getWindow();
        LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);
        eduGradeYearList = new ArrayList<>();
        subjectTypeList = new ArrayList<>();
        StartupResponse.DataBean startupDataBean = BaseApplication.getStartupDataBean();
        eduGradeYearList.addAll(startupDataBean.eduGradeYearList);
        subjectTypeList.addAll(startupDataBean.subjectTypeList);
        for (StartupResponse.DataBean.EduGradeYearListBean eduGradeYearListBean :
                eduGradeYearList) {
            eduGradeYearListBean.state = false;
        }
        for (StartupResponse.DataBean.SubjectTypeListBean subjectTypeListBean : subjectTypeList) {
            subjectTypeListBean.state = 0;
        }
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
                selectEdu = eduGradeYearList.get(position).n;
                for (int i = 0; i < subjectTypeList.size(); i++) {
                    if (isSelectEnable(position, subjectTypeList.get(i).i)) {
                        subjectTypeList.get(i).state = 1;
                    } else {
                        subjectTypeList.get(i).state = 0;
                    }
                }
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
                selectSubject = subjectTypeList.get(position).n;
                listener.click(eduGradeId, (int) id, "#" + selectEdu + selectSubject + "#");
                dismiss();
            }
        });
        return dialog;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 点击回调接口
     */
    public interface OnBack {
        void click(int eduId, int subjectId, String tag);
    }
}

