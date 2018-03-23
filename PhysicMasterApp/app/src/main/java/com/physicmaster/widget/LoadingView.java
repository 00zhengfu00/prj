package com.physicmaster.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.physicmaster.R;

/**
 * Created by huashigen on 2017-10-25.
 */

public class LoadingView extends View {

    private int mWidth;
    private int mHeight;
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;
    private final int mDefaultColor = 0xff999999;
    private final int mDefaultSegmentWidth = 10;
    private final int mDefaultSegmentLength = 20;
    private int mSegmentWidth = mDefaultSegmentWidth;
    private int mSegmentColor = mDefaultColor;
    private int mSegmentLength = mDefaultSegmentLength;

    private int control = 1;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.LoadingView_pathColor:
                    mSegmentColor = typedArray.getColor(attr, mDefaultColor);
                    break;
                case R.styleable.LoadingView_segmentLength:
                    mSegmentLength = typedArray.getDimensionPixelSize(attr, mDefaultSegmentLength);
                    break;
                case R.styleable.LoadingView_segmentWidth:
                    mSegmentWidth = typedArray.getDimensionPixelSize(attr, mDefaultSegmentWidth);
                    break;
            }
        }
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mSegmentColor);
        mPaint.setStrokeWidth(mSegmentWidth);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < 12; i++) {
            mPaint.setAlpha(((i + 1 + control) % 12 * 255) / 12);
            canvas.drawLine(mCenterX, mCenterY - mSegmentLength, mCenterX, mCenterY - mSegmentLength * 2, mPaint);
            canvas.rotate(30, mCenterX, mCenterY);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(12, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                control = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }
}