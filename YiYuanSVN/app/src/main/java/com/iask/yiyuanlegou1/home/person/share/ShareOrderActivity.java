package com.iask.yiyuanlegou1.home.person.share;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.common.Constant;
import com.iask.yiyuanlegou1.common.MultiImageUploadManager;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.home.share.ShareDetailActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.CommonResponse;
import com.iask.yiyuanlegou1.network.respose.account.AliyunServerBean;
import com.iask.yiyuanlegou1.network.respose.account.OssTokenResponse;
import com.iask.yiyuanlegou1.network.service.account.OssTokenService;
import com.iask.yiyuanlegou1.network.service.product.AddShareService;
import com.iask.yiyuanlegou1.utils.BitmapUtils;
import com.iask.yiyuanlegou1.utils.GetPictrueProcess;
import com.iask.yiyuanlegou1.utils.OssUtils;
import com.iask.yiyuanlegou1.utils.ScreenUtils;
import com.iask.yiyuanlegou1.utils.UIUtils;
import com.iask.yiyuanlegou1.widget.HorizontalListView;
import com.iask.yiyuanlegou1.widget.ImageUploadDialog;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ShareOrderActivity extends BaseActivity {
    private TitleBarView title;
    private int itemId;
    private int qishu;
    private int sid;
    private HorizontalListView horizontalListView;
    private Button btnAddPic, btnPublish;
    private ImageAdapter adapter;
    private ProgressDialog progressDialog;
    /**
     * 缓存大小：单位-M
     */
    private int cacheSize = 5;
    /**
     * 处理图片选取、拍摄、裁剪、存储等任务
     */
    private GetPictrueProcess avatorProcess;
    //临时AccessKeyId
    private String tempAk;
    //临时AccessKeySecret
    private String tempSK;
    //securityToken
    private String securityToken;

    private Handler handler = new Handler();

    private LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(cacheSize * 1024 *
            1024);

    private ArrayList<String> paths = new ArrayList<String>();

    public static final String EXTRA_PICTURE_PATHS = "EXTRA_PICTURE_PATHS";
    public static final String EXTRA_PICTURE_POSITION = "EXTRA_PICTURE_POSITION";
    public static int REQUEST_CODE_EDIT_PICTURE = 0x104;

    protected void findViewById() {
    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setTitleText(R.string.publish_share);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareOrderActivity.this.finish();
            }
        });
        btnAddPic = (Button) findViewById(R.id.btn_add_picture);
        btnAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickAvator();
            }
        });
        btnPublish = (Button) findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doPublish();
            }
        });
        horizontalListView = (HorizontalListView) findViewById(R.id.horizontal_listview);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                onBitmapClicked(position);
            }
        });
        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId", 0);
        qishu = intent.getIntExtra("qishu", 0);
        sid = intent.getIntExtra("sid", 0);
        adapter = new ImageAdapter(paths, this);
        horizontalListView.setAdapter(adapter);
        avatorProcess = new GetPictrueProcess(this);
        getOssToken();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_share_order;
    }

    private void doPublish() {
        EditText etTitle = (EditText) findViewById(R.id.et_title);
        final String title = etTitle.getText().toString();
        if (TextUtils.isEmpty(title)) {
            UIUtils.showToast(this, "标题不能为空！");
            return;
        }
        EditText etContent = (EditText) findViewById(R.id.et_content);
        final String content = etContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            UIUtils.showToast(this, "内容不能为空！");
            return;
        }
        if (paths.size() == 0) {
            UIUtils.showToast(this, "还没上传图片呢！");
            return;
        }
        if (TextUtils.isEmpty(tempAk)) {
            UIUtils.showToast(this, "上传服务暂不用，请退出重试！");
            return;
        }
        progressDialog = ProgressDialog.show(this, "正在上传图片", "请等候……", true, false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MultiImageUploadManager.UploadInstance> instances = new ArrayList
                        <MultiImageUploadManager.UploadInstance>();
                for (int i = 0; i < paths.size(); i++) {
                    MultiImageUploadManager.UploadInstance instance = MultiImageUploadManager
                            .createUploadInstance();
                    instance.path = paths.get(i);
                    instances.add(instance);
                }
                MultiImageUploadManager manager = new MultiImageUploadManager(instances,
                        ShareOrderActivity.this);

                manager.init(tempAk, tempSK, securityToken);
                manager.setListener(new MultiImageUploadManager.UploadResultListener() {
                    @Override
                    public void onUploadSuccess(final List<String> urls) {
                        progressDialog.dismiss();
                        ShareOrderActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UIUtils.showToast(ShareOrderActivity.this, "上传成功！");
                                notifyServer(urls, title, content);
                            }
                        });
                    }

                    @Override
                    public int onUploadProgress() {
                        return 0;
                    }

                    @Override
                    public void onUploadFail() {
                        progressDialog.dismiss();
                    }
                });
                manager.startUpload();
            }
        }).start();
    }

    /**
     * 同步到服务端
     *
     * @param urls
     */
    private void notifyServer(List<String> urls, String title, String content) {
        AddShareService service = new AddShareService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(final CommonResponse data) {
                try {
                    ShareOrderActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UIUtils.showToast(ShareOrderActivity.this, data.getMsg());
                            LocalBroadcastManager.getInstance(ShareOrderActivity.this)
                                    .sendBroadcast(new Intent(MyUnShareFragment
                                            .SHARE_ORDER_REFRESH));
                            ShareOrderActivity.this.finish();
                        }
                    });
                } catch (Exception e) {
                }
            }

            @Override
            public void onGetError(int errorCode, final String errorMsg, Throwable error) {
                try {
                    ShareOrderActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            UIUtils.showToast(ShareOrderActivity.this, errorMsg);
                        }
                    });
                } catch (Exception e) {
                }
            }
        });
        StringBuilder builder = new StringBuilder();
        builder.append("itemId=" + itemId);
        builder.append("&qishu=" + qishu);
        try {
            title = URLEncoder.encode(title, Constant.CHARACTER_ENCODING);
            builder.append("&sdtitle=" + title);
            content = URLEncoder.encode(content, Constant.CHARACTER_ENCODING);
            builder.append("&sdcontent=" + content);
            builder.append("&sid=" + sid);
            builder.append("&sdphotolist=" + JSON.toJSON(urls));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.post(builder.toString(), true);
    }

    /**
     * 获取阿里云访问token
     */
    private void getOssToken() {
        OssTokenService service = new OssTokenService(this);
        service.setCallback(new IOpenApiDataServiceCallback<OssTokenResponse>() {
            @Override
            public void onGetData(OssTokenResponse data) {
                try {
                    tempAk = data.data.ossToken.tempAk;
                    tempSK = data.data.ossToken.tempSk;
                    securityToken = data.data.ossToken.securityToken;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(ShareOrderActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("", true);
    }

    private void startUpload() {
        String localPath = avatorProcess.getAfterCropImageUri().getPath().toString();
        Object object = CacheManager.getObject(CacheManager
                .TYPE_USER_INFO, CacheKeys.OSS_SERVER_INFO, AliyunServerBean.class);
        if (object == null) {
            Toast.makeText(ShareOrderActivity.this, "服务暂不可使用！", Toast.LENGTH_SHORT).show();
            return;
        }
        final AliyunServerBean bean = (AliyunServerBean) object;
        OssUtils.initOss(getApplicationContext(), tempAk, tempSK, bean.getCdnName(), securityToken);
        final String avatorName = System.currentTimeMillis() + "";
        OssUtils.uploadFile(bean.getBucketName(), bean.getImgPath() + avatorName, localPath, new
                OssUtils.OnUploadListener() {

                    @Override
                    public void onUploadSucc(final String path) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShareOrderActivity.this, "上传成功！", Toast.LENGTH_SHORT)
                                        .show();
//                                notifyServer(bean.getCdnName() + bean.getImgPath() + avatorName);
                            }
                        });
                    }

                    @Override
                    public void onUploadFail(String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ShareOrderActivity.this, "上传失败！", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });

                    }

                    @Override
                    public void onUploadProgress(PutObjectRequest request, long currentSize, long
                            totalSize) {

                    }
                });
    }

    private void pickAvator() {
        final ImageUploadDialog dialog = new ImageUploadDialog(this, R.style.CustomStyle);
        avatorProcess.setPicUri();
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
                    intent = avatorProcess.takePicture(null);
                    startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_TAKE_PICTURE);
                }
            }
        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == GetPictrueProcess.REQUEST_CODE_PICK_IMAGE) {
//            Intent intent = avatorProcess.cropAvatar(data.getData());
//            startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER);
            String localPath = BitmapUtils.getRealFilePath(this, data.getData());
            paths.add(localPath);
            adapter.notifyDataSetChanged();
        } else if (requestCode == GetPictrueProcess.REQUEST_CODE_TAKE_PICTURE) {
//            Intent intent = avatorProcess.cropAvatar(null);
//            startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER);
            String localPath = avatorProcess.getBeforCropImageUri().getPath().toString();
            paths.add(localPath);
            adapter.notifyDataSetChanged();
        } else if (requestCode == REQUEST_CODE_EDIT_PICTURE) {
            ArrayList<String> newlist = data.getStringArrayListExtra(EXTRA_PICTURE_PATHS);
            updateAnswerPictures(newlist);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        adapter.notifyDataSetChanged();
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
            params.width = (int) (ScreenUtils.getScreendensity(mContext) * 58);
            params.height = (int) (ScreenUtils.getScreendensity(mContext) * 58);
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
}
