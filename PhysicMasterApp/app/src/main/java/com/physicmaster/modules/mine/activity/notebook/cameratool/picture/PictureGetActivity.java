package com.physicmaster.modules.mine.activity.notebook.cameratool.picture;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.cropview.Crop;

import java.io.File;

public class PictureGetActivity extends BaseActivity implements PictureTakeTask.OnPictureGetListener {

    private Camera mCamera = null;
    private CameraPreview mPreview;
    private FrameLayout mPreviewLayout;
    private Context mContext;
    private boolean mBLightOn = false;

    public static final int REQUEST_CODE_PICK_IMAGE = 0x11;
    public static final String EXTRA_PICTURE_FILE_PATH = "EXTRA_PICTURE_FILE_PATH";

    @Override
    protected void findViewById() {
    }

    @Override
    protected void initView() {
        initCamera();
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //获得当前窗体对象
        Window window = getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);

        mContext = this;

        ImageButton btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> {
            UIUtils.showToast(this, "test");
            Log.d("PictureGetActivity", "btn_ok onClick!");
            if (null == mCamera) {
                Log.e("PictureGetActivity", "btn_ok onClick mCamera is null!");
                PictureGetActivity.this.finish();
                return;
            }
            new PictureTakeTask(mContext, mCamera, PictureGetActivity.this).execute();
        });

        ImageButton btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            releaseCamera();
            finish();
        });

        ImageButton btnAlbum = findViewById(R.id.btn_album);
        btnAlbum.setOnClickListener(v -> {
            Camera.Parameters parameters = mCamera.getParameters();
            if (false == mBLightOn) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mBLightOn = true;
            } else {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mBLightOn = false;
            }
            mCamera.setParameters(parameters);
        });
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_homework_picture_get;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    /**
     * 初始化相机
     */
    private void initCamera() {
        if (null == mCamera) {
            // Create an instance of Camera
            if (checkCameraHardware(this)) {
                mCamera = getCameraInstance();
            } else {
                Toast.makeText(this, "手机没有摄像头，请选择有摄像头的手机!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (null == mCamera) {
            return;
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        mPreviewLayout = findViewById(R.id.homework_camera_preview);
        mPreviewLayout.addView(mPreview);
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Toast.makeText(BaseApplication.getAppContext(), "没有拿到相机实例!", Toast.LENGTH_SHORT).show();
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            String path = getGalleryImagePath(data);
            returnPicture(path);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getGalleryImagePath(Intent data) {
        Uri imgUri = data.getData();
        String filePath = "";
        String type = data.getType();
        if (null == type) {
            // For getting images from gallery.
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imgUri, filePathColumn, null, null, null);
            if (null != cursor) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            } else {
                filePath = imgUri.getPath();
            }

        } else if ("image/jpeg".equals(type)) {
            //小米手机
            filePath = imgUri.getPath();
        }
        return filePath;
    }

    @Override
    public void onPictureGetted(String filePath) {
//        Intent intent = new Intent(PictureGetActivity.this, PictureCropActivity.class);
//        intent.putExtra(PictureCropActivity.EXTRA_ORIGIN_FILE_PATH, filePath);
//        startActivity(intent);
        Uri inputUri = Uri.fromFile(new File(filePath));
        Uri outputUri = Uri.fromFile(new File(getCacheDir().getAbsolutePath() + File.separator + "IMG.jpg"));
        Crop.of(inputUri, outputUri).asSquare().start(this);
    }

    private void returnPicture(String picFile) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PICTURE_FILE_PATH, picFile);
        setResult(RESULT_OK, intent);
        finish();
    }
}
