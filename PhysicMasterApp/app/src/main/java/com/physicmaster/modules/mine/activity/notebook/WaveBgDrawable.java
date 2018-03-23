package com.physicmaster.modules.mine.activity.notebook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.physicmaster.R;
import com.physicmaster.utils.ScreenUtils;

/**
 * Created by huashigen on 2018-01-03.
 */

public class WaveBgDrawable extends Drawable {
    private Bitmap bitmapWave;
    private Context mContext;
    private Rect canvasRect;
    private int margin = 10;//dp
    private Paint mPaint;

    public WaveBgDrawable(Context context) {
        mContext = context;
        bitmapWave = BitmapFactory.decodeResource(context.getResources(), R.mipmap.wave_small);
        margin = ScreenUtils.dp2px(mContext, margin);
        mPaint = new Paint();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int colorBg = Color.parseColor("#19AEFE");
        canvas.drawColor(colorBg);
        int width = canvasRect.width();
        int height = canvasRect.height();
        //只在画布垂直中央的两边画两条波浪
        canvas.drawBitmap(bitmapWave, margin, height / 2 - margin, mPaint);
        canvas.drawBitmap(bitmapWave, width - margin - bitmapWave.getWidth(), height / 2 - margin, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        canvasRect = bounds;
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
