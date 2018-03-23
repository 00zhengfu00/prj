/* 
 * 系统名称：lswuyou
 * 类  名  称：AvatorProcess.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-10-9 下午1:49:38
 * 功能说明： 处理用户更换头像
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.common.StorageConfigure;

import java.io.File;
import java.io.FileNotFoundException;

public class GetPictrueProcess {
    private Activity mContext;
    /**
     * 裁剪后的头像Uri
     */
    private Uri afterCropImageUri;
    /**
     * 裁剪前的图片Uri
     */
    private Uri beforCropImageUri;
    /**
     * 正方形头像的尺寸 (dp)
     */
    private static final int sizePx = 200;
    /**
     * 选择图片请求
     */
    public static final int REQUEST_CODE_PICK_IMAGE = 0x101;
    /**
     * 裁剪图片请求
     */
    public static final int REQUEST_CODE_IMAGE_CROPPER = 0x102;
    /**
     * 拍摄图片请求
     */
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x103;

    public static int PICTURE_QUESTION_WIDTH = 1024;

    public GetPictrueProcess(Activity context) {
        mContext = context;
        init(context);
    }

    /**
     * 图片裁剪初始化
     */
    private void init(Context context) {
        getAvatorStorageUri();
    }

    /**
     * 调用系统拍摄图片的功能
     */
    public Intent takePicture(Uri uri) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (null == uri) {
            uri = beforCropImageUri;
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        return intent;
    }

    /**
     * 调用系统选择图片的功能
     */
    public Intent pickPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 调用系统裁剪的图片
     */
    public Intent cropAvatar(Uri uri) {
        if (uri != null) {
            beforCropImageUri = uri;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(beforCropImageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", sizePx);
        intent.putExtra("outputY", sizePx);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, afterCropImageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false); // no face detection
        return intent;
    }


    /**
     * 根据裁剪后的图片Uri地址转化为Bitmap
     */
    public Bitmap decodeUriAsBitmap() {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream
                    (afterCropImageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 获得头像存储的Uri
     */
    public void getAvatorStorageUri() {
        String avatorPath = StorageConfigure.getPicFilsPath();
        afterCropImageUri = Uri.fromFile(new File(avatorPath + "avatar1.jpg"));
        beforCropImageUri = Uri.fromFile(new File(avatorPath + "avatar2.jpg"));

    }

    public void setPicUri() {
        String avatorPath = StorageConfigure.getPicFilsPath();
        long name = System.currentTimeMillis();
        afterCropImageUri = Uri.fromFile(new File(avatorPath + name + "pic1.jpg"));
        beforCropImageUri = Uri.fromFile(new File(avatorPath + name + "pic2.jpg"));
    }

    public Uri getAfterCropImageUri() {
        return afterCropImageUri;
    }

    public void setAfterCropImageUri(Uri afterCropImageUri) {
        this.afterCropImageUri = afterCropImageUri;
    }

    public Uri getBeforCropImageUri() {
        return beforCropImageUri;
    }

    public void setBeforCropImageUri(Uri beforCropImageUri) {
        this.beforCropImageUri = beforCropImageUri;
    }

    /**
     * 根据路径获取图片
     */
    public Bitmap getBitmap(String path) {
        Bitmap bmap = BitmapUtils.decodeBitmapFromSd2(path, (int) (BaseApplication.getScreenWidth
                () * 0.2), (int) (BaseApplication.getScreenWidth() * 0.2));
        return bmap;
    }

}
