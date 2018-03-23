package com.physicmaster.modules.study.fragment.widget.wave;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.physicmaster.R;


/**
 * Created by huashigen on 2017-06-30.
 */

public class Boat extends View {
    private Bitmap bitmapBoat;
    private Paint mPaint;
    private final float mMaxWaveAngle = 4.0f;
    private float mWaveDelta = -0.05f;
    private float mCurWaveAngle = 0.0f;
    private float mMaxTranslate = 6.0f;
    private float mCurTranslate = 0.0f;
    private float mTranslateDelta = 0.1f;
    private RefreshProgressRunnable mRefreshProgressRunnable;
    private float density;

    public Boat(Context context) {
        this(context, null);
    }

    public Boat(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Boat(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setBackgroundColor(Color.TRANSPARENT);
        density = getResources().getDisplayMetrics().density;
        bitmapBoat = ((BitmapDrawable) getResources().getDrawable(R.mipmap.boat1)).getBitmap();
        Bitmap bitmapBoatMax = ((BitmapDrawable) getResources().getDrawable(R.mipmap.boat3)).getBitmap();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bitmapBoatMax.getWidth() + (int) (10 * density), bitmapBoatMax.getHeight() + (int) (10 * density));
        setLayoutParams(params);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setFilterBitmap(true);
        mPaint.setColor(Color.RED);
        mMaxTranslate = mMaxTranslate * density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.rotate(mCurWaveAngle, getWidth() / 2, getHeight() / 2);
        canvas.translate(0, mCurTranslate);
        canvas.drawBitmap(bitmapBoat, (getWidth() - bitmapBoat.getWidth()) / 2, getHeight() - bitmapBoat.getHeight(), mPaint);
//        canvas.drawBitmap(bitmapBoat, null, new Rect(0, 0, getWidth(), getHeight()), mPaint);
        if (hasNewMsg) {
            canvas.drawCircle(bitmapBoat.getWidth() - 10 * density, 15 * density, 5 * density, mPaint);
        }
    }

    private class RefreshProgressRunnable implements Runnable {
        public void run() {
            synchronized (Boat.this) {
                long start = System.currentTimeMillis();
                calAngle();
                invalidate();
                //每秒约60帧
                long gap = 16 - (System.currentTimeMillis() - start);
                postDelayed(this, gap < 0 ? 0 : gap);
            }
        }
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if (View.GONE == visibility) {
            removeCallbacks(mRefreshProgressRunnable);
        } else {
            removeCallbacks(mRefreshProgressRunnable);
            mRefreshProgressRunnable = new RefreshProgressRunnable();
            post(mRefreshProgressRunnable);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            if (mMaxWaveAngle == 0) {
                startWave();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mMaxWaveAngle == 0) {
            startWave();
        }
    }

    private void startWave() {
    }

    /**
     * 计算船晃动角度
     */
    private void calAngle() {
        mCurWaveAngle += mWaveDelta;
        if (1 == Float.compare(Math.abs(mCurWaveAngle), mMaxWaveAngle)) {
            mWaveDelta *= -1;
        }
        mCurTranslate += mTranslateDelta;
        if (1 == Float.compare(Math.abs(mCurTranslate), mMaxTranslate)) {
            mTranslateDelta *= -1;
        }
    }

    /**
     * 升级船只
     *
     * @param boat
     */
    public void upgradeBoat(Bitmap boat, boolean hasNewMsg) {
        bitmapBoat = boat;
        this.hasNewMsg = hasNewMsg;
        invalidate();
    }

    private boolean hasNewMsg = false;
}
