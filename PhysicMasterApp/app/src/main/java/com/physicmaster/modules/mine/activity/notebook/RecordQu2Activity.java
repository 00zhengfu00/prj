package com.physicmaster.modules.mine.activity.notebook;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.modules.study.fragment.dialogfragment.AddTagDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.request.notebook.RecordQuBean;
import com.physicmaster.net.response.notebook.AddDirResponse;
import com.physicmaster.net.response.notebook.GetWrongWhyResponse;
import com.physicmaster.net.response.notebook.RecordWrongResponse;
import com.physicmaster.net.service.notebook.AddTagService;
import com.physicmaster.net.service.notebook.GetAllTagService;
import com.physicmaster.net.service.notebook.GetWrongWhyService;
import com.physicmaster.net.service.notebook.RecordWrongService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.CheckableTextView;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;
import com.physicmaster.widget.myFlowLayout.FlowLayout;
import com.physicmaster.widget.myFlowLayout.TagAdapter;
import com.physicmaster.widget.myFlowLayout.TagFlowLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RecordQu2Activity extends BaseActivity implements View.OnClickListener {


    private ImageView ivAdd, ivHuang1, ivHuang2, ivHuang3, ivHuang4, ivHuang5, ivHong1, ivHong2, ivHong3, ivHong4, ivHong5;
    private Button btnSubmit;
    private TagFlowLayout mFlowLayout1, mFlowLayout3;
    private List<GetWrongWhyResponse.DataBean.QuWrongTagsBean> quWrongTags;
    private int dirId = -1;
    private int difficultyLevel = -1;
    private int masterLevel = -1;
    private RecordQuBean recordQuData;
    private List<AddDirResponse.DataBean.QuWrongDirsBean.AppUserQuWrongDirDtoListBean> appUserQuWrongDirDtoList = new ArrayList<>();
    private String subjectId;
    private TagAdapter tagAdapter;

    @Override
    protected void findViewById() {
        ivAdd = findViewById(R.id.iv_add);
        ivHuang1 = findViewById(R.id.iv_huang1);
        ivHuang2 = findViewById(R.id.iv_huang2);
        ivHuang3 = findViewById(R.id.iv_huang3);
        ivHuang4 = findViewById(R.id.iv_huang4);
        ivHuang5 = findViewById(R.id.iv_huang5);
        ivHong1 = findViewById(R.id.iv_hong1);
        ivHong2 = findViewById(R.id.iv_hong2);
        ivHong3 = findViewById(R.id.iv_hong3);
        ivHong4 = findViewById(R.id.iv_hong4);
        ivHong5 = findViewById(R.id.iv_hong5);
        btnSubmit = findViewById(R.id.btn_submit);
        mFlowLayout1 = findViewById(R.id.flowlayout1);
        mFlowLayout3 = findViewById(R.id.flowlayout3);

        ivAdd.setOnClickListener(this);
        ivHuang1.setOnClickListener(this);
        ivHuang2.setOnClickListener(this);
        ivHuang3.setOnClickListener(this);
        ivHuang4.setOnClickListener(this);
        ivHuang5.setOnClickListener(this);
        ivHong1.setOnClickListener(this);
        ivHong2.setOnClickListener(this);
        ivHong3.setOnClickListener(this);
        ivHong4.setOnClickListener(this);
        ivHong5.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

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
                .setMiddleTitleText("录入错题");
    }


    @Override
    protected void initView() {
        recordQuData = getIntent().getParcelableExtra("recordQuData");
        subjectId = getIntent().getStringExtra("subjectId");
        getWrongWhy();
        getAllTag();
    }

    private void getWrongWhy() {
        final GetWrongWhyService service = new GetWrongWhyService(RecordQu2Activity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GetWrongWhyResponse>() {

            @Override
            public void onGetData(GetWrongWhyResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                quWrongTags = data.data.quWrongTags;

                mFlowLayout1.setAdapter(new TagAdapter<GetWrongWhyResponse.DataBean.QuWrongTagsBean>(quWrongTags) {
                    @Override
                    public View getView(FlowLayout parent, int position, GetWrongWhyResponse.DataBean.QuWrongTagsBean quWrong) {
                        CheckableTextView tvWhy = (CheckableTextView) getLayoutInflater().inflate(R.layout.grid_item_why,
                                mFlowLayout1, false);
                        tvWhy.setText(quWrong.name + "");
                        return tvWhy;
                    }
                });

                mFlowLayout1.setOnSelectListener(selectPosSet -> {

                });
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(RecordQu2Activity.this, errorMsg);

            }
        });
        service.postLogined("", false);
    }

    private void getAllTag() {
        final GetAllTagService service = new GetAllTagService(RecordQu2Activity.this);
        service.setCallback(new IOpenApiDataServiceCallback<AddDirResponse>() {

            @Override
            public void onGetData(AddDirResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                appUserQuWrongDirDtoList = data.data.quWrongDirs.get(0).appUserQuWrongDirDtoList;
                tagAdapter = new TagAdapter<AddDirResponse.DataBean.QuWrongDirsBean.AppUserQuWrongDirDtoListBean>(appUserQuWrongDirDtoList) {
                    @Override
                    public View getView(FlowLayout parent, int position, AddDirResponse.DataBean.QuWrongDirsBean.AppUserQuWrongDirDtoListBean appUserQuWrongDirDto) {
                        TextView tvWhy = (TextView) getLayoutInflater().inflate(R.layout.grid_item_dir,
                                mFlowLayout3, false);
                        tvWhy.setText(appUserQuWrongDirDto.dirName + "");
                        return tvWhy;
                    }
                };
                mFlowLayout3.setAdapter(tagAdapter);
                mFlowLayout3.setOnSelectListener(selectPosSet -> {
                    if (selectPosSet.iterator().hasNext()) {
                        int position = selectPosSet.iterator().next();
                        dirId = appUserQuWrongDirDtoList.get(position).dirId;
                    } else {
                        dirId = -1;
                    }
                });
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(RecordQu2Activity.this, errorMsg);

            }
        });
        service.postLogined("subjectId=" + subjectId, false);
    }

    private void submit4sever() {


        if (difficultyLevel == -1) {
            UIUtils.showToast(this, "请选择难易程度");
            return;
        }
        if (masterLevel == -1) {
            UIUtils.showToast(this, "请选择掌握程度");
            return;
        }

        if (dirId == -1) {
            UIUtils.showToast(this, "请选择标签");
            return;
        }

        recordQuData.setDifficultyLevel(difficultyLevel);
        recordQuData.setMasterLevel(masterLevel);

        Set<Integer> selectedList = mFlowLayout1.getSelectedList();
        if (null != selectedList && selectedList.size() != 0) {
            ArrayList<Integer> integers = new ArrayList<>(selectedList);
            ArrayList<Integer> selectedWhy = new ArrayList<>();
            for (int j = 0; j < integers.size(); j++) {
                for (int i = 0; i < quWrongTags.size(); i++) {
                    if (quWrongTags.get(integers.get(j).intValue()).tagId == quWrongTags.get(i).tagId) {
                        selectedWhy.add(quWrongTags.get(i).tagId);
                    }
                }
            }
            Gson gson = new Gson();
            String json = gson.toJson(selectedWhy);
            recordQuData.setTagId(json);
        }

        if (dirId != -1) {
            recordQuData.setDirId(dirId);
        }

        final RecordWrongService service = new RecordWrongService(RecordQu2Activity.this);
        service.setCallback(new IOpenApiDataServiceCallback<RecordWrongResponse>() {

            @Override
            public void onGetData(RecordWrongResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                UIUtils.showToast(RecordQu2Activity.this, "提交成功");
                startActivity(new Intent(RecordQu2Activity.this, NoteBookActivity.class));
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(RecordQu2Activity.this, errorMsg);

            }
        });
        service.postLogined(recordQuData.toString(), false);
    }

    /**
     * 提交标签到服务器
     *
     * @param name
     */
    private void addDir(String name) {
        final AddTagService service = new AddTagService(RecordQu2Activity.this);
        final ProgressLoadingDialog dialog = new ProgressLoadingDialog(RecordQu2Activity.this);
        dialog.showDialog(() -> service.cancel());
        String finalName = name;
        service.setCallback(new IOpenApiDataServiceCallback<AddDirResponse>() {
            @Override
            public void onGetData(AddDirResponse data) {
                dialog.dismissDialog();
                UIUtils.showToast(RecordQu2Activity.this, "添加成功~");
                if (null != appUserQuWrongDirDtoList && appUserQuWrongDirDtoList.size() != 0) {
                    appUserQuWrongDirDtoList.clear();
                }
                appUserQuWrongDirDtoList.addAll(data.data.quWrongDirs.get(0).appUserQuWrongDirDtoList);
                tagAdapter.setSelected(appUserQuWrongDirDtoList.size() - 1, true);
                dirId = data.data.quWrongDirs.get(0).appUserQuWrongDirDtoList.get(appUserQuWrongDirDtoList.size() - 1).dirId;
                tagAdapter.notifyDataChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                dialog.dismissDialog();
                UIUtils.showToast(RecordQu2Activity.this, errorMsg);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                AddTagDialogFragment addTagDialogFragment = new AddTagDialogFragment();
                addTagDialogFragment.setOnActionBtnClickListener(name -> addDir(name));
                addTagDialogFragment.show(getSupportFragmentManager(), "addDir");
                break;
            case R.id.btn_submit:
                submit4sever();
                break;
            case R.id.iv_hong1:
                if (ivHong1.isSelected()) {
                    if (ivHong2.isSelected()) {
                        difficultyLevel = 1;
                        ivHong1.setSelected(true);
                        ivHong2.setSelected(false);
                        ivHong3.setSelected(false);
                        ivHong4.setSelected(false);
                        ivHong5.setSelected(false);
                    } else {
                        difficultyLevel = -1;
                        ivHong1.setSelected(false);
                        ivHong2.setSelected(false);
                        ivHong3.setSelected(false);
                        ivHong4.setSelected(false);
                        ivHong5.setSelected(false);
                    }
                } else {
                    difficultyLevel = 1;
                    ivHong1.setSelected(true);
                    ivHong2.setSelected(false);
                    ivHong3.setSelected(false);
                    ivHong4.setSelected(false);
                    ivHong5.setSelected(false);
                }

                break;
            case R.id.iv_hong2:
                if (ivHong2.isSelected()) {
                    if (ivHong3.isSelected()) {
                        difficultyLevel = 2;
                        ivHong1.setSelected(true);
                        ivHong2.setSelected(true);
                        ivHong3.setSelected(false);
                        ivHong4.setSelected(false);
                        ivHong5.setSelected(false);
                    } else {
                        difficultyLevel = -1;
                        ivHong1.setSelected(false);
                        ivHong2.setSelected(false);
                        ivHong3.setSelected(false);
                        ivHong4.setSelected(false);
                        ivHong5.setSelected(false);
                    }
                } else {
                    difficultyLevel = 2;
                    ivHong1.setSelected(true);
                    ivHong2.setSelected(true);
                    ivHong3.setSelected(false);
                    ivHong4.setSelected(false);
                    ivHong5.setSelected(false);
                }

                break;
            case R.id.iv_hong3:
                if (ivHong3.isSelected()) {
                    if (ivHong4.isSelected()) {
                        difficultyLevel = 3;
                        ivHong1.setSelected(true);
                        ivHong2.setSelected(true);
                        ivHong3.setSelected(true);
                        ivHong4.setSelected(false);
                        ivHong5.setSelected(false);
                    } else {
                        difficultyLevel = -1;
                        ivHong1.setSelected(false);
                        ivHong2.setSelected(false);
                        ivHong3.setSelected(false);
                        ivHong4.setSelected(false);
                        ivHong5.setSelected(false);
                    }
                } else {
                    difficultyLevel = 3;
                    ivHong1.setSelected(true);
                    ivHong2.setSelected(true);
                    ivHong3.setSelected(true);
                    ivHong4.setSelected(false);
                    ivHong5.setSelected(false);
                }

                break;
            case R.id.iv_hong4:
                if (ivHong4.isSelected()) {
                    if (ivHong5.isSelected()) {
                        difficultyLevel = 4;
                        ivHong1.setSelected(true);
                        ivHong2.setSelected(true);
                        ivHong3.setSelected(true);
                        ivHong4.setSelected(true);
                        ivHong5.setSelected(false);
                    } else {
                        difficultyLevel = -1;
                        ivHong1.setSelected(false);
                        ivHong2.setSelected(false);
                        ivHong3.setSelected(false);
                        ivHong4.setSelected(false);
                        ivHong5.setSelected(false);
                    }
                } else {
                    difficultyLevel = 4;
                    ivHong1.setSelected(true);
                    ivHong2.setSelected(true);
                    ivHong3.setSelected(true);
                    ivHong4.setSelected(true);
                    ivHong5.setSelected(false);
                }

                break;
            case R.id.iv_hong5:
                if (ivHong5.isSelected()) {
                    difficultyLevel = -1;
                    ivHong1.setSelected(false);
                    ivHong2.setSelected(false);
                    ivHong3.setSelected(false);
                    ivHong4.setSelected(false);
                    ivHong5.setSelected(false);
                } else {
                    difficultyLevel = 5;
                    ivHong1.setSelected(true);
                    ivHong2.setSelected(true);
                    ivHong3.setSelected(true);
                    ivHong4.setSelected(true);
                    ivHong5.setSelected(true);
                }

                break;

            case R.id.iv_huang1:
                if (ivHuang1.isSelected()) {
                    if (ivHuang2.isSelected()) {
                        masterLevel = 1;
                        ivHuang1.setSelected(true);
                        ivHuang2.setSelected(false);
                        ivHuang3.setSelected(false);
                        ivHuang4.setSelected(false);
                        ivHuang5.setSelected(false);
                    } else {
                        masterLevel = -1;
                        ivHuang1.setSelected(false);
                        ivHuang2.setSelected(false);
                        ivHuang3.setSelected(false);
                        ivHuang4.setSelected(false);
                        ivHuang5.setSelected(false);
                    }

                } else {
                    masterLevel = 1;
                    ivHuang1.setSelected(true);
                    ivHuang2.setSelected(false);
                    ivHuang3.setSelected(false);
                    ivHuang4.setSelected(false);
                    ivHuang5.setSelected(false);
                }

                break;
            case R.id.iv_huang2:
                if (ivHuang2.isSelected()) {
                    if (ivHuang3.isSelected()) {
                        masterLevel = 2;
                        ivHuang1.setSelected(true);
                        ivHuang2.setSelected(true);
                        ivHuang3.setSelected(false);
                        ivHuang4.setSelected(false);
                        ivHuang5.setSelected(false);
                    } else {
                        masterLevel = -1;
                        ivHuang1.setSelected(false);
                        ivHuang2.setSelected(false);
                        ivHuang3.setSelected(false);
                        ivHuang4.setSelected(false);
                        ivHuang5.setSelected(false);
                    }
                } else {
                    masterLevel = 2;
                    ivHuang1.setSelected(true);
                    ivHuang2.setSelected(true);
                    ivHuang3.setSelected(false);
                    ivHuang4.setSelected(false);
                    ivHuang5.setSelected(false);
                }

                break;
            case R.id.iv_huang3:
                if (ivHuang3.isSelected()) {
                    if (ivHuang4.isSelected()) {
                        masterLevel = 3;
                        ivHuang1.setSelected(true);
                        ivHuang2.setSelected(true);
                        ivHuang3.setSelected(true);
                        ivHuang4.setSelected(false);
                        ivHuang5.setSelected(false);
                    } else {
                        masterLevel = -1;
                        ivHuang1.setSelected(false);
                        ivHuang2.setSelected(false);
                        ivHuang3.setSelected(false);
                        ivHuang4.setSelected(false);
                        ivHuang5.setSelected(false);
                    }
                } else {
                    masterLevel = 3;
                    ivHuang1.setSelected(true);
                    ivHuang2.setSelected(true);
                    ivHuang3.setSelected(true);
                    ivHuang4.setSelected(false);
                    ivHuang5.setSelected(false);
                }

                break;
            case R.id.iv_huang4:
                if (ivHuang4.isSelected()) {
                    if (ivHuang5.isSelected()) {
                        masterLevel = 4;
                        ivHuang1.setSelected(true);
                        ivHuang2.setSelected(true);
                        ivHuang3.setSelected(true);
                        ivHuang4.setSelected(true);
                        ivHuang5.setSelected(false);
                    } else {
                        masterLevel = -1;
                        ivHuang1.setSelected(false);
                        ivHuang2.setSelected(false);
                        ivHuang3.setSelected(false);
                        ivHuang4.setSelected(false);
                        ivHuang5.setSelected(false);
                    }
                } else {
                    masterLevel = 4;
                    ivHuang1.setSelected(true);
                    ivHuang2.setSelected(true);
                    ivHuang3.setSelected(true);
                    ivHuang4.setSelected(true);
                    ivHuang5.setSelected(false);
                }

                break;
            case R.id.iv_huang5:
                if (ivHuang5.isSelected()) {
                    masterLevel = -1;
                    ivHuang1.setSelected(false);
                    ivHuang2.setSelected(false);
                    ivHuang3.setSelected(false);
                    ivHuang4.setSelected(false);
                    ivHuang5.setSelected(false);
                } else {
                    masterLevel = 5;
                    ivHuang1.setSelected(true);
                    ivHuang2.setSelected(true);
                    ivHuang3.setSelected(true);
                    ivHuang4.setSelected(true);
                    ivHuang5.setSelected(true);
                }

                break;
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_record_qu2;
    }
}
