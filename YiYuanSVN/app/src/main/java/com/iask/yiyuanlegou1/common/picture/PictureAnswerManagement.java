/* 
 * 系统名称：lswuyou
 * 类  名  称：PictureAnswerManagement.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-10-30 下午1:54:09
 * 功能说明： 管理图片拍摄、裁剪、保存、上传
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.common.picture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.common.StorageConfigure;
import com.iask.yiyuanlegou1.utils.BitmapUtils;

public class PictureAnswerManagement {
	private String pictureDir;
	private Uri curPicturePath;
	public static final int PICTURE_QUESTION_WIDTH = BaseApplication.getScreenWidth();
	public static final int PICTURE_QUESTION_HEIGHT = BaseApplication.getScreenHeight();
	/** 裁剪图片请求 */
	public static final int REQUEST_CODE_IMAGE_CROPPER = 0x1;
	/** 拍摄图片请求 */
	public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
	/** 选择图片请求 */
	public static final int REQUEST_CODE_SELECT_PICTURE = 0x3;

	public PictureAnswerManagement() {
		pictureDir = StorageConfigure.getPicFilsPath();
	}

	/** 返回当前拍摄图片的路径，用以上传图片 */
	public String getCurPicturePath() {
		return curPicturePath.getPath().toString();
	}

	/** 调用系统拍摄图片的功能 */
	public Intent takePicture() {
		curPicturePath = Uri.fromFile(new File(pictureDir + generatePictureName()));
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, curPicturePath);
		return intent;
	}

	public Uri getPictureUri() {
		return curPicturePath;
	}

	/** 调用系统裁剪图片的功能 */
	public Intent cropPicture() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(curPicturePath, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1.01);
		intent.putExtra("outputX", PICTURE_QUESTION_WIDTH);
		intent.putExtra("outputY", PICTURE_QUESTION_HEIGHT);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, curPicturePath);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		return intent;
	}

	/** 生成的随机文件名 */
	private String generatePictureName() {
		String randomString = UUID.randomUUID().toString();
		return randomString;
	}

	/** 存储bitmap */
	public String saveBitmap(Bitmap bitmap) {
		String fileName = generatePictureName();
		File f = new File(pictureDir, fileName);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pictureDir + fileName;
	}

	/** 根据路径获取图片 */
	public Bitmap getBitmap(String path) {
		Bitmap bmap = BitmapUtils.decodeBitmapFromSd2(path, (int) (BaseApplication.getScreenWidth
				() * 0.2), (int) (BaseApplication.getScreenWidth() * 0.2));
		return bmap;
	}

	/** 获取压缩到指定大小的图片 */
	public Bitmap getBitmap(Bitmap src) {
		return BitmapUtils.createScaleBitmap(src, (int) (BaseApplication.getScreenWidth() * 0.2), (int) (BaseApplication.getScreenWidth() * 0.2));
	}
}
