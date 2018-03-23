/* 
 * 系统名称：lswuyou
 * 类  名  称：BitmapUtils.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-11-1 下午2:59:00
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;

public class BitmapUtils {
	/** 计算缩略倍数-只能为2的幂次方 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	/** 　如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响 */
	public static Bitmap createScaleBitmap(Bitmap src, int dstWidth, int dstHeight) {
		Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
		if (src != dst) { // 如果没有缩放，那么不回收
			src.recycle(); // 释放Bitmap的native像素数组
		}
		return dst;
	}

	/** 从Resources中加载图片-生成缩略图 */
	public static Bitmap decodeBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options); // 读取图片长宽
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); // 计算inSampleSize
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
		return createScaleBitmap(src, reqWidth, reqHeight); // 进一步得到目标大小的缩略图
	}

	/** 从sd卡上加载图片 -生成缩略图 */
	public static Bitmap decodeBitmapFromSd(String pathName, int reqWidth, int reqHeight) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
		return createScaleBitmap(src, reqWidth, reqHeight);
	}

	/** 从Resources中加载图片-生成缩略图方法2(图片更平滑) */
	public static Bitmap decodeBitmapFromResource2(Resources res, int resId, int reqWidth, int reqHeight) {
		Bitmap bmap = BitmapFactory.decodeResource(res, resId);
		bmap = ThumbnailUtils.extractThumbnail(bmap, reqWidth, reqHeight);
		return bmap;
	}

	/** 从sd中加载图片-生成缩略图方法2(图片更平滑) */
	public static Bitmap decodeBitmapFromSd2(String pathName, int reqWidth, int reqHeight) {
		Bitmap bmap = BitmapFactory.decodeFile(pathName);
		bmap = ThumbnailUtils.extractThumbnail(bmap, reqWidth, reqHeight);
		return bmap;
	}
	
  static public Bitmap adjustPhotoRotation(Bitmap bm, int orientationDegree)
  {

          Matrix m = new Matrix();
          m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
          float targetX, targetY;
          if (orientationDegree == 90) {
          targetX = bm.getHeight();
          targetY = 0;
          } else {
          targetX = bm.getHeight();
          targetY = bm.getWidth();
    }

      final float[] values = new float[9];
      m.getValues(values);

      float x1 = values[Matrix.MTRANS_X];
      float y1 = values[Matrix.MTRANS_Y];

      m.postTranslate(targetX - x1, targetY - y1);

      Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.RGB_565);
      Paint paint = new Paint();
      Canvas canvas = new Canvas(bm1);
      canvas.drawBitmap(bm, m, paint);

      return bm1;
    }
  
  	public static Bitmap decodeBitmapFromFile(String pathName) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(pathName, options);
		options.inSampleSize = calculateInSampleSize(options, ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());
		options.inJustDecodeBounds = false;
		Bitmap src = BitmapFactory.decodeFile(pathName, options);
		return src;
	}

	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath( final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore
					.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}
}
