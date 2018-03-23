package com.physicmaster.modules.mine.activity.notebook;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.common.MultiImageUploadManager;
import com.physicmaster.modules.discuss.activity.EditImageActivity;
import com.physicmaster.modules.mine.activity.notebook.cameratool.picture.PictureTakeActivity;
import com.physicmaster.modules.study.fragment.widget.dynamicbg.HeaderAndFooterWrapper;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.request.notebook.RecordQuBean;
import com.physicmaster.net.response.account.GetOssInfoResponse;
import com.physicmaster.net.response.account.GetOssInfoResponse.DataBean.OssConfigBean;
import com.physicmaster.net.response.account.GetOssInfoResponse.DataBean.OssTokenBean;
import com.physicmaster.net.service.account.GetOssInfoService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ImageUploadDialog;
import com.physicmaster.widget.TitleBuilder;
import com.physicmaster.widget.cropview.Crop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordQuActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView rclQuestion, rclAnswer;
    private HeaderAndFooterWrapper questionWrapper, answerWrapper;
    private List<String> questionIMGs;
    private List<String> answerIMGs;
    private static final int REQUEST_CODE_TAKE_QUESTION_IMG = 1;
    private static final int REQUEST_CODE_TAKE_ANSWER_IMG = 2;
    private static final int REQUEST_CODE_PICK_QUESTION_IMG = 3;
    private static final int REQUEST_CODE_PICK_ANSWER_IMG = 4;
    private static final int TYPE_GET_QUESTION_IMG = 1;
    private static final int TYPE_GET_ANSWER_IMG = 2;
    public static final String EXTRA_PICTURE_PATHS = "EXTRA_PICTURE_PATHS";
    public static final String EXTRA_PICTURE_POSITION = "EXTRA_PICTURE_POSITION";
    public static final int REQUEST_CODE_EDIT_PICTURE = 0x104;
    private View footerAnswerAdd;
    private View footerQuestionAdd;
    private ProgressDialog progressDialog;
    private RecordQuBean recordQuBean;
    private String subjectId;

    @Override
    protected void findViewById() {
        rclQuestion = findViewById(R.id.rcl_question);
        rclAnswer = findViewById(R.id.rcl_answer);
        findViewById(R.id.btn_next).setOnClickListener(this);
    }

    @Override
    protected void initView() {
        initTitle();
        subjectId = getIntent().getStringExtra("subjectId");
        //设置RecyclerView 内部不滚动
        rclQuestion.setNestedScrollingEnabled(false);
        rclAnswer.setNestedScrollingEnabled(false);
        //设置LayoutManager
        GridLayoutManager gridLMQu = new GridLayoutManager(this, 3);
        rclQuestion.setLayoutManager(gridLMQu);
        GridLayoutManager gridLMAn = new GridLayoutManager(this, 3);
        rclAnswer.setLayoutManager(gridLMAn);

        //添加题目图片的RecyclerView初始化
        questionIMGs = new ArrayList<>();
        IMGAdapter questionAdapter = new IMGAdapter(questionIMGs, this, 1);
        questionWrapper = new HeaderAndFooterWrapper(questionAdapter);
        footerQuestionAdd = LayoutInflater.from(this).inflate(R.layout.rcv_record_item_camera, rclQuestion, false);
        footerQuestionAdd.setOnClickListener(v -> permissionRequest(TYPE_GET_QUESTION_IMG));
        questionWrapper.addFootView(footerQuestionAdd);
        rclQuestion.setAdapter(questionWrapper);

        //添加答案图片的RecyclerView初始化
        answerIMGs = new ArrayList<>();
        IMGAdapter answerAdapter = new IMGAdapter(answerIMGs, this, 2);
        answerWrapper = new HeaderAndFooterWrapper(answerAdapter);
        footerAnswerAdd = LayoutInflater.from(this).inflate(R.layout.rcv_record_item_camera, rclAnswer, false);
        footerAnswerAdd.setOnClickListener(v -> permissionRequest(TYPE_GET_ANSWER_IMG));
        answerWrapper.addFootView(footerAnswerAdd);
        rclAnswer.setAdapter(answerWrapper);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_record_qu;
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(view -> finish()).setMiddleTitleText("自录错题");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next: {
                doNext();
                break;
            }
        }
    }

    /**
     * 输入参数检验
     */
    private void doNext() {
        //题干和解析：文字或者图片不能同时为空
        //我的答案、正确答案可为空
        EditText etQuestion = findViewById(R.id.et_question);
        EditText etMyAnswer = findViewById(R.id.et_my_answer);
        EditText etRightAnswer = findViewById(R.id.et_right_answer);
        EditText etAnalysis = findViewById(R.id.et_analysis);
        String question = etQuestion.getText().toString();
        String myAnswer = etMyAnswer.getText().toString();
        String rightAnswer = etRightAnswer.getText().toString();
        String analysis = etAnalysis.getText().toString();
        if (TextUtils.isEmpty(question) && questionIMGs.size() == 0) {
            UIUtils.showToast(this, "请输入题目内容或上传图片");
            return;
        }
        if (TextUtils.isEmpty(analysis) && answerIMGs.size() == 0) {
            UIUtils.showToast(this, "请输入题目解析内容或上传图片");
            return;
        }
        recordQuBean = new RecordQuBean();
        recordQuBean.setQuestionDes(question);
        recordQuBean.setAnalysisDes(analysis);
        recordQuBean.setUserAnswer(myAnswer);
        recordQuBean.setStandardAnswer(rightAnswer);
        if (questionIMGs.size() > 0 || answerIMGs.size() > 0) {
            progressDialog = ProgressDialog.show(this, "正在上传图片", "请等候……", true, false);
            progressDialog.show();
            getOssInfo();
        } else {
            Intent intent = new Intent(RecordQuActivity.this, RecordQu2Activity.class);
            intent.putExtra("recordQuData", recordQuBean);
            intent.putExtra("subjectId", subjectId);
            startActivity(intent);
        }
    }

    /*************************************
     * 阿里云oss开始
     ********************************/
    //临时AccessKeyId
    private String tempAk;
    //临时AccessKeySecret
    private String tempSK;
    //securityToken
    private String securityToken;

    /**
     * 获取阿里云访问token
     */
    private void getOssInfo() {
        GetOssInfoService service = new GetOssInfoService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetOssInfoResponse>() {
            @Override
            public void onGetData(GetOssInfoResponse data) {
                uploadQuestionImages(data.data.ossToken, data.data.ossConfig);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                progressDialog.dismiss();
                UIUtils.showToast(RecordQuActivity.this, "上传图片失败，请重试");
            }
        });
        service.postLogined("", true);
    }

    /**
     * 上传题目图片
     */
    private void uploadQuestionImages(OssTokenBean ossTokenBean, OssConfigBean ossConfigBean) {
        if (questionIMGs.size() > 0) {
            AsyncTask<List<String>, Integer, Boolean> uploadQuImgTask = new AsyncTask<List<String>, Integer, Boolean>() {
                @Override
                protected Boolean doInBackground(List<String>... imgs) {
                    List<MultiImageUploadManager.UploadInstance> instances = new ArrayList<>();
                    for (String imgPath : imgs[0]) {
                        MultiImageUploadManager.UploadInstance instance = MultiImageUploadManager.createUploadInstance();
                        instance.path = imgPath;
                        instances.add(instance);
                    }
                    MultiImageUploadManager manager = new MultiImageUploadManager(instances, RecordQuActivity.this);

                    manager.init(ossTokenBean, ossConfigBean);
                    manager.setListener(new MultiImageUploadManager.UploadResultListener() {
                        @Override
                        public void onUploadSuccess(final List<MultiImageUploadManager.ImageInfo>
                                                            urls) {
                            RecordQuActivity.this.runOnUiThread(() -> {
                                        recordQuBean.setQuestionImg(JSON.toJSONString(urls));
                                        uploadAnalysisImages(ossTokenBean, ossConfigBean);
                                    }
                            );
                        }

                        @Override
                        public int onUploadProgress() {
                            return 0;
                        }

                        @Override
                        public void onUploadFail() {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                    });
                    manager.startUpload();
                    return null;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }
            };
            uploadQuImgTask.execute(questionIMGs);
        } else {
            uploadAnalysisImages(ossTokenBean, ossConfigBean);
        }
    }

    /**
     * 上传解析图片
     */
    private void uploadAnalysisImages(OssTokenBean ossTokenBean, OssConfigBean ossConfigBean) {
        if (answerIMGs.size() > 0) {
            AsyncTask<List<String>, Integer, Boolean> uploadAnaImgTask = new AsyncTask<List<String>, Integer, Boolean>() {
                @Override
                protected Boolean doInBackground(List<String>... imgs) {
                    List<MultiImageUploadManager.UploadInstance> instances = new ArrayList<>();
                    for (String imgPath : imgs[0]) {
                        MultiImageUploadManager.UploadInstance instance = MultiImageUploadManager.createUploadInstance();
                        instance.path = imgPath;
                        instances.add(instance);
                    }
                    MultiImageUploadManager manager = new MultiImageUploadManager(instances, RecordQuActivity.this);

                    manager.init(ossTokenBean, ossConfigBean);
                    manager.setListener(new MultiImageUploadManager.UploadResultListener() {
                        @Override
                        public void onUploadSuccess(final List<MultiImageUploadManager.ImageInfo>
                                                            urls) {
                            RecordQuActivity.this.runOnUiThread(() -> {
                                        recordQuBean.setAnalysisImg(JSON.toJSONString(urls));
                                        if (progressDialog != null) {
                                            progressDialog.dismiss();
                                        }
                                        UIUtils.showToast(RecordQuActivity.this, "上传成功");
                                        Intent intent = new Intent(RecordQuActivity.this, RecordQu2Activity.class);
                                        intent.putExtra("subjectId", subjectId);
                                        intent.putExtra("recordQuData", recordQuBean);
                                        startActivity(intent);
                                    }
                            );
                        }

                        @Override
                        public int onUploadProgress() {
                            return 0;
                        }

                        @Override
                        public void onUploadFail() {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            UIUtils.showToast(RecordQuActivity.this, "上传失败，请重试！");
                        }
                    });
                    manager.startUpload();
                    return null;
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    super.onPostExecute(aBoolean);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }
            };
            uploadAnaImgTask.execute(answerIMGs);
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Intent intent = new Intent(RecordQuActivity.this, RecordQu2Activity.class);
            intent.putExtra("recordQuData", recordQuBean);
            intent.putExtra("subjectId", subjectId);
            startActivity(intent);
        }
    }

    private static class IMGAdapter extends RecyclerView.Adapter<IMGViewHolder> {
        private List<String> nbData;
        private Context mContext;
        private int type;

        public IMGAdapter(List<String> nbData, Context context, int type) {
            this.nbData = nbData;
            this.mContext = context;
            this.type = type;
        }

        @Override
        public IMGViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_record_img_item, parent, false);
            return new IMGViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(IMGViewHolder holder, int position) {
            Glide.with(mContext).load(new File(nbData.get(position))).into(holder.img);
            holder.img.setOnClickListener(v -> {
                Intent intent = new Intent(mContext, EditImageActivity.class);
                intent.putStringArrayListExtra(EXTRA_PICTURE_PATHS, (ArrayList<String>) nbData);
                intent.putExtra(EXTRA_PICTURE_POSITION, position);
                intent.putExtra("type", type);
                ((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_EDIT_PICTURE);
            });
        }

        @Override
        public int getItemCount() {
            return nbData.size();
        }
    }

    private static class IMGViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;

        public IMGViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.iv_img);
        }
    }

    /**
     * Android6.0需要申请权限
     */
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final static int CAMERA_REQUEST_CODE = 1;

    /**
     * Android6.0权限申请
     */
    private void permissionRequest(int type) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                // 申请权限
                ActivityCompat.requestPermissions(this, DANGEROUS_PERMISSION,
                        CAMERA_REQUEST_CODE);
            } else {
                // 权限已经授予,直接初始化
                getPicture(type);
            }
        } else {
            getPicture(type);
        }
    }

    /**
     * 获取图片->拍照或从相册选择
     *
     * @param type 问题或者答案
     */
    private void getPicture(int type) {
        final ImageUploadDialog dialog = new ImageUploadDialog(this, R.style.bottomOutStyle);
        dialog.setOnBack(btn -> {
            dialog.dismiss();
            Intent intent;
            if (R.id.btn_select_picture == btn) {
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                if (null == intent.resolveActivity(getPackageManager())) {
                    UIUtils.showToast(RecordQuActivity.this, "没有找到可供选择图片的软件");
                    return;
                }
                int code = REQUEST_CODE_PICK_QUESTION_IMG;
                if (type == TYPE_GET_ANSWER_IMG) {
                    code = REQUEST_CODE_PICK_ANSWER_IMG;
                }
                startActivityForResult(intent, code);
            } else {
                int code = REQUEST_CODE_TAKE_QUESTION_IMG;
                if (type == TYPE_GET_ANSWER_IMG) {
                    code = REQUEST_CODE_TAKE_ANSWER_IMG;
                }
                intent = new Intent(RecordQuActivity.this, PictureTakeActivity.class);
                startActivityForResult(intent, code);
            }
        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_TAKE_QUESTION_IMG) {
            Uri uri = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
            String imgPath = uri.getPath();
            questionIMGs.add(imgPath);
            if (questionIMGs.size() >= 2) {
                questionWrapper.removeFootView(0);
                UIUtils.showToast(RecordQuActivity.this, "题目最多上传2张图片");
            }
            questionWrapper.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_TAKE_ANSWER_IMG) {
            Uri uri = data.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
            String imgPath = uri.getPath();
            answerIMGs.add(imgPath);
            if (answerIMGs.size() >= 9) {
                answerWrapper.removeFootView(0);
                UIUtils.showToast(RecordQuActivity.this, "解析最多上传9张图片");
            }
            answerWrapper.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_PICK_QUESTION_IMG) {
            Uri uri = data.getData();
            if (uri != null) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String cropImgName = timeStamp + "_img_crop.jpg";
                Uri outputUri = Uri.fromFile(new File(getFilesDir().getAbsolutePath() + File.separator + cropImgName));
                Crop.of(uri, outputUri).start(RecordQuActivity.this, REQUEST_CODE_TAKE_QUESTION_IMG);
            }
        } else if (requestCode == REQUEST_CODE_PICK_ANSWER_IMG) {
            Uri uri = data.getData();
            if (uri != null) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String cropImgName = timeStamp + "_img_crop.jpg";
                Uri outputUri = Uri.fromFile(new File(getFilesDir().getAbsolutePath() + File.separator + cropImgName));
                Crop.of(uri, outputUri).start(RecordQuActivity.this, REQUEST_CODE_TAKE_ANSWER_IMG);
            }
        } else if (requestCode == REQUEST_CODE_EDIT_PICTURE) {
            ArrayList<String> newlist = data.getStringArrayListExtra(EXTRA_PICTURE_PATHS);
            int type = data.getIntExtra("type", -1);
            if (type == 1) {
                questionIMGs.clear();
                questionIMGs.addAll(newlist);
                questionWrapper.notifyDataSetChanged();
            } else if (type == 2) {
                answerIMGs.clear();
                answerIMGs.addAll(newlist);
                answerWrapper.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
