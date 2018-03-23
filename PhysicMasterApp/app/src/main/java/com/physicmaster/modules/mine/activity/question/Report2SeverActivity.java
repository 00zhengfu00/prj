package com.physicmaster.modules.mine.activity.question;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.common.Constant;
import com.physicmaster.common.GetPictrueProcess;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.discuss.activity.EditImageActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.CommonResponse;
import com.physicmaster.net.response.account.GetAliyunServerInfoResponse;
import com.physicmaster.net.response.account.GetOssInfoResponse;
import com.physicmaster.net.response.account.GetOssTokenResponse;
import com.physicmaster.net.service.account.GetAliyunServerInfoService;
import com.physicmaster.net.service.account.GetOssInfoService;
import com.physicmaster.net.service.discuss.ReportService;
import com.physicmaster.utils.BitmapUtils;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ImageUploadDialog;
import com.physicmaster.widget.TitleBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class Report2SeverActivity extends BaseActivity {
    private ProgressDialog progressDialog;
    private Logger logger = AndroidLogger.getLogger();
    /**
     * 缓存大小：单位-M
     */
    private int cacheSize = 5;

    private Handler handler = new Handler();

    private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(cacheSize * 1024 *
            1024);

    private ArrayList<String> paths = new ArrayList<String>();
    private PhotoAdapter photoAdapter;

    private RecyclerView recyclerView;

    public static final String EXTRA_PICTURE_PATHS = "EXTRA_PICTURE_PATHS";
    public static final String EXTRA_PICTURE_POSITION = "EXTRA_PICTURE_POSITION";
    public static int REQUEST_CODE_EDIT_PICTURE = 0x104;
    private int subjectType;
    private int eduGradeYear;

    /**
     * Android6.0需要申请权限
     */
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int CAMERA_REQUEST_CODE = 1;
    private TextView tvGradeSubject;
    private Button btnPublish;
    private int complaintId;
    private int complaintType;
    private int reasonType;
    private TitleBuilder titleBuilder;
    private EditText etContent;

    protected void findViewById() {
        etContent = (EditText) findViewById(R.id.et_content);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnPublish = (Button) findViewById(R.id.btn_publish);
        initTitle();
        complaintId = getIntent().getIntExtra("complaintId", -1);
        complaintType = getIntent().getIntExtra("complaintType", -1);
        reasonType = getIntent().getIntExtra("reasonType", -1);
        String text = "举报";
        if (reasonType == 0) {
            text = "欺诈";
        } else if (reasonType == 1) {
            text = "色情";
        } else if (reasonType == 2) {
            text = "不实消息";
        } else if (reasonType == 3) {
            text = "违法犯罪";
        } else if (reasonType == 4) {
            text = "骚扰";
        } else if (reasonType == 10) {
            text = "其他";

        }
        titleBuilder.setMiddleTitleText(text);
    }

    @Override
    protected void initView() {
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPublish();
            }
        });

//        photoAdapter = new PhotoAdapter(this, paths);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper
//                .VERTICAL));
//        recyclerView.setAdapter(photoAdapter);
//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
//                new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
//                            PhotoPicker.builder()
//                                    .setPhotoCount(PhotoAdapter.MAX)
//                                    .setShowCamera(true)
//                                    .setPreviewEnabled(false)
//                                    .setSelected(paths)
//                                    .start(Report2SeverActivity.this);
//                        } else {
//                            PhotoPreview.builder()
//                                    .setPhotos(paths)
//                                    .setCurrentItem(position)
//                                    .start(Report2SeverActivity.this);
//                        }
//                    }
//                }));
//        avatorProcess = new GetPictrueProcess(this);
//        getOssToken();
//        if (BaseApplication.getStartupDataBean().ossConfig == null) {
//            getAliyunServerInfo();
//        }


    }


    /**
     * 发布问题
     */
    private void doPublish() {
//        EditText etTitle = (EditText) findViewById(R.id.et_title);
//        final String title = etTitle.getText().toString();
//        if (TextUtils.isEmpty(title)) {
//            UIUtils.showToast(this, "标题不能为空！");
//            return;
//        }

        final String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            UIUtils.showToast(this, "内容不能为空！");
            return;
        }
        notifyServer(content);
//        if (TextUtils.isEmpty(tempAk)) {
//            UIUtils.showToast(this, "上传服务暂不可用，请退出重试！");
//            return;
//        }
//        progressDialog = ProgressDialog.show(this, "正在发布", "请等候……", true, false);
//        if (paths.size() != 0) {
//            progressDialog.show();
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    List<MultiImageUploadManager.UploadInstance> instances = new ArrayList
//                            <MultiImageUploadManager.UploadInstance>();
//                    for (int i = 0; i < paths.size(); i++) {
//                        MultiImageUploadManager.UploadInstance instance = MultiImageUploadManager
//                                .createUploadInstance();
//                        instance.path = paths.get(i);
//                        instances.add(instance);
//                    }
//                    MultiImageUploadManager manager = new MultiImageUploadManager(instances,
//                            Report2SeverActivity.this);
//
//                    manager.init(tempAk, tempSK, securityToken);
//                    manager.setListener(new MultiImageUploadManager.UploadResultListener() {
//                        @Override
//                        public void onUploadSuccess(final List<MultiImageUploadManager.ImageInfo>
//                                                            imageInfos) {
//                            progressDialog.dismiss();
//                            Report2SeverActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    notifyServer(imageInfos, null, content);
//                                }
//                            });
//                        }
//
//                        @Override
//                        public int onUploadProgress() {
//                            return 0;
//                        }
//
//                        @Override
//                        public void onUploadFail() {
//                            progressDialog.dismiss();
//                        }
//                    });
//                    manager.startUpload();
//                }
//            }).start();
//        } else {
//        }

    }

    private void notifyServer(String content) {
        ReportService service = new ReportService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
//                progressDialog.dismiss();
                UIUtils.showToast(Report2SeverActivity.this, "举报成功");
                setResult(RESULT_OK);
                Report2SeverActivity.this.finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
//                progressDialog.dismiss();
                UIUtils.showToast(Report2SeverActivity.this, errorMsg);
            }
        });

        StringBuilder params = new StringBuilder();
        if (complaintId != -1) {
            params.append("complaintId=" + complaintId);

        }
        if (complaintType != -1) {
            params.append("&complaintType=" + complaintType);

        }
        if (reasonType != -1) {
            params.append("&reasonType=" + reasonType);

        }
        try {
            content = URLEncoder.encode(content, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.append("&remark=" + content);
        service.postLogined(params.toString(), false);
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        titleBuilder = new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText etContent = (EditText) findViewById(R.id.et_content);
                        hideInputSoft(Report2SeverActivity.this, etContent);
                        finish();
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_report2_sever;
    }

    /**
     * 更新图片路径和url列表
     */
    private void updateAnswerPictures(ArrayList<String> newlist) {
        boolean exist;
        int index = paths.size() - 1;
        while (index >= 0) {
            String tmpPath = paths.get(index);
            exist = pathExist(tmpPath, newlist);
            if (true != exist) {
                paths.remove(index);
            }
            index--;
        }
    }

    /**
     * 判读图片是否被删除
     */
    private boolean pathExist(String path, ArrayList<String> newlist) {
        int newCount = newlist.size();
        for (int i = 0; i < newCount; i++) {
            if (path.equals(newlist.get(i))) {
                return true;
            }
        }
        return false;
    }

    class ImageAdapter extends BaseAdapter {
        private List<String> path;
        private Context mContext;

        public ImageAdapter(List<String> path, Context mContext) {
            this.mContext = mContext;
            this.path = path;
        }

        @Override
        public int getCount() {
            return path.size();
        }

        @Override
        public Object getItem(int position) {
            return path.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_image_item,
                        null);
                holder.ivHomeworkImage = (ImageView) convertView.findViewById(R.id
                        .iv_homework_image);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageView iv = holder.ivHomeworkImage;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
            params.width = (int) (ScreenUtils.getScreenDensity(mContext) * 58);
            params.height = (int) (ScreenUtils.getScreenDensity(mContext) * 58);
            iv.setLayoutParams(params);
            displayBmp(iv, paths.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        ImageView ivHomeworkImage;
    }

    /**
     * 渲染图片
     */
    public void displayBmp(final ImageView iv, final String sourcePath) {
        if (iv.getTag() != null && iv.getTag().equals(sourcePath)) {
            return;
        }
        iv.setTag(sourcePath);
        Bitmap bitmap = lruCache.get(sourcePath);

        if (null != bitmap) {
            refreshView(iv, bitmap, sourcePath);
            return;
        } else {
            // 不在缓存则加载图片
            new Thread() {
                Bitmap img;

                public void run() {
                    try {
                        img = avatorProcess.getBitmap(sourcePath);
                        lruCache.put(sourcePath, img);
                    } catch (Exception e) {
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            refreshView(iv, img, sourcePath);
                        }
                    });
                }
            }.start();
        }
    }

    private void refreshView(ImageView imageView, Bitmap bitmap, String path) {
        if (imageView != null && bitmap != null) {
            if (path != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setTag(path);
            }
        }
    }

    private void onBitmapClicked(int position) {
        Intent intent = new Intent(this, EditImageActivity.class);
        intent.putStringArrayListExtra(EXTRA_PICTURE_PATHS, paths);
        intent.putExtra(EXTRA_PICTURE_POSITION, position);
        startActivityForResult(intent, REQUEST_CODE_EDIT_PICTURE);
    }

    private void pickAvator() {
        if (paths.size() >= 9) {
            UIUtils.showToast(this, "最多只能上传9张图片");
            return;
        }
        final ImageUploadDialog dialog = new ImageUploadDialog(this, R.style.bottomOutStyle);
        dialog.setOnBack(new ImageUploadDialog.OnBack() {

            @Override
            public void click(int btn) {
                dialog.dismiss();
                Intent intent;
                if (R.id.btn_select_picture == btn) {
                    intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_PICK_IMAGE);
                } else {
                    intent = avatorProcess.takePicture();
                    startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_TAKE_PICTURE);
                }
            }
        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == GetPictrueProcess.REQUEST_CODE_PICK_IMAGE) {
            String localPath = BitmapUtils.getRealFilePath(this, data.getData());
            paths.add(localPath);
        } else if (requestCode == GetPictrueProcess.REQUEST_CODE_TAKE_PICTURE) {
            String localPath = avatorProcess.getBeforCropImageUri().getPath().toString();
            paths.add(localPath);
        } else if (requestCode == REQUEST_CODE_EDIT_PICTURE) {
            ArrayList<String> newlist = data.getStringArrayListExtra(EXTRA_PICTURE_PATHS);
            updateAnswerPictures(newlist);
        } else if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview
                .REQUEST_CODE) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            paths.clear();
            if (photos != null) {
                paths.addAll(photos);
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Android6.0权限申请
     */
    private void permissionRequest() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                // 申请权限
                ActivityCompat.requestPermissions(this, DANGEROUS_PERMISSION,
                        CAMERA_REQUEST_CODE);
            } else {
                // 权限已经授予,直接初始化
                pickAvator();
            }
        } else {
            pickAvator();
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults != null) {
                // 权限授予成功,初始化
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] ==
                        PackageManager.PERMISSION_GRANTED) {
                    logger.debug("成功获得授权");
                    pickAvator();
                } else {
                    logger.debug("未获得授权");
                    // 三方处理自己逻辑,这里只做测试用
                    UIUtils.showToast(Report2SeverActivity.this, "您拒绝了系统权限，无法上传头像");
                }
            } else {
                UIUtils.showToast(Report2SeverActivity.this, "获取系统权限异常");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
    //保存上传成功的头像路径
    private String headPath;
    private GetPictrueProcess avatorProcess;

    /**
     * 获取阿里云访问token
     */
    private void getOssToken() {
        GetOssInfoService service = new GetOssInfoService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetOssInfoResponse>() {
            @Override
            public void onGetData(GetOssInfoResponse data) {
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
            }
        });
        service.postLogined("", true);
    }

}
