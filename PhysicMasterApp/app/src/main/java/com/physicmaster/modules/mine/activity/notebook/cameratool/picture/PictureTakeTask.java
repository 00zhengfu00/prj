package com.physicmaster.modules.mine.activity.notebook.cameratool.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.utils.BitmapUtils;
import com.physicmaster.utils.ScreenUtils;
import com.physicmaster.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PictureTakeTask extends AsyncTask<String, Void, Boolean> {
    private static final String TAG = "PictureTakeTask";
    private Camera mCamera;
    private Context mContext;
    private OnPictureGetListener mListener;

    public PictureTakeTask(Context context, Camera camera, OnPictureGetListener listener) {
        super();
        // TODO Auto-generated constructor stub
        mCamera = camera;
        mContext = context;
        mListener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        // TODO Auto-generated method stub
        takePicture();
        return null;
    }


    private void takePicture() {
        try {
            mCamera.takePicture(null, null, mPicture);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }


    private PictureCallback mPicture = (data, camera) -> scan2Media(data);

    private void scan2Media(final byte[] data) {
        new Thread(() -> {
            BitmapFactory.Options option = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, option);
            bitmap = BitmapUtils.centerCutBitmap(bitmap, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] tmpdata = baos.toByteArray();
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.d("PictureCallBack", "Error creating media file, check storage permissions: ");
                return;
            }
            String fileName = pictureFile.toString();
            //rotate picture
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(tmpdata);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("PictureCallBack", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("PictureCallBack", "Error accessing file: " + e.getMessage());
            }

            Log.d("PictureCallBack", "onPictureTaken saved of file:" + pictureFile.getPath());

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(mContext,
                    new String[]{fileName}, null,
                    (path, uri) -> {
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);
                    });

            if (null != mListener) {
                mListener.onPictureGetted(pictureFile.toString());
            }
        }).start();
    }


    /**
     * Create a File for saving an image
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        String appName = Utils.getAppName(mContext);
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appName);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("lswuyou", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }

    interface OnPictureGetListener {
        public void onPictureGetted(String fileName);
    }

}
