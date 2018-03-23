package com.physicmaster.modules.mine.activity.notebook.cameratool.picture;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.physicmaster.base.BaseApplication;
import com.physicmaster.utils.ScreenUtils;

import java.io.IOException;
import java.util.List;

/**
 * A basic Camera preview class
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private static final String TAG = "CameraPreview";

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here
        Camera.Parameters parameters = mCamera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            parameters.set("orientation", "portrait");
            mCamera.setDisplayOrientation(90);
        } else {
            parameters.set("orientation", "landscape");
            mCamera.setDisplayOrientation(0);
        }

        try {
            mCamera.setParameters(parameters);
        } catch (RuntimeException e) {
            // TODO: handle exception
            Log.e(TAG, "Error mCamera setParameters: " + e.getMessage());
        }


        //set twice, to avoid any parameter not support
        //set camera picture size and preview size
        parameters = mCamera.getParameters();

        //set picture size
        Size picSize = choosePictureSize(parameters);
        if (null != picSize) {
            parameters.setPictureSize(picSize.width, picSize.height);
        }

        //set preview size
        Size previewSize = choosePreviewSize(parameters, picSize);
        if (null != previewSize) {
            parameters.setPreviewSize(previewSize.width, previewSize.height);
        }

        try {
            mCamera.setParameters(parameters);
        } catch (RuntimeException e) {
            Log.e(TAG, "Error mCamera setParameters: " + e.getMessage());
        }

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    /**
     * 选择展示出来的尺寸
     *
     * @param parameters
     * @param picSize
     * @return
     */
    private Size choosePreviewSize(Camera.Parameters parameters, Size picSize) {
        List<Size> sizePreviewList = parameters.getSupportedPreviewSizes();
        for (Size tmp : sizePreviewList) {
            if (tmp.height == picSize.height && tmp.width == picSize.width) {
                return picSize;
            }
        }
        return null;
    }

    /**
     * 选择最合适本机分辨率的相机拍摄尺寸
     *
     * @param parameters
     * @return
     */
    private Size choosePictureSize(Camera.Parameters parameters) {
        boolean bRotate = BaseApplication.getAppContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE;
        int screenWidth = ScreenUtils.getScreenWidth();
        //最终选择的图片尺寸
        Size sizeChoosen = null;
        List<Size> sizeList = parameters.getSupportedPictureSizes();
        //将sizeList从小到大排序
        int diff = Integer.MAX_VALUE;
        if (false == bRotate) {
            //横屏
            for (Size tmp : sizeList) {
                int tempDiff = Math.abs(tmp.width - screenWidth);
                if (tempDiff < diff) {
                    diff = tempDiff;
                    sizeChoosen = tmp;
                }
            }
        } else {
            //竖屏
            for (Size tmp : sizeList) {
                int tempDiff = Math.abs(tmp.height - screenWidth);
                if (tempDiff < diff) {
                    diff = tempDiff;
                    sizeChoosen = tmp;
                }
            }
        }
        return sizeChoosen;
    }
}
