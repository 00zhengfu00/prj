package com.physicmaster.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.physicmaster.R;

/**
 * Created by songrui on 17/1/16.
 */

public class LocusView extends View {

    private Paint mPaint;
    private Path  mPath;
    private int   mWidth;
    private int   mHeight;
    private Paint mPaintBackground;
    private int pathInsideWidth       = 30;// 内部轨道宽度
    private int pathOutsideWidth      = 90;//外部轨道宽度
    private int pointSize             = 40;//外部轨道宽度
    private int boxWidth              = 100;//宝箱大小
    private int boxHeight             = 100;//宝箱大小
    private int portraitPadding       = 20;//头像padding
    private int portraitPaddingBottom = 50;//头像paddingBottom
    private Bitmap pointBitmapOn, pointBitmapOff, boxOnBigmap, boxOffBigmap, portraitBitmap;
    private float marginLeftPercent  = 0.15f;
    private float marginRightPercent = 0.88f;
    private float pathXPercent       = 0.8f;
    private float pathStartXPercent  = 0.1f;
    private int   currentStep        = 0;

    private Bitmap userPortraitBitmap;

    private OnGiftBoxOpenListener onGiftBoxOpenListener;

    private OnBoxClickListener onBoxClickListener;
    private int                startHeight;
    private int                index;

    public LocusView(Context context) {
        this(context, null);
    }

    public LocusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Lucas);
        pathInsideWidth = typedArray.getDimensionPixelSize(R.styleable.Lucas_pathInsideWidth,
                pathInsideWidth);
        pathOutsideWidth = typedArray.getDimensionPixelSize(R.styleable.Lucas_pathOutsideWidth,
                pathOutsideWidth);
        pointSize = typedArray.getDimensionPixelSize(R.styleable.Lucas_pointSize,
                pointSize);
        boxHeight = typedArray.getDimensionPixelSize(R.styleable.Lucas_boxHeight,
                boxHeight);
        boxWidth = typedArray.getDimensionPixelSize(R.styleable.Lucas_boxWidth,
                boxWidth);
        portraitPadding = typedArray.getDimensionPixelSize(R.styleable.Lucas_portraitPadding,
                portraitPadding);
        portraitPaddingBottom = typedArray.getDimensionPixelSize(R.styleable
                        .Lucas_portraitPaddingBottom,
                portraitPaddingBottom);
        typedArray.recycle();
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.colorLocus));
        mPaint.setStrokeWidth(pathInsideWidth);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPaintBackground = new Paint();
        mPaintBackground.setColor(getResources().getColor(R.color.colorGreenBackground));
        mPaintBackground.setStrokeWidth(pathOutsideWidth);
        mPaintBackground.setAntiAlias(true);
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintBackground.setStrokeCap(Paint.Cap.ROUND);

        Drawable pointBitmapOnDrawable = getResources().getDrawable(R.mipmap.signin);
        pointBitmapOn = ((BitmapDrawable) pointBitmapOnDrawable).getBitmap();

        Drawable pointBitmapOffDrawable = getResources().getDrawable(R.mipmap.unsignin);
        pointBitmapOff = ((BitmapDrawable) pointBitmapOffDrawable).getBitmap();

        Drawable boxOnDrawable = getResources().getDrawable(R.mipmap.box_open);
        boxOnBigmap = ((BitmapDrawable) boxOnDrawable).getBitmap();

        Drawable boxOffDrawable = getResources().getDrawable(R.mipmap.box_off);
        boxOffBigmap = ((BitmapDrawable) boxOffDrawable).getBitmap();

        Drawable drawablePortrait = getResources().getDrawable(R.mipmap.head_wraper);
        portraitBitmap = ((BitmapDrawable) drawablePortrait).getBitmap();

    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
        invalidate();
    }

    public void setUserPortraitBitmap(Bitmap userPortraitBitmap) {
        this.userPortraitBitmap = userPortraitBitmap;
        invalidate();
    }

    public void setOnGiftBoxOpenListener(OnGiftBoxOpenListener onGiftBoxOpenListener) {
        this.onGiftBoxOpenListener = onGiftBoxOpenListener;
    }

    public void setOnBoxClickListener(OnBoxClickListener onBoxClickListener) {
        this.onBoxClickListener = onBoxClickListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            int desired = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() -
                    getPaddingRight();
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int desired = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() -
                    getPaddingBottom();
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        this.mHeight = height;
        this.mWidth = width;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startHeight = mHeight - pathOutsideWidth / 2;
        //画外部路径
        canvas.drawLine(mWidth * marginLeftPercent, startHeight, mWidth * marginRightPercent,
                startHeight,
                mPaintBackground);
        canvas.drawLine(mWidth * marginRightPercent, startHeight, mWidth * marginRightPercent,
                startHeight -
                        mHeight * 0.22f, mPaintBackground);
        canvas.drawLine(mWidth * marginRightPercent, startHeight - mHeight * 0.22f, mWidth *
                        marginLeftPercent,
                startHeight - mHeight * 0.22f, mPaintBackground);
        canvas.drawLine(mWidth * marginLeftPercent, startHeight - mHeight * 0.22f, mWidth *
                        marginLeftPercent, startHeight - mHeight * 0.40f,
                mPaintBackground);
        canvas.drawLine(mWidth * marginLeftPercent, startHeight - mHeight * 0.40f, mWidth *
                        marginRightPercent, startHeight - mHeight * 0.40f,
                mPaintBackground);
        canvas.drawLine(mWidth * marginRightPercent, startHeight - mHeight * 0.40f,
                mWidth * marginRightPercent, startHeight - mHeight * 0.62f,
                mPaintBackground);
        canvas.drawLine(mWidth * marginRightPercent, startHeight - mHeight * 0.62f,
                mWidth * marginLeftPercent, startHeight - mHeight * 0.62f,
                mPaintBackground);
        canvas.drawLine(mWidth * marginLeftPercent, startHeight - mHeight * 0.62f, mWidth *
                        marginLeftPercent,
                startHeight - mHeight * 0.82f, mPaintBackground);
        canvas.drawLine(mWidth * marginLeftPercent, startHeight - mHeight * 0.82f, mWidth * 4
                        / 10,
                startHeight - mHeight * 0.82f, mPaintBackground);

        //画内部路径
        canvas.drawLine(mWidth * marginLeftPercent, startHeight, mWidth * marginRightPercent,
                startHeight,
                mPaint);
        canvas.drawLine(mWidth * marginRightPercent, startHeight, mWidth * marginRightPercent,
                startHeight -
                        mHeight * 0.22f, mPaint);
        canvas.drawLine(mWidth * marginRightPercent, startHeight - mHeight * 0.22f, mWidth *
                        marginLeftPercent,
                startHeight - mHeight * 0.22f, mPaint);
        canvas.drawLine(mWidth * marginLeftPercent, startHeight - mHeight * 0.22f, mWidth *
                        marginLeftPercent, startHeight - mHeight * 0.40f,
                mPaint);
        canvas.drawLine(mWidth * marginLeftPercent, startHeight - mHeight * 0.40f, mWidth *
                        marginRightPercent, startHeight - mHeight * 0.40f,
                mPaint);
        canvas.drawLine(mWidth * marginRightPercent, startHeight - mHeight * 0.40f,
                mWidth * marginRightPercent, startHeight - mHeight * 0.62f,
                mPaint);
        canvas.drawLine(mWidth * marginRightPercent, startHeight - mHeight * 0.62f,
                mWidth * marginLeftPercent, startHeight - mHeight * 0.62f,
                mPaint);
        canvas.drawLine(mWidth * marginLeftPercent, startHeight - mHeight * 0.62f, mWidth *
                        marginLeftPercent,
                startHeight - mHeight * 0.82f, mPaint);
        canvas.drawLine(mWidth * marginLeftPercent, startHeight - mHeight * 0.82f, mWidth * 4
                        / 10,
                startHeight - mHeight * 0.82f, mPaint);

        //画步点
        if (currentStep <= 6) {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent),
                    startHeight, 6, canvas, currentStep, currentStep, true);
        } else if (currentStep > 6) {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent),
                    startHeight, 6, canvas, -1, 6, true);
        } else {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent),
                    startHeight, 6, canvas, -1, 0, true);
        }
        if (currentStep >= 8 && currentStep <= 13) {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                            (startHeight - mHeight * 0.22f), 6, canvas, 7 - (currentStep - 7),
                    (currentStep - 7), false);
        } else if (currentStep > 13) {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                    (startHeight - mHeight * 0.22f), 6, canvas, -1, 6, true);
        } else {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                    (startHeight - mHeight * 0.22f), 6, canvas, -1, 0, true);
        }
        if (currentStep >= 15 && currentStep <= 20) {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                            (startHeight - mHeight * 0.40f), 6,
                    canvas, currentStep - 14, currentStep - 14, true);
        } else if (currentStep > 20) {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                            (startHeight - mHeight * 0.40f), 6,
                    canvas, -1, 6, true);
        } else {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                            (startHeight - mHeight * 0.40f), 6,
                    canvas, -1, 0, true);
        }
        if (currentStep >= 22 && currentStep <= 27) {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                            (startHeight - mHeight * 0.62f), 6,
                    canvas, 7 - (currentStep - 21), (currentStep - 21), false);
        } else if (currentStep > 27) {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                            (startHeight - mHeight * 0.62f), 6,
                    canvas, -1, 6, true);
        } else {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                            (startHeight - mHeight * 0.62f), 6,
                    canvas, -1, 0, true);
        }
        if (currentStep >= 29) {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                            (startHeight - mHeight * 0.82f), 2,
                    canvas, currentStep - 28, currentStep - 28, true);
        } else {
            drawPoint((int) (mWidth * marginLeftPercent), (int) (mWidth * marginRightPercent), (int)
                            (startHeight - mHeight * 0.82f), 2,
                    canvas, -1, 0, true);
        }

        //画宝箱
        if (currentStep >= 7) {
            drawBox((int) (mWidth * marginRightPercent), (int) (startHeight - mHeight * 0.11f),
                    true, canvas, 1);
        } else {
            drawBox((int) (mWidth * marginRightPercent), (int) (startHeight - mHeight * 0.11f),
                    false, canvas, 1);
        }
        if (currentStep >= 14) {
            drawBox((int) (mWidth * marginLeftPercent), (int) (startHeight - mHeight * 0.31f), true,
                    canvas, 2);
        } else {
            drawBox((int) (mWidth * marginLeftPercent), (int) (startHeight - mHeight * 0.31f),
                    false,
                    canvas, 2);
        }
        if (currentStep >= 21) {
            drawBox((int) (mWidth * marginRightPercent), (int) (startHeight - mHeight * 0.51f),
                    true, canvas, 3);
        } else {
            drawBox((int) (mWidth * marginRightPercent), (int) (startHeight - mHeight * 0.51f),
                    false, canvas, 3);
        }
        if (currentStep >= 28) {
            drawBox((int) (mWidth * marginLeftPercent), (int) (startHeight - mHeight * 0.72f), true,
                    canvas, 4);
        } else {
            drawBox((int) (mWidth * marginLeftPercent), (int) (startHeight - mHeight * 0.72f),
                    false,
                    canvas, 4);
        }

        //画起始点
        Drawable drawable = getResources().getDrawable(R.mipmap.start);
        Bitmap startBitmap = ((BitmapDrawable) drawable).getBitmap();
        RectF rectStart = new RectF();
        rectStart.set(mWidth * marginLeftPercent - startBitmap.getWidth() / 2, startHeight -
                startBitmap.getHeight() + pathInsideWidth / 2, mWidth *
                marginLeftPercent + startBitmap.getWidth() / 2, startHeight + pathInsideWidth / 2);
        canvas.drawBitmap(startBitmap, null, rectStart, mPaintBackground);
    }

    /**
     * 画步点
     * @param onSize
     *         走过的步点数目
     * @param inOrder
     *         是否按顺序排列或者按倒序排列
     */
    private void drawPoint(int startX, int endX, int startY, int num, Canvas canvas, int
            currentStep, int onSize, boolean inOrder) {
        int length = endX - startX;
        int space = length / 7;
        RectF rectF = new RectF();
        for (int i = 1; i <= num; i++) {
            rectF.set(startX + space * i - pointSize / 2, startY - pointSize / 2, startX + space
                    * i + pointSize / 2, startY + pointSize / 2);
            if (inOrder) {
                if (i <= onSize) {
                    canvas.drawBitmap(pointBitmapOn, null, rectF, mPaint);
                } else {
                    canvas.drawBitmap(pointBitmapOff, null, rectF, mPaint);
                }
            } else {
                if (i <= 6 - onSize) {
                    canvas.drawBitmap(pointBitmapOff, null, rectF, mPaint);
                } else {
                    canvas.drawBitmap(pointBitmapOn, null, rectF, mPaint);
                }
            }

            //画当前位置
            if (currentStep == i) {
                RectF rectF1 = new RectF();
                rectF1.set(startX + space * i - portraitBitmap.getWidth() / 2, startY -
                        portraitBitmap.getHeight(), startX + space * i + portraitBitmap.getWidth()
                        / 2, startY);
                canvas.drawBitmap(portraitBitmap, null, rectF1, mPaint);
                RectF rectPortrait = new RectF();
                rectPortrait.set(startX + space * i - portraitBitmap.getWidth() / 2 +
                        portraitPadding, startY -
                        portraitBitmap.getHeight() + portraitPadding, startX + space * i +
                        portraitBitmap
                                .getWidth()
                                / 2 - portraitPadding, startY - portraitPaddingBottom);
                if (userPortraitBitmap != null) {
                    canvas.drawBitmap(userPortraitBitmap, null, rectPortrait, mPaint);
                }
            }
        }
    }

    /**
     * 画宝箱
     */
    private void drawBox(int boxX, int boxY, boolean opened, Canvas canvas, int index) {
        RectF rectF = new RectF();
        rectF.set(boxX - boxWidth / 2, boxY - boxHeight / 2, boxX + boxWidth / 2, boxY +
                boxHeight / 2);
        if (opened) {
            canvas.drawBitmap(boxOnBigmap, null, rectF, mPaint);
        } else {
            canvas.drawBitmap(boxOffBigmap, null, rectF, mPaint);
        }
        //画当前签到位置
        RectF rectS = new RectF();
        rectS.set(boxX - portraitBitmap.getWidth() / 2, boxY - portraitBitmap.getHeight() -
                        boxOnBigmap.getHeight() / 2,
                boxX + portraitBitmap.getWidth() / 2, boxY - boxOnBigmap.getHeight() / 2);
        RectF rectPortrait = new RectF();
        rectPortrait.set(boxX - portraitBitmap.getWidth() / 2 + portraitPadding, boxY -
                portraitBitmap
                        .getHeight() - boxOnBigmap.getHeight() / 2 + portraitPadding, boxX +
                portraitBitmap.getWidth()
                        / 2 - portraitPadding, boxY - boxOnBigmap.getHeight() / 2 - portraitPaddingBottom);
        if (currentStep == 7 && index == 1) {
            canvas.drawBitmap(portraitBitmap, null, rectS, mPaint);
            if (userPortraitBitmap != null) {
                canvas.drawBitmap(userPortraitBitmap, null, rectPortrait, mPaint);
            }
        } else if (currentStep == 14 && index == 2) {
            canvas.drawBitmap(portraitBitmap, null, rectS, mPaint);
            if (userPortraitBitmap != null) {
                canvas.drawBitmap(userPortraitBitmap, null, rectPortrait, mPaint);
            }
        } else if (currentStep == 21 && index == 3) {
            canvas.drawBitmap(portraitBitmap, null, rectS, mPaint);
            if (userPortraitBitmap != null) {
                canvas.drawBitmap(userPortraitBitmap, null, rectPortrait, mPaint);
            }
        } else if (currentStep == 28 && index == 4) {
            canvas.drawBitmap(portraitBitmap, null, rectS, mPaint);
            if (userPortraitBitmap != null) {
                canvas.drawBitmap(userPortraitBitmap, null, rectPortrait, mPaint);
            }
        }
    }

    /**
     * 签到前进一步
     */
    public void goForward() {
        if (currentStep == 30) {
            return;
        }
        currentStep++;
        invalidate();
        if (currentStep == 7 || currentStep == 14 || currentStep == 21 || currentStep == 28) {
            if (onGiftBoxOpenListener != null) {
                onGiftBoxOpenListener.onGiftBoxOpen();
            }
        }
    }

    public interface OnGiftBoxOpenListener {
        void onGiftBoxOpen();
    }

    public interface OnBoxClickListener {
        void onBoxClick(int index);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if ((x < (int) ((mWidth * marginRightPercent) + boxWidth / 2))
                    && (x > (int) ((mWidth * marginRightPercent) - boxWidth / 2))
                    && (y > (int) ((startHeight - mHeight * 0.11f) - boxHeight / 2))
                    && (y < (int) ((startHeight - mHeight * 0.11f) + boxHeight / 2))) {
                if (onBoxClickListener != null) {
                    onBoxClickListener.onBoxClick(6);
                }
            } else if ((x < (int) ((mWidth * marginLeftPercent) + boxWidth / 2))
                    && (x > (int) ((mWidth * marginLeftPercent) - boxWidth / 2))
                    && (y > (int) ((startHeight - mHeight * 0.31f) - boxHeight / 2))
                    && (y < (int) ((startHeight - mHeight * 0.31f) + boxHeight / 2))) {
                if (onBoxClickListener != null) {
                    onBoxClickListener.onBoxClick(13);
                }
            } else if ((x < (int) ((mWidth * marginRightPercent) + boxWidth / 2))
                    && (x > (int) ((mWidth * marginRightPercent) - boxWidth / 2))
                    && (y > (int) ((startHeight - mHeight * 0.51f) - boxHeight / 2))
                    && (y < (int) ((startHeight - mHeight * 0.51f) + boxHeight / 2))) {
                if (onBoxClickListener != null) {
                    onBoxClickListener.onBoxClick(20);
                }
            } else if ((x < (int) ((mWidth * marginLeftPercent) + boxWidth / 2))
                    && (x > (int) ((mWidth * marginLeftPercent) - boxWidth / 2))
                    && (y > (int) ((startHeight - mHeight * 0.72f) - boxHeight / 2))
                    && (y < (int) ((startHeight - mHeight * 0.72f) + boxHeight / 2))) {
                if (onBoxClickListener != null) {
                    onBoxClickListener.onBoxClick(27);
                }
            }
                return true;
        }
        return super.onTouchEvent(event);
    }
}
