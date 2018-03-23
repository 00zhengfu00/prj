/* 
 * 系统名称：lswuyou
 * 类  名  称：CircleImageView.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-11-26 下午8:10:47
 * 功能说明： 绘制圆形的Drawable
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.common;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class CircleImageDrawable extends Drawable {

	private Paint  mPaint;
	private int    mWidth;
	private Bitmap mBitmap;

	public CircleImageDrawable(Bitmap bitmap) {
		mBitmap = bitmap;
		BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setShader(bitmapShader);
		mWidth = Math.min(mBitmap.getWidth(), mBitmap.getHeight());
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaint);
	}

	@Override
	public int getIntrinsicWidth() {
		return mWidth;
	}

	@Override
	public int getIntrinsicHeight() {
		return mWidth;
	}

	@Override
	public void setAlpha(int alpha) {
		mPaint.setAlpha(alpha);
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		mPaint.setColorFilter(cf);
	}

	@Override
	public int getOpacity() {
		return PixelFormat.TRANSLUCENT;
	}
}
