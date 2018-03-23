package com.physicmaster.modules.mine.activity.notebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.guide.ExposureView;
import com.physicmaster.modules.guide.GuideNoteBookDialogFragment1;
import com.physicmaster.modules.study.fragment.dialogfragment.AddTagDialogFragment;
import com.physicmaster.modules.study.fragment.dialogfragment.DeleteDirDialogFragment;
import com.physicmaster.modules.study.fragment.widget.dynamicbg.HeaderAndFooterWrapper;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.notebook.AddDirResponse;
import com.physicmaster.net.response.notebook.GetTagResponse;
import com.physicmaster.net.response.notebook.GetTagResponse.DataBean.DirListBean;
import com.physicmaster.net.service.notebook.AddTagService;
import com.physicmaster.net.service.notebook.GetTagService;
import com.physicmaster.net.service.notebook.RmQuDirService;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2017-12-05.
 */

public class NoteBookFragment extends BaseFragment2 {
    private RecyclerView rclNote;
    private HeaderAndFooterWrapper headerAndFooterWrapper;
    private List<DirListBean> tagsList;
    private String subjectId;
    private static final int[] COLORS = {R.color.color_fcbb5f, R.color.color_4dddb9, R.color.color_9cb6f6, R.color.color_fb8888};
    private int itemHeight;
    private View footerAdd;

    @Override
    protected void initView(View view) {
        subjectId = getArguments().getString("subjectId");
        rclNote = rootView.findViewById(R.id.rcv_note);

        //设置LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rclNote.setLayoutManager(gridLayoutManager);

        tagsList = new ArrayList<>();
        NBAdapter nbAdapter = new NBAdapter(tagsList, subjectId, getContext());
        headerAndFooterWrapper = new HeaderAndFooterWrapper(nbAdapter);
        //添加增加错题本item
        footerAdd = LayoutInflater.from(getContext()).inflate(R.layout.rcv_notebook_item_add, rclNote, false);
        itemHeight = calItemHeight();
        View vIcon = footerAdd.findViewById(R.id.rl_icon);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) vIcon.getLayoutParams();
        params.height = itemHeight;
        vIcon.setLayoutParams(params);
        footerAdd.setOnClickListener(v -> {
            AddTagDialogFragment addTagDialogFragment = new AddTagDialogFragment();
            addTagDialogFragment.setOnActionBtnClickListener(name -> addDir(name));
            addTagDialogFragment.show(getFragmentManager(), "addDir");
        });
        headerAndFooterWrapper.addFootView(footerAdd);
        rclNote.setAdapter(headerAndFooterWrapper);
    }

    /**
     * 获取错题本标签目录
     */
    private void getDirs() {
        final GetTagService service = new GetTagService(getContext());
        final ProgressLoadingDialog dialog = new ProgressLoadingDialog(getContext());
        dialog.showDialog(() -> service.cancel());
        service.setCallback(new IOpenApiDataServiceCallback<GetTagResponse>() {
            @Override
            public void onGetData(GetTagResponse data) {
                refreshUI(data.data.dirList);
                dialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                dialog.dismissDialog();
                UIUtils.showToast(getContext(), errorMsg);
            }
        });
        service.postLogined("subjectId=" + subjectId, false);
    }

    /**
     * 刷新界面
     *
     * @param dirList
     */
    private void refreshUI(List<DirListBean> dirList) {
        tagsList.clear();
        tagsList.addAll(dirList);
        headerAndFooterWrapper.notifyDataSetChanged();
        if (!subjectId.equals(Constant.SUBJECTID_PM + "")) {
            return;
        }
        if (tagsList.size() > 0) {
            return;
        }
        new Handler().postDelayed(() -> {
            boolean isShowed = SpUtils.getBoolean(getContext(), SpUtils.SHOW_NOTEBOOK_GUIDE, false);
            if (!isShowed) {
                int[] location = new int[2];
                footerAdd.getLocationOnScreen(location);
                int statusBarHeight = ScreenUtils.getStatusBarHeight();
                ExposureView exposureView = new ExposureView(location[0], location[1], location[0] + footerAdd.getWidth(), location[1] + footerAdd
                        .getHeight() - statusBarHeight);
                Bundle data = new Bundle();
                data.putParcelable("view1", exposureView);
                SpUtils.putBoolean(getContext(), SpUtils.SHOW_NOTEBOOK_GUIDE, true);
                GuideNoteBookDialogFragment1 guideNoteBookDialogFragment1 = new GuideNoteBookDialogFragment1(getActivity(), data);
                guideNoteBookDialogFragment1.setPaintViewOnClickListener(v -> {
                    guideNoteBookDialogFragment1.dismiss();
                    NoteBookActivity activity = (NoteBookActivity) getActivity();
                    activity.showGuide();
                });
            }
        }, 300);
    }

    /**
     * 提交标签到服务器
     *
     * @param name
     */
    private void addDir(String name) {
        final AddTagService service = new AddTagService(getContext());
        final ProgressLoadingDialog dialog = new ProgressLoadingDialog(getContext());
        dialog.showDialog(() -> service.cancel());
        service.setCallback(new IOpenApiDataServiceCallback<AddDirResponse>() {
            @Override
            public void onGetData(AddDirResponse data) {
                dialog.dismissDialog();
                UIUtils.showToast(getContext(), "添加成功~");
                getDirs();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                dialog.dismissDialog();
                UIUtils.showToast(getContext(), errorMsg);
            }
        });
        try {
            name = URLEncoder.encode(name, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.postLogined("subjectId=" + subjectId + "&dirName=" + name, false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_notebook;
    }

    @Override
    public void fetchData() {
    }

    @Override
    public void onResume() {
        super.onResume();
        getDirs();
    }

    private int calItemHeight() {
        int leftWidth = BaseApplication.getScreenWidth() - mContext.getResources().getDimensionPixelSize(R.dimen.dimen_50);
        return leftWidth / 3;
    }

    private class NBAdapter extends RecyclerView.Adapter<NBViewHolder> {
        private List<DirListBean> nbData;
        private String subjectId;
        private Context mContext;
        private int itemHeight;

        public NBAdapter(List<DirListBean> nbData, String subjectId, Context context) {
            this.nbData = nbData;
            this.subjectId = subjectId;
            this.mContext = context;
            itemHeight = calItemHeight();
        }

        @Override
        public NBViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_notebook_item, parent, false);
            return new NBViewHolder(itemView, itemHeight);
        }

        @Override
        public void onBindViewHolder(NBViewHolder holder, int position) {
            TextView tvTitle = holder.tvTitle;
            TextView tvQuNum = holder.tvQuNum;
            int index = position % 4;
            int color = COLORS[index];
            tvTitle.setText(nbData.get(position).name);
            if (nbData.get(position).name.equals("掌握归档")) {
                color = R.color.color_52c2fd;
            }
            tvTitle.setBackgroundColor(mContext.getResources().getColor(color));
            tvQuNum.setBackgroundColor(Color.TRANSPARENT);
            tvQuNum.setText("共" + nbData.get(position).co + "题");

            holder.rootView.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, NoteBookWebActivity.class);
                intent.putExtra("pageType", 1);
                intent.putExtra("subjectId", subjectId);
                intent.putExtra("dirId", nbData.get(position).dirId + "");
                startActivityForResult(intent, 1);
            });
            holder.rootView.setOnLongClickListener(v -> {
                DeleteDirDialogFragment fragment = new DeleteDirDialogFragment();
                fragment.show(getFragmentManager(), "deleteDir");
                fragment.setOnActionBtnClickListener(() -> {
                    if (nbData.get(position).rmEnable) {
                        removeDir(nbData.get(position).dirId);
                    } else {
                        if (nbData.get(position).co > 0) {
                            UIUtils.showToast(getContext(), "目录中存在题目，无法删除");
                        } else {
                            UIUtils.showToast(getContext(), "归档目录不能删除");
                        }
                    }
                });
                return true;
            });
        }

        @Override
        public int getItemCount() {
            return nbData.size();
        }
    }

    /**
     * 删除错题目录
     *
     * @param dirId
     */
    private void removeDir(int dirId) {
        RmQuDirService service = new RmQuDirService(getContext());
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                UIUtils.showToast(getContext(), "删除成功");
                getDirs();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getContext(), errorMsg);
            }
        });
        service.postLogined("dirId=" + dirId, false);
    }

    private static class NBViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvQuNum;
        public View rootView;

        public NBViewHolder(View itemView, int itemHeight) {
            super(itemView);
            rootView = itemView;
            tvTitle = itemView.findViewById(R.id.tv_title);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvTitle.getLayoutParams();
            params.height = itemHeight;
            tvTitle.setLayoutParams(params);
            tvQuNum = itemView.findViewById(R.id.tv_qu_num);
        }
    }
}
