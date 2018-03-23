package com.physicmaster.widget;

/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/10 10:58
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.physicmaster.R;

/**
 * 自定义带圆点的进度条
 */
public class HalfProgressBar extends View {
    private int maxProgress = 100;
    //设置进度条背景宽度
    private float progressStrokeWidth = 6;
    //设置进度条进度宽度
    private float marxArcStorkeWidth = 8;
    //设置进度条圆点的宽度
    private float circularDotWidth = 25;
    private static final String TAG = "HalfProgressBar";

    /**
     * 画笔对象的引用
     */
    private Paint paint;

    public synchronized int getProgress() {
        return progress;
    }

    /**
     * Android提供了Invalidate方法实现界面刷新，但是Invalidate不能直接在线程中调用，因为他是违背了单线程模型：Android
     * UI操作并不是线程安全的，并且这些操作必须在UI线程中调用。
     * 而postInvalidate()在工作者线程中被调用 使用postInvalidate则比较简单，不需要handler，直接在线程中调用postInvalidate即可。
     *
     * @param progress 传过来的进度
     */
    public void setProgress(int progress) {
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        this.progress = progress;
        postInvalidate();
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    /**
     * 当前进度
     */
    private int progress = 0;

    private RectF oval;
    private int roundProgressColor;
    private int roundColor;
    private int circularDotColor;

    public HalfProgressBar(Context context) {
        super(context);
    }

    public HalfProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        oval = new RectF();
        //这是自定义view 必须要写的
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.HalfProgressBar);
        roundProgressColor = mTypedArray.getColor(R.styleable
                .HalfProgressBar_roundProgressColor1, Color.YELLOW);
        roundColor = mTypedArray.getColor(R.styleable.HalfProgressBar_roundColor1, Color.YELLOW);
        circularDotColor = mTypedArray.getColor(R.styleable.HalfProgressBar_circularDotColor1,
                Color.YELLOW);
        mTypedArray.recycle();
    }

    public HalfProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        oval = new RectF();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.HalfProgressBar);
        roundProgressColor = mTypedArray.getColor(R.styleable
                .HalfProgressBar_roundProgressColor1, Color.YELLOW);
        roundColor = mTypedArray.getColor(R.styleable.HalfProgressBar_roundColor1, Color.YELLOW);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO 自动生成的方法存根
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();

        paint.setAntiAlias(true); // 设置画笔为抗锯齿
        paint.setColor(roundColor); // 设置画笔颜色
        paint.setStrokeWidth(progressStrokeWidth); // 线宽
        paint.setStyle(Paint.Style.STROKE);

        oval.left = marxArcStorkeWidth / 2; // 左上角x
        oval.top = circularDotWidth; // 左上角y
        oval.right = width - circularDotWidth / 2; // 左下角x
        oval.bottom = width - circularDotWidth / 2; // 右下角y
        float bangjing = ((width - circularDotWidth / 2) / 2);//半径
        //调整圆背景的大小
        canvas.drawArc(oval, 180, 180, false, paint); // 绘制进度条背景
        //进度条颜色
//        dodo(progress);
        paint.setColor(roundProgressColor);
        paint.setStrokeWidth(marxArcStorkeWidth);
        float swipeAngle = ((float) progress / (float) maxProgress);
        Log.d(TAG, "swipeAngle:"+swipeAngle);
        canvas.drawArc(oval, 180, 180 * ((float) progress / (float) maxProgress), false, paint);
        // 绘制进度圆弧
        //画圆点
        paint.setColor(circularDotColor);
        paint.setAntiAlias(true); // 设置画笔为抗锯齿
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(circularDotWidth);
        //当画笔样式为STROKE或FILL_OR_STROKE时，设置笔刷的图形样式，如圆形样式Cap.ROUND,或方形样式Cap.SQUARE
        paint.setStrokeCap(Paint.Cap.ROUND);
        float jindu = ((float) progress / (float) maxProgress * 100 * 1.8f);
        canvas.drawPoint(bangjing - ((float) (Math.sin((Math.PI / (double) 180) * (90 - jindu)))
                        * bangjing),
                bangjing + circularDotWidth - ((float) (Math.cos((Math.PI / (double) 180) *
                        (double) (90 - jindu))) * bangjing), paint);
//        canvas.drawPoint(bangjing * (float) (1 - Math.cos(Math.PI * (double) 180 * (float)
//                progress / maxProgress))
//                , bangjing * (float) (1 - Math.sin(Math.PI * (double) 180 * (float) progress /
//                        maxProgress)), paint);

    }

    public void dodo(float progress) {
        //也可使用3.0的AnimationSet实现
//      AnimationSet set = new AnimationSet(true);
//      set.setRepeatCount(AnimationSet.INFINITE);
//      set.setInterpolator(new AccelerateDecelerateInterpolator());
//      set.start();
//      this.setAnimation(set);

        //4.0以上，在AnimationSet基础上封装的，遗憾的是没有Repeat
        AnimatorSet animation = new AnimatorSet();

        ObjectAnimator progressAnimation = ObjectAnimator.ofFloat(this, "progress", 0f, progress);
        progressAnimation.setDuration(700);// 动画执行时间

        /*
         * AccelerateInterpolator　　　　　                  加速，开始时慢中间加速
         * DecelerateInterpolator　　　 　　                 减速，开始时快然后减速
         * AccelerateDecelerateInterolator　                     先加速后减速，开始结束时慢，中间加速
         * AnticipateInterpolator　　　　　　                 反向 ，先向相反方向改变一段再加速播放
         * AnticipateOvershootInterpolator　                 反向加超越，先向相反方向改变，再加速播放，会超出目的值然后缓慢移动至目的值
         * BounceInterpolator　　　　　　　
         * 跳跃，快到目的值时值会跳跃，如目的值100，后面的值可能依次为85，77，70，80，90，100
         * CycleIinterpolator　　　　　　　　                   循环，动画循环一定次数，值的改变为一正弦函数：Math.sin(2 *
         * mCycles * Math.PI * input) LinearInterpolator　　　 线性，线性均匀改变
         * OvershottInterpolator　　　　　　                  超越，最后超出目的值然后缓慢改变到目的值
         * TimeInterpolator　　　　　　　　　                        一个接口，允许你自定义interpolator，以上几个都是实现了这个接口
         */
        progressAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        animation.playTogether(progressAnimation);//动画同时执行,可以做多个动画
        animation.start();
    }
}
