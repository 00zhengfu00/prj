package com.physicmaster.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.physicmaster.utils.ScreenUtils;

/**
 * Created by songrui on 17/9/29.
 */

public class MyWaveView extends View
{

    // 波纹颜色
    private static final int WAVE_PAINT_COLOR1 = 0x20f0f0f0;
    // 波纹颜色
    private static final int WAVE_PAINT_COLOR2 = 0x20f0f0f0;
    // 波纹颜色
    private static final int WAVE_PAINT_COLOR3 = 0x20f0f0f0;

    // y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 30;
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 5;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 4;
    // 第三条水波移动速度
    private static final int TRANSLATE_X_SPEED_THREE = 3;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private float[] mResetThreeYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOffsetSpeedThree;
    private int mXOneOffset;
    private int mXTwoOffset;
    private int mXThreeOffset;

    private Paint      mWavePaint1;
    private Paint      mWavePaint2;
    private DrawFilter mDrawFilter;
    private float progress = 1.0f;

    private float nowProgress=0f;
    private int continueTime=0;//上升动画效果持续时间，默认0
    private final Paint mWavePaint3;

    /**
     *
     * @Title: setProgress
     * @Description: 设置水波纹动画上升的高度百分比及持续时间
     * @author: wukeqi
     * @date: 2017-4-27 上午8:49:44
     * @param progress
     * @param continueTime
     * @return: void
     */
    public void setProgress(float progress,int continueTime)
    {
        this.progress = progress;
        this.continueTime=continueTime;
        postInvalidate();
    }

    public MyWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = ScreenUtils.dp2px(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo =  ScreenUtils.dp2px(context, TRANSLATE_X_SPEED_TWO);
        mXOffsetSpeedThree =  ScreenUtils.dp2px(context, TRANSLATE_X_SPEED_THREE);

        // 初始绘制波纹的画笔
        mWavePaint1 = new Paint();
        // 去除画笔锯齿
        mWavePaint1.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint1.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mWavePaint1.setColor(WAVE_PAINT_COLOR1);

        // 初始绘制波纹的画笔
        mWavePaint2 = new Paint();
        // 去除画笔锯齿
        mWavePaint2.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint2.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mWavePaint2.setColor(WAVE_PAINT_COLOR2);

        // 初始绘制波纹的画笔
        mWavePaint3 = new Paint();
        // 去除画笔锯齿
        mWavePaint3.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint3.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mWavePaint3.setColor(WAVE_PAINT_COLOR3);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        resetPositonY();
   /*     for (int i = 0; i < mTotalWidth; i++) {

            // 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
            // 绘制第一条水波纹
            canvas.drawLine(i, mTotalHeight - mResetOneYPositions[i] - (mTotalHeight-STRETCH_FACTOR_A)*progress, i,
                    mTotalHeight,
                    mWavePaint1);

            // 绘制第二条水波纹
            canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] - (mTotalHeight-STRETCH_FACTOR_A)*progress, i,
                    mTotalHeight,
                    mWavePaint2);
        }  */

        canvas.drawLines(mResetOneYPositions, mWavePaint1);
        canvas.drawLines(mResetTwoYPositions, mWavePaint2);
        canvas.drawLines(mResetThreeYPositions, mWavePaint3);

        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;
        mXThreeOffset += mXOffsetSpeedThree;

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }
        if (mXThreeOffset > mTotalWidth) {
            mXThreeOffset = 0;
        }

        //y轴上升的 百分比，形成缓缓上升的效果
        nowProgress+=progress/(continueTime/20<=0?1:continueTime/20);

        nowProgress=nowProgress>=progress?progress:nowProgress;

        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片

        handler.sendEmptyMessageDelayed(0, 20);
    }

    private Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            postInvalidate();
        }

    };
    private void resetPositonY() {
      /*  // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length/4 - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset*4, mResetOneYPositions, 0, yOneInterval*4);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval*4, mXOneOffset*4);



        int yTwoInterval = mYPositions.length/4 - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset*4, mResetTwoYPositions, 0,
                yTwoInterval*4);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval*4, mXTwoOffset*4);*/

        //根据偏移量重新赋值（y轴起点处的值）

        for(int i=0;i<mTotalWidth;i++){
            mResetOneYPositions[((mXOneOffset+i)*4+1)%(mTotalWidth*4)]=mYPositions[i*4+1]- (mTotalHeight-STRETCH_FACTOR_A)*nowProgress;
            mResetTwoYPositions[((mXTwoOffset+i)*4+1)%(mTotalWidth*4)]=mYPositions[i*4+1]- (mTotalHeight-STRETCH_FACTOR_A)*nowProgress;
            mResetThreeYPositions[((mXThreeOffset+i)*4+1)%(mTotalWidth*4)]=mYPositions[i*4+1]- (mTotalHeight-STRETCH_FACTOR_A)*nowProgress;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth*4];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth*4];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth*4];
        // 用于保存波纹三的y值
        mResetThreeYPositions = new float[mTotalWidth*4];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        // 根据view总宽度得出所有对应的y值  （4*i+1）,并创建drawLines所需要的数组
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[4*i+0]=i;
            mYPositions[4*i+1]=(float) (mTotalHeight-(STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y));
            mYPositions[4*i+2]=i;
            mYPositions[4*i+3] = mTotalHeight;
        }

        //复制数组
        System.arraycopy(mYPositions, 0, mResetOneYPositions, 0, mTotalWidth*4);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, 0, mTotalWidth*4);
        System.arraycopy(mYPositions, 0, mResetThreeYPositions, 0, mTotalWidth*4);
    }
}
