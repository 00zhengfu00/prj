package com.physicmaster.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.physicmaster.R;
import com.physicmaster.modules.guide.ExposureView;
import com.physicmaster.modules.guide.GuideDialogFragment3;
import com.physicmaster.modules.guide.GuideDialogFragment4;
import com.physicmaster.modules.study.fragment.chapter.ChapterListFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.excercise.GetChapterListResponse;
import com.physicmaster.net.response.excercise.GetChapterListResponse.DataBean.ChapterListBean;
import com.physicmaster.net.service.excercise.GetChapterListService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2017-10-17.
 */

public class SelectChapterView implements View.OnClickListener {
    private Context context;
    public ViewGroup decorView;//显示本view的根view
    public ViewGroup rootView;//附加view的根view
    private boolean isShowing;
    protected View clickView;//是通过哪个View弹出的
    private boolean isAnim = true;
    private boolean dismissing;
    private OnDismissListener onDismissListener;
    private Animation outAnim;
    private Animation inAnim;
    private List<StartupResponse.DataBean.BookMenuBean> bookMenuList;
    private String selectSubject;
    private String versionAndGrade;
    private List<Integer> selectedBookIds;
    private List<String> selectedVersionGrades;
    private int selectIndex;

    private RecyclerView rclSubject;
    private ListView lvChapterList;
    private List<ChapterListBean> chapterList;
    private ChapterListFragment.OnItemSelectListener onItemSelectListener;
    private StartupResponse.DataBean.BookMenuBean bookMenuBean;
    private List<SubjectBean> subjectList;
    private int mBookId;
    private ArrayList<String> options1Items;
    private ArrayList<ArrayList<String>> options2Items;
    private String str1;
    private String str2;
    private TextView tvVersionAndGrade, tvSwitch;
    private OptionsPickerView pvOptions;
    private String versionGrade, subjectName;
    private LoadingView loadingView;
    private int index = -1;
    private SubjectAdapter subjectAdapter;


    public SelectChapterView(Context context, ViewGroup decorView, int marginTop, int marginBottom, Bundle data) {
        this.context = context;
        this.decorView = decorView;
        initViews(decorView, marginTop, marginBottom, data);
    }

    private void initViews(ViewGroup decorView, int marginTop, int marginBottom, Bundle data) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.dialog_fragment_select_chapter, decorView, false);
        rootView.setOnClickListener(v -> {

        });
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, marginTop, 0, marginBottom);
        rootView.setLayoutParams(layoutParams);

        lvChapterList = rootView.findViewById(R.id.lv_chapter);
        loadingView = rootView.findViewById(R.id.view_loading);
        tvVersionAndGrade = rootView.findViewById(R.id.tv_grade_subject);
        tvSwitch = rootView.findViewById(R.id.tv_switch);
        tvSwitch.setOnClickListener(this);
        rclSubject = rootView.findViewById(R.id.rcl_subject);

        receiveData(data);
        initData();
        //设置LayoutManager
        GridLayoutManager gridLMQu = new GridLayoutManager(context, 3);
        rclSubject.setLayoutManager(gridLMQu);
        rclSubject.setNestedScrollingEnabled(false);
        subjectAdapter = new SubjectAdapter(subjectList);
        rclSubject.setAdapter(subjectAdapter);

        selectTab(selectIndex);
    }

    private void receiveData(Bundle data) {
        selectSubject = data.getString("selectSubject");
        versionAndGrade = data.getString("versionGrade");
        mBookId = data.getInt("bookId");
        bookMenuList = data.getParcelableArrayList("bookMenu");
    }

    protected void initData() {
        selectIndex = 0;
        selectedBookIds = new ArrayList<>();
        selectedVersionGrades = new ArrayList<>();
        options1Items = new ArrayList<>();
        options2Items = new ArrayList<>();
        chapterList = new ArrayList<>();
        subjectList = new ArrayList<>();
        for (int i = 0; i < bookMenuList.size(); i++) {
            SubjectBean subject = new SubjectBean();
            subject.subjectId = bookMenuList.get(i).i;
            subject.subjectName = bookMenuList.get(i).n;
            subject.selected = false;
            subjectList.add(subject);
            if (bookMenuList.get(i).n.equals(selectSubject)) {
                selectIndex = i;
                selectedBookIds.add(mBookId);
                selectedVersionGrades.add(versionAndGrade);
            } else {
                selectedBookIds.add(bookMenuList.get(i).e.get(0).b.get(0).i);
                selectedVersionGrades.add(bookMenuList.get(i).e.get(0).n + " " + bookMenuList.get(i).e.get(0).b.get(0).n);
            }
        }
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    public void setOnItemSelectListener(ChapterListFragment.OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_switch:
                showSelect();
                break;
        }
    }

    /**
     * 获取已选择的bookId
     *
     * @param position
     * @return
     */
    private int getSelectBookId(int position) {
        return selectedBookIds.get(position);
    }

    /**
     * @param v      (是通过哪个View弹出的)
     * @param isAnim 是否显示动画效果
     */
    public void show(View v, boolean isAnim) {
        this.clickView = v;
        this.isAnim = isAnim;
        show();
    }

    public void show(boolean isAnim) {
        this.isAnim = isAnim;
        show();
    }

    public void show(View v) {
        this.clickView = v;
        show();
    }

    /**
     * 添加View到根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        isShowing = true;
        onAttached(rootView);
        rootView.requestFocus();
    }

    public Animation getInAnimation() {
        return AnimationUtils.loadAnimation(context, R.anim.slide_in_from_top);
    }

    public Animation getOutAnimation() {
        return AnimationUtils.loadAnimation(context, R.anim.slide_out_to_top);
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        return rootView.getParent() != null || isShowing;
    }

    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        decorView.addView(view);
        if (isAnim) {
            rootView.startAnimation(inAnim);
        }
    }

    public void dismiss() {
        if (dismissing) {
            return;
        }
        if (isAnim) {
            //消失动画
            outAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    dismissImmediately();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            rootView.startAnimation(outAnim);
        } else {
            dismissImmediately();
        }
        dismissing = true;
    }

    public void dismissImmediately() {
        decorView.post(new Runnable() {
            @Override
            public void run() {
                //从根视图移除
                decorView.removeView(rootView);
                isShowing = false;
                dismissing = false;
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });
    }

    public interface OnDismissListener {
        public void onDismiss();
    }

    /**
     * 选择某一个科目
     *
     * @param index
     */
    private void selectTab(int index) {
        if (this.index == index) {
            return;
        }
        for (SubjectBean subject : subjectList) {
            subject.selected = false;
        }
        subjectList.get(index).selected = true;
        subjectAdapter.notifyDataSetChanged();
        this.index = index;
        switchSubjectData(index);
        getChapterList();
    }

    private String getSelectVersionAndGrade(int index) {
        return selectedVersionGrades.get(index);
    }

    private void switchSubjectData(int index) {
        subjectName = bookMenuList.get(index).n;
        mBookId = getSelectBookId(index);
        bookMenuBean = bookMenuList.get(index);
        tvVersionAndGrade.setText(selectedVersionGrades.get(index));

        options1Items.clear();
        options2Items.clear();
        //选项1
        List<StartupResponse.DataBean.BookMenuBean.EBean> editions = bookMenuBean.e;
        for (int i = 0; i < editions.size(); i++) {
            options1Items.add(editions.get(i).n);
            ArrayList<String> options2Item = new ArrayList<>();
            for (StartupResponse.DataBean.BookMenuBean.EBean.BBean booksBean : editions.get(i).b) {
                options2Item.add(booksBean.n);
            }
            options2Items.add(options2Item);
        }
    }

    /**
     * 获取章节列表
     */
    private void getChapterList() {
        loadingView.setVisibility(View.VISIBLE);
        lvChapterList.setVisibility(View.GONE);
        GetChapterListService service = new GetChapterListService(context);
        service.setCallback(new IOpenApiDataServiceCallback<GetChapterListResponse>() {
            @Override
            public void onGetData(GetChapterListResponse data) {
                loadingView.setVisibility(View.GONE);
                lvChapterList.setVisibility(View.VISIBLE);
                chapterList.clear();
                chapterList.addAll(data.data.chapterList);
                ChapterAdapter adapter = new ChapterAdapter(context);
                lvChapterList.setAdapter(adapter);
                lvChapterList.setOnItemClickListener((parent, view, position, id) -> {
                    if (onItemSelectListener != null) {
                        onItemSelectListener.onSubmit(chapterList.get(position).chapterId + "", chapterList.get(position).name, getSelectBookId(index),
                                getSelectVersionAndGrade(index), subjectName);
                    }
                });
                boolean isNewGuideShow = SpUtils.getBoolean(context, "is_new_guide_show", false);
                if (!isNewGuideShow) {
                    showGuideFragment3();
                    SpUtils.putBoolean(context, "is_new_guide_show", true);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(context, errorMsg);
            }
        });
        service.postLogined("bookId=" + getSelectBookId(index), false);
    }

    class ChapterAdapter extends BaseAdapter {
        private Context mContext;

        public ChapterAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return chapterList.size();
        }

        @Override
        public Object getItem(int position) {
            return chapterList.get(position);
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
            holder.tvName.setText(chapterList.get(position).name);
            holder.tvPercent.setText(chapterList.get(position).progress + "");
            return convertView;
        }

        class ViewHolder {
            TextView tvName;
            TextView tvPercent;
        }
    }

    /**
     * 仅用在引导页面
     *
     * @return
     */
    public void showGuideFragment3() {
        int[] location = new int[2];
        tvSwitch.getLocationOnScreen(location);
        final ExposureView view32 = new ExposureView(location[0], location[1], location[0] + tvSwitch.getWidth(), tvSwitch.getHeight() + location[1]);
        new Handler().postDelayed(() -> {
            Bundle bundle3 = new Bundle();
            lvChapterList.getPositionForView(lvChapterList);
            int[] location1 = new int[2];
            lvChapterList.getLocationOnScreen(location1);
            ExposureView view31 = new ExposureView(lvChapterList.getLeft() + (int) (4 * getResources().getDisplayMetrics().density), location1[1], (int) (120 *
                    context.getResources().getDisplayMetrics()
                            .density), location1[1] + (int) (200 * getResources().getDisplayMetrics().density));
            bundle3.putParcelable("view1", view31);
            if (view32.getRight() >= context.getResources().getDisplayMetrics().widthPixels) {
                view32.setRight(context.getResources().getDisplayMetrics().widthPixels - (int) (4 * getResources().getDisplayMetrics().density));
            }
            bundle3.putParcelable("view2", view32);
            final GuideDialogFragment3 guideDialogFragment3 = new GuideDialogFragment3((Activity) context, bundle3);
            guideDialogFragment3.setPaintViewOnClickListener(v -> {
                guideDialogFragment3.dismiss();
                showSelect();
                new Handler().postDelayed(() -> showDialogFragment4(), 300);
            });
        }, 300);
    }

    private void showDialogFragment4() {
        int[] location = pvOptions.getPickerView();
        ExposureView view = new ExposureView(0, location[1], getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels - getResources()
                .getDimensionPixelOffset(R.dimen.dimen_10));
        Bundle locData = new Bundle();
        locData.putParcelable("view1", view);
        GuideDialogFragment4 guideDialogFragment4 = new GuideDialogFragment4((Activity) context, locData);
    }

    private Resources getResources() {
        return context.getResources();
    }

    private FragmentManager getFragmentManager() {
        return ((FragmentActivity) context).getSupportFragmentManager();
    }

    private void showSelect() {
        //选项选择器
        //返回的分别是三个级别的选中位置
        pvOptions = new OptionsPickerView.Builder(context, (options1, option2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            try {
                str1 = options1Items.get(options1);
                str2 = options2Items.get(options1).get(option2);
                int bookId = bookMenuBean.e.get(options1).b.get(option2).i;
                if (bookId == selectedBookIds.get(index)) {
                    return;
                }
                versionGrade = str1 + " " + str2;
                selectedVersionGrades.set(index, versionGrade);
                tvVersionAndGrade.setText(versionGrade);
                selectedBookIds.set(index, bookId);
                getChapterList();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }).setLayoutRes(R.layout.pickerview_options2, null).build();

        pvOptions.setSelectOptions(0, 0);
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
    }

    private class SubjectAdapter extends RecyclerView.Adapter<SubjectHolder> {
        private List<SubjectBean> subjectData;

        public SubjectAdapter(List<SubjectBean> subjectData) {
            this.subjectData = subjectData;
        }

        @Override
        public SubjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_subject_tv_item, parent, false);
            return new SubjectHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SubjectHolder holder, int position) {
            SubjectBean subject = subjectData.get(position);
            holder.tvSubject.setText(subject.subjectName);
            if (subject.selected) {
                holder.tvSubject.setSelected(true);
            } else {
                holder.tvSubject.setSelected(false);
            }
            holder.tvSubject.setOnClickListener(v -> {
                selectTab(position);
            });
        }

        @Override
        public int getItemCount() {
            return subjectData.size();
        }
    }

    private class SubjectHolder extends RecyclerView.ViewHolder {
        private TextView tvSubject;

        public SubjectHolder(View itemView) {
            super(itemView);
            tvSubject = itemView.findViewById(R.id.tv_subject);
        }
    }

    private static class SubjectBean {
        public String subjectName;
        public int subjectId;
        public boolean selected;
    }
}
