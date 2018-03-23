package com.physicmaster.modules.study.fragment.chapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.physicmaster.R;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.modules.guide.ExposureView;
import com.physicmaster.modules.guide.GuideDialogFragment3;
import com.physicmaster.modules.guide.GuideDialogFragment4;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.excercise.GetChapterListResponse;
import com.physicmaster.net.response.excercise.GetChapterListResponse.DataBean.ChapterListBean;
import com.physicmaster.net.service.excercise.GetChapterListService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.LoadingView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChapterListFragment extends BaseFragment2 {
    private ListView lvSubjects;
    private List<ChapterListBean> list;
    private OnItemSelectListener onItemSelectListener;
    private StartupResponse.DataBean.BookMenuBean bookMenuBean;
    private int bookId;
    private ArrayList<String> options1Items;
    private ArrayList<ArrayList<String>> options2Items;
    private String str1;
    private String str2;
    private TextView tvVersionAndGrade, tvSwitch;
    private OptionsPickerView pvOptions;
    private String versionGrade, subjectName;
    private LoadingView loadingView;

    public ChapterListFragment() {
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @Override
    protected void initView(View view) {
        lvSubjects = (ListView) rootView.findViewById(R.id.lv_chapter);
        loadingView = (LoadingView) rootView.findViewById(R.id.view_loading);
        bookMenuBean = getArguments().getParcelable("bookMenuBean");

        tvVersionAndGrade = (TextView) rootView.findViewById(R.id.tv_grade_subject);
        tvSwitch = (TextView) rootView.findViewById(R.id.tv_switch);

        tvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelect();
            }
        });
        list = new ArrayList<>();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chapter_list;
    }

    @Override
    public void fetchData() {
        Bundle data = getArguments();
        bookId = data.getInt("bookId");
        subjectName = data.getString("subjectName");
        versionGrade = data.getString("versionGrade");
        if (TextUtils.isEmpty(versionGrade)) {
            versionGrade = bookMenuBean.e.get(0).n + " " + bookMenuBean.e.get(0).b.get(0).n;
        }
        tvVersionAndGrade.setText(versionGrade);
        init();
        getChapterList();
    }

    private void init() {
        options1Items = new ArrayList<>();
        options2Items = new ArrayList<>();
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

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获取章节列表
     */
    private void getChapterList() {
        loadingView.setVisibility(View.VISIBLE);
        lvSubjects.setVisibility(View.GONE);
        GetChapterListService service = new GetChapterListService(getContext());
        service.setCallback(new IOpenApiDataServiceCallback<GetChapterListResponse>() {
            @Override
            public void onGetData(GetChapterListResponse data) {
                loadingView.setVisibility(View.GONE);
                lvSubjects.setVisibility(View.VISIBLE);
                list.clear();
                list.addAll(data.data.chapterList);
                SubjectAdapter adapter = new SubjectAdapter(mContext);
                lvSubjects.setAdapter(adapter);
                lvSubjects.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onSubmit(list.get(position).chapterId + "", list.get(position).name, bookId, versionGrade, subjectName);
                        }
                    }
                });
                boolean isNewGuideShow = SpUtils.getBoolean(mContext, "is_new_guide_show", false);
                if (!isNewGuideShow) {
                    showGuideFragment3();
                    SpUtils.putBoolean(mContext, "is_new_guide_show", true);
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getActivity(), errorMsg);
            }
        });
        service.postLogined("bookId=" + bookId, false);
    }

    private void showSelect() {
        //选项选择器
        //返回的分别是三个级别的选中位置
        pvOptions = new OptionsPickerView.Builder(getContext(), new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                try {
                    str1 = options1Items.get(options1);
                    str2 = options2Items.get(options1).get(option2);
                    versionGrade = str1 + " " + str2;
                    tvVersionAndGrade.setText(versionGrade);
                    bookId = bookMenuBean.e.get(options1).b.get(option2).i;
                    getChapterList();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).setLayoutRes(R.layout.pickerview_options2, null).build();

        pvOptions.setSelectOptions(0, 0);
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
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
            lvSubjects.getPositionForView(lvSubjects);
            int[] location1 = new int[2];
            lvSubjects.getLocationOnScreen(location1);
            ExposureView view31 = new ExposureView(lvSubjects.getLeft() + (int) (4 * getResources().getDisplayMetrics().density), location1[1], (int) (120 *
                    getResources().getDisplayMetrics()
                            .density), location1[1] + (int) (200 * getResources().getDisplayMetrics().density));
            bundle3.putParcelable("view1", view31);
            if (view32.getRight() >= getResources().getDisplayMetrics().widthPixels) {
                view32.setRight(getResources().getDisplayMetrics().widthPixels - (int) (4 * getResources().getDisplayMetrics().density));
            }
            bundle3.putParcelable("view2", view32);
            GuideDialogFragment3 guideDialogFragment3 = new GuideDialogFragment3(getActivity(),bundle3);
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
        GuideDialogFragment4 guideDialogFragment4 = new GuideDialogFragment4(getActivity(),locData);
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

    public interface OnItemSelectListener {
        void onSubmit(String chapterId, String name, int selectBookId, String versionGrade, String selectSubject);
    }
}