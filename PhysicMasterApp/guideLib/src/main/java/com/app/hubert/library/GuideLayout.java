package com.app.hubert.library;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by hubert
 * <p>
 * Created on 2017/7/27.
 */
public class GuideLayout extends RelativeLayout {

    public static final int DEFAULT_BACKGROUND_COLOR = 0xb2000000;
    private int mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
    private Paint mPaintDash;
    private Paint mPaint;
    private List<HighLight> highLights;

    private int offset;//fix #13 nubia view.getLocationOnScreen获取异常（没有包含statusBar高度）

    public GuideLayout(Context context) {
        this(context, null);
    }

    public GuideLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaintDash = new Paint();
        mPaint.setAntiAlias(true);
        mPaintDash.setAntiAlias(true);
        mPaintDash.setColor(Color.WHITE);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        PorterDuffXfermode xfermodeAdd = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        mPaint.setXfermode(xfermode);
        mPaintDash.setXfermode(xfermodeAdd);
        mPaintDash.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.stroke_width));
        mPaintDash.setStyle(Paint.Style.STROKE);
        //设置画笔遮罩滤镜,可以传入BlurMaskFilter或EmbossMaskFilter，前者为模糊遮罩滤镜而后者为浮雕遮罩滤镜
        //这个方法已经被标注为过时的方法了，如果你的应用启用了硬件加速，你是看不到任何阴影效果的
        //mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));
        //关闭当前view的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        setClickable(true);

        //ViewGroup默认设定为true，会使onDraw方法不执行，如果复写了onDraw(Canvas)方法，需要清除此标记
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mBackgroundColor);
        if (highLights != null) {
            for (HighLight highLight : highLights) {
                RectF rectF = highLight.getRectF();
                rectF.top = rectF.top + offset;
                rectF.bottom = rectF.bottom + offset;
                int padding = getResources().getDimensionPixelSize(R.dimen.padding);
                RectF rectF1 = new RectF(rectF.left-padding,rectF.top-padding,rectF.right+padding,rectF.bottom+padding);
                int strokeWidth = getResources().getDimensionPixelSize(R.dimen.stroke_width);
                PathEffect effects = new DashPathEffect(new float[]{strokeWidth, strokeWidth * 2, strokeWidth, strokeWidth * 2}, 1);
                mPaintDash.setPathEffect(effects);
                switch (highLight.getType()) {
                    case CIRCLE:
                        canvas.drawCircle(rectF.centerX(), rectF.centerY(), highLight.getRadius(), mPaint);
                        canvas.drawCircle(rectF.centerX(), rectF.centerY(), highLight.getRadius() + padding, mPaintDash);
                        break;
                    case OVAL:
                        canvas.drawOval(rectF, mPaint);
                        canvas.drawOval(rectF1, mPaintDash);
                        break;
                    case ROUND_RECTANGLE:
                        canvas.drawRoundRect(rectF, highLight.getRound(), highLight.getRound(), mPaint);
                        canvas.drawRoundRect(rectF1, highLight.getRound(), highLight.getRound(), mPaintDash);
                        break;
                    case RECTANGLE:
                    default:
                        canvas.drawRect(rectF, mPaint);
                        canvas.drawRect(rectF1, mPaintDash);
                        break;
                }
            }
        }
    }

    public void setHighLights(List<HighLight> holeList) {
        highLights = holeList;
    }

    public void setBackgroundColor(int backgroundColor) {
        if (backgroundColor != 0) {
            this.mBackgroundColor = backgroundColor;
        } else {
            mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
        }
    }

    public void setOffset(int offset) {
        this.offset = offset;
        invalidate();
    }
}