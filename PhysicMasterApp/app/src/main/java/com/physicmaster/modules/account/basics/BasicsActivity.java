package com.physicmaster.modules.account.basics;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.MainActivity;
import com.physicmaster.common.CircleImageDrawable;
import com.physicmaster.common.GetPictrueProcess;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.bean.GuideFinishBean;
import com.physicmaster.net.response.account.GetOssInfoResponse;
import com.physicmaster.net.response.account.GetOssInfoResponse.DataBean.OssConfigBean;
import com.physicmaster.net.response.account.GetOssInfoResponse.DataBean.OssTokenBean;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.GetOssInfoService;
import com.physicmaster.net.service.user.GuideFinishService;
import com.physicmaster.utils.OssUtils;
import com.physicmaster.utils.StringUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ImageUploadDialog;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.RoundImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicsActivity extends BaseActivity {

    private EditText etUser;
    private RoundImageView ivHeader;
    private GetPictrueProcess avatorProcess;
    private String mNickName;
    private int mPetId;

    /**
     * Android6.0需要申请权限
     */
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private final static int CAMERA_REQUEST_CODE = 1;
    private Button btnNext;

    @Override
    protected void findViewById() {
        ivHeader = (RoundImageView) findViewById(R.id.iv_header);
        etUser = (EditText) findViewById(R.id.et_user);
        btnNext = (Button) findViewById(R.id.btn_next);

        btnNext.setOnClickListener(v -> submitInfo());
    }

    private void guideFinish() {
        GuideFinishBean bean = new GuideFinishBean(mNickName, mPetId, headPath);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        final GuideFinishService service = new GuideFinishService(BasicsActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                UIUtils.showToast(BasicsActivity.this, "设置完成");
                CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, data.data.loginVo);
                BaseApplication.setUserData(data.data.loginVo);
                Intent intent = new Intent(BasicsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                System.gc();
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "设置失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(BasicsActivity.this, errorMsg);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(() -> service.cancel());
        service.postLogined(bean.toString(), false);
    }

    private void submitInfo() {
        mNickName = etUser.getText().toString().trim();
        if (StringUtils.isEmpty(mNickName)) {
            UIUtils.showToast(BasicsActivity.this, "请填写您的尊姓大名");
            return;
        }
        Pattern p = Pattern.compile("^[\\u4E00-\\u9FA5A-Za-z0-9_]+$");
        Matcher m = p.matcher(mNickName);
        if (!m.matches()) {
            UIUtils.showToast(this, "不可以包含特殊符号哦！");
            return;
        }
        hideInputSoft(this, etUser);
        guideFinish();
    }

    @Override
    protected void initView() {
        mPetId = getIntent().getIntExtra("petId", -1);
        ivHeader.setOnClickListener(view -> permissionRequest());
        UserDataResponse.UserDataBean.LoginVoBean mDataBean = BaseApplication.getUserData();
        if (mDataBean != null) {
            Glide.with(this).load(mDataBean.portrait).placeholder(R.drawable.placeholder_gray)
                    .into(ivHeader);
            etUser.setText(mDataBean.nickname);
        }
        avatorProcess = new GetPictrueProcess(this);
    }

    private void pickAvator() {
        final ImageUploadDialog dialog = new ImageUploadDialog(this, R.style.bottomOutStyle);
        dialog.setOnBack(btn -> {
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
        });
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == GetPictrueProcess.REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {
                Intent intent = avatorProcess.cropAvatar(data.getData());
                startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER);
            }
        } else if (requestCode == GetPictrueProcess.REQUEST_CODE_TAKE_PICTURE) {
            Intent intent = avatorProcess.cropAvatar(null);
            startActivityForResult(intent, GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER);
        } else if (requestCode == GetPictrueProcess.REQUEST_CODE_IMAGE_CROPPER) {
            getOssInfo();
            Bitmap bitmap = avatorProcess.decodeUriAsBitmap();
            if (null != bitmap) {
                CircleImageDrawable drawable = new CircleImageDrawable(bitmap);
                ivHeader.setImageDrawable(drawable);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_basics;
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
                    Log.i("result", "成功获得授权");
                    pickAvator();
                } else {
                    Log.i("result", "未获得授权");
                    // 三方处理自己逻辑,这里只做测试用
                    UIUtils.showToast(BasicsActivity.this, "您拒绝了系统权限，无法上传头像");
                }
            } else {
                UIUtils.showToast(BasicsActivity.this, "获取系统权限异常");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /*************************************
     * 阿里云oss开始
     ********************************/
    //保存上传成功的头像路径
    private String headPath;

    /**
     * 获取阿里云访问token
     */
    private void getOssInfo() {
        GetOssInfoService service = new GetOssInfoService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetOssInfoResponse>() {
            @Override
            public void onGetData(GetOssInfoResponse data) {
                try {
                    startUpload(data.data.ossToken, data.data.ossConfig);
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

    /**
     * 开始上传
     */
    private void startUpload(OssTokenBean ossTokenBean, OssConfigBean ossConfigBean) {
        upload(ossTokenBean, ossConfigBean);
    }

    /**
     * 上传图片
     *
     * @param ossTokenBean
     * @param ossConfigBean
     */
    private void upload(OssTokenBean ossTokenBean, OssConfigBean ossConfigBean) {
        String path = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            path = avatorProcess.getCropAvatarPath();
        } else {
            path = avatorProcess.getAfterCropImageUri().getPath().toString();
        }
        final String localPath = path;
        OssUtils.initOss(getApplicationContext(), ossTokenBean.tempAk, ossTokenBean.tempSk, ossConfigBean.hostId, ossTokenBean.securityToken);
        UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication.getUserData();
        if (loginVoBean == null) {
            gotoLoginActivity();
            return;
        }
        final String avatorName = loginVoBean.dtUserId + System.currentTimeMillis() + ".jpeg";
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        loadingDialog.showDialog(() -> {

        });
        new Thread(() -> OssUtils.uploadFile(ossConfigBean.bucketName, ossConfigBean.imgPath + avatorName, localPath, new
                OssUtils.OnUploadListener() {

                    @Override
                    public void onUploadSucc(final String path1) {
                        runOnUiThread(() -> {
                            loadingDialog.dismissDialog();
                            UIUtils.showToast(BasicsActivity.this, "上传成功！");
                            headPath = ossConfigBean.imgPath + avatorName;
                            final Bitmap bitmap = avatorProcess.decodeUriAsBitmap();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (null != bitmap) {
                                        CircleImageDrawable drawable = new
                                                CircleImageDrawable
                                                (bitmap);
                                        ivHeader.setImageDrawable(drawable);
                                    }
                                }
                            });
                        });
                    }

                    @Override
                    public void onUploadFail(String msg) {
                        runOnUiThread(() -> {
                            loadingDialog.dismissDialog();
                            UIUtils.showToast(BasicsActivity.this, "上传失败!");
                        });

                    }

                    @Override
                    public void onUploadProgress(PutObjectRequest request, long currentSize,
                                                 long totalSize) {

                    }
                })).start();
    }
    /************************************阿里云oss结束**********************************/
}
