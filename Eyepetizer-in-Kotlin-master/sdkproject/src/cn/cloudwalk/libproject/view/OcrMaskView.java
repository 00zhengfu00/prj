package cn.cloudwalk.libproject.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.cloudwalk.libproject.Contants;

public class OcrMaskView extends View {
    private static final int FOCUSING = 1;
    private static final int FOCUSED = 2;
    private static final int NOFOCUS = 0;

    Context mContext;

    int mClrCircleBigOuter = Color.argb(0x88, 0x00, 0x00, 0x00);
    int mClrCircleBigInter = Color.argb(0x88, 0x99, 0x99, 0x99);
    int mClrCircleSmall = Color.argb(0x88, 0xDD, 0xDD, 0xDD);

    private Paint mLinePaint, mRectPoint, mBankPaint, mPaint, mPaintText;
    int width;
    int height;
    float focusX, focusY;
    int focusType;

    int ocrRectH, ocrRectW, ocrRectLeft, ocrRectTop;
    volatile int left=0, top=0, right=0, bottom=0;
    volatile boolean isDrawProgress = false;
    int flag = -1;//卡类型，

    final int TIME_OUT_DRAW_LINE = 2000;
    volatile long drawLineTime = 0;

    Bitmap mBitmapFocus,mBitmapFocused;
    Bitmap mBitmapScan = null;//扫描线
    int scanLineTop = 0;//扫描线坐标
    final int SCAN_VELOCITY = 8;// 扫描线移动速度
	int mDetAngle = 0;
    private boolean mAttached;
    String mBankCardTitle = "";//横竖版银行卡标题
    public OcrMaskView(Context context) {
        this(context, null);
    }

    public OcrMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
    }

    public void desotry() {
    }

    //画扫描线
    private final Runnable mDrawLine = new Runnable() {
        public void run() {
            left = 0;
            top = 0;
            right = 0;
            bottom = 0;
            postInvalidate();
            getHandler().postDelayed(mDrawLine, TIME_OUT_DRAW_LINE);
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!mAttached) {
            mAttached = true;
            mDrawLine.run();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getHandler().removeCallbacks(mDrawLine);
            mAttached = false;
        }
    }


    private void drawScanAnimation(Canvas canvas, Bitmap bitmap, Rect rect) {
		if (scanLineTop <= 0 || scanLineTop >= rect.bottom) 
			scanLineTop = rect.top;
		
		scanLineTop += SCAN_VELOCITY;
		
		if (scanLineTop+2 < rect.bottom) {
			Rect scanRect = new Rect(rect.left, scanLineTop, rect.right, scanLineTop+2);
			canvas.drawBitmap(bitmap, null, scanRect, null);
		}
    }

	/**
	 * setFocus:设置正在获取焦点 <br/>
	 * 
	 * @param x
	 * @param y
	 * @author:284891377 Date: 2016年6月30日 下午5:17:06
	 * @since JDK 1.7
	 */
    public void setFocus(float x, float y) {
        focusX = x;
        focusY = y;
        focusType = FOCUSING;
        invalidate();
    }

    /**
     * 对齐后进行画线
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setLine(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        drawLineTime = System.currentTimeMillis();

        if (0==left || 0==top || 0==right || 0==bottom) {
            isDrawProgress = false;
        }
        invalidate();
    }

    public void setDrawProgress() {
        this.isDrawProgress = true;
        invalidate();
    }

    /**
     * setFocused:设置已经获取焦点 <br/>
     *
     * @author:284891377 Date: 2016年6月30日 下午5:17:06
     * @since JDK 1.7
     */
    public void setFocused() {

        focusType = FOCUSED;
        invalidate();
    }

    public void  setOcr(int width, int height, int ocrRectW,int ocrRectH, int flag
    		,Bitmap bitmapScan, Bitmap bitmapFocus, Bitmap bitmapFocused) {
        this.flag = flag;
        this.width = width;
        this.height = height;
        this.ocrRectW = ocrRectW;
        this.ocrRectH = ocrRectH;
        this.mBitmapScan = bitmapScan;
        this.mBitmapFocus = bitmapFocus;
        this.mBitmapFocused = bitmapFocused;
        ocrRectLeft = (width - ocrRectW) / 2;
        ocrRectTop = (height - ocrRectH) / 2;
        postInvalidate();
    }

    /**
     * clearFocus:清除焦点 <br/>
     *
     * @author:284891377 Date: 2016年6月30日 下午5:17:06
     * @since JDK 1.7
     */
    public void clearFocus() {
        focusType = NOFOCUS;
        invalidate();
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setTextSize(dip2px(mContext, 18));
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setTextAlign(Paint.Align.CENTER);

        mRectPoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPoint.setColor(0xAA525252);

        mBankPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBankPaint.setColor(Color.GREEN);

        mPaint = new Paint();
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        mPaintText = new TextPaint();
        mPaintText.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (Contants.OCR_FLAG_BANKCARD==flag){
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                mBankCardTitle = "横版银行卡检测";
            }else{
                mBankCardTitle = "竖版银行卡检测";
            }
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 画四周mask层
        drawMask(canvas);
        //画四个角
        drawAct(canvas,mBankPaint);
        //画对齐线
        drawCardLine(canvas);

        if (null!=mBitmapScan) {
            drawScanAnimation(canvas, mBitmapScan, new Rect(ocrRectLeft,ocrRectTop,ocrRectLeft+ocrRectW,ocrRectTop+ocrRectH));//画扫描线
        }
    	
        // 画焦点
        if (null!=mBitmapFocus && null!=mBitmapFocused) {
	        if (focusType == FOCUSING) {
	            canvas.drawBitmap(mBitmapFocus, focusX - mBitmapFocus.getWidth() / 2, focusY - mBitmapFocus.getHeight() / 2, null);
	        } else if (focusType == FOCUSED) {
	            canvas.drawBitmap(mBitmapFocused, focusX - mBitmapFocused.getWidth() / 2, focusY - mBitmapFocused.getHeight() / 2, null);
	        }
        }
        
        // 画文字
        if (Contants.OCR_FLAG_BANKCARD==flag) {
            //画横竖版银行卡文字
            if (!TextUtils.isEmpty(mBankCardTitle)){
                canvas.drawText(mBankCardTitle, width / 2, (int) (ocrRectTop - dip2px(mContext, 10)), mLinePaint);
            }
            canvas.drawText("请将银行卡与模板框对齐", width / 2, (int) (ocrRectTop + ocrRectH + dip2px(mContext, 20)), mLinePaint);
        } else if (Contants.OCR_FLAG_IDFRONT==flag) {
            canvas.drawText("请将身份证正面与模板框对齐", width / 2, (int) (ocrRectTop + ocrRectH + dip2px(mContext, 20)), mLinePaint);
        } else {
            canvas.drawText("请将身份证反面与模板框对齐", width / 2, (int) (ocrRectTop + ocrRectH + dip2px(mContext, 20)), mLinePaint);
        }

        //绘制进度提示
        if (isDrawProgress) {
            drawProgress(canvas);
        }
    }

    private void drawCardLine(Canvas canvas) {
        //左
        if (left == 1) {
            Rect leftLine = new Rect(ocrRectLeft, ocrRectTop, ocrRectLeft + dip2px(mContext, 4), ocrRectTop + ocrRectH);
            canvas.drawRect(leftLine, mBankPaint);
        }
        //右
        if (right == 1) {
            Rect rightLine = new Rect(ocrRectLeft + ocrRectW - dip2px(mContext, 4), ocrRectTop, ocrRectLeft + ocrRectW, ocrRectTop + ocrRectH);
            canvas.drawRect(rightLine, mBankPaint);
        }
        //顶
        if (top == 1) {
            Rect topLine = new Rect(ocrRectLeft, ocrRectTop, ocrRectLeft + ocrRectW, ocrRectTop + dip2px(mContext, 4));
            canvas.drawRect(topLine, mBankPaint);
        }
        //底
        if (bottom == 1) {
            Rect bottomLine = new Rect(ocrRectLeft, ocrRectTop + ocrRectH - dip2px(mContext, 4), ocrRectLeft + ocrRectW, ocrRectTop + ocrRectH);
            canvas.drawRect(bottomLine, mBankPaint);
        }

        invalidate();
    }

    private void drawAct(Canvas canvas,Paint mPaint) {
        //左上角横
        Rect lTHorizontal = new Rect(ocrRectLeft, ocrRectTop, ocrRectLeft + dip2px(mContext, 30), ocrRectTop + dip2px(mContext, 4));
        canvas.drawRect(lTHorizontal, mPaint);
        //左上角竖
        Rect lTVertical = new Rect(ocrRectLeft, ocrRectTop, ocrRectLeft + dip2px(mContext, 4), ocrRectTop + dip2px(mContext, 30));
        canvas.drawRect(lTVertical, mPaint);
        //左下角横
        Rect lBHorizontal = new Rect(ocrRectLeft, ocrRectTop + ocrRectH - dip2px(mContext, 4), ocrRectLeft + dip2px(mContext, 30), ocrRectTop + ocrRectH);
        canvas.drawRect(lBHorizontal, mPaint);
        //左下角竖
        Rect lBVertical = new Rect(ocrRectLeft, ocrRectTop + ocrRectH - dip2px(mContext, 30), ocrRectLeft + dip2px(mContext, 4), ocrRectTop + ocrRectH);
        canvas.drawRect(lBVertical, mPaint);
        //右上角横
        Rect rTHorizontal = new Rect(ocrRectLeft + ocrRectW - dip2px(mContext, 30), ocrRectTop, ocrRectLeft + ocrRectW, ocrRectTop + dip2px(mContext, 4));
        canvas.drawRect(rTHorizontal, mPaint);
        //右上角竖
        Rect rTVertical = new Rect(ocrRectLeft + ocrRectW - dip2px(mContext, 4), ocrRectTop, ocrRectLeft + ocrRectW, ocrRectTop + dip2px(mContext, 30));
        canvas.drawRect(rTVertical, mPaint);
        //右下角横
        Rect rBHorizontal = new Rect(ocrRectLeft + ocrRectW - dip2px(mContext, 30), ocrRectTop + ocrRectH - dip2px(mContext, 4), ocrRectLeft + ocrRectW, ocrRectTop + ocrRectH);
        canvas.drawRect(rBHorizontal, mPaint);
        //右下角竖
        Rect rBVertical = new Rect(ocrRectLeft + ocrRectW - dip2px(mContext, 4), ocrRectTop + ocrRectH - dip2px(mContext, 30), ocrRectLeft + ocrRectW, ocrRectTop + ocrRectH);
        canvas.drawRect(rBVertical, mPaint);
    }

    private void drawMask(Canvas canvas) {
        // TODO 部分手机拍照变灰
        Rect frameTop = new Rect(0, 0, width, ocrRectTop);
        canvas.drawRect(frameTop, mRectPoint);
        Rect frameLeft = new Rect(0, ocrRectTop, ocrRectLeft, ocrRectTop + ocrRectH);
        canvas.drawRect(frameLeft, mRectPoint);
        Rect frameRight = new Rect(ocrRectLeft + ocrRectW, ocrRectTop, width, ocrRectTop + ocrRectH);
        canvas.drawRect(frameRight, mRectPoint);
        Rect frameBottom = new Rect(0, ocrRectTop + ocrRectH, width, height);
        canvas.drawRect(frameBottom, mRectPoint);
    }

    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * scale + 0.5f);
        return px;
    }

    public static float px2dip(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        float dp = (px - 0.5f) / scale;
        return dp;
    }

    public float AngleToRadian(float angle) {
        return (3.1415926f/180.0f)*angle;
    }

    public void drawProgress(Canvas canvas) {
        Rect rect1 = new Rect(0,0,width,height);
        Rect rect2 = new Rect(
                ocrRectLeft+ocrRectW/8
                ,ocrRectTop+ocrRectH/8
                ,ocrRectLeft+ocrRectW/8+ocrRectW*6/8
                ,ocrRectTop+ocrRectH/8+ocrRectH*6/8);

        int padding = dip2px(mContext, 10);
        float hwRounds = rect2.height() * 0.85f;
        PointF ptCenter = new PointF(rect1.width()/2, rect1.height()/2);
        float rOuter = (Math.min(Math.min(rect2.height(), rect2.width()),hwRounds) - padding) / 10.0f;
        float rInter = rOuter * 4.0f;

        mPaint.setColor(mClrCircleBigOuter);
        canvas.drawCircle(ptCenter.x, ptCenter.y, rInter, mPaint);

        mPaint.setColor(mClrCircleBigInter);
        canvas.drawCircle(ptCenter.x, ptCenter.y, rInter - dip2px(mContext, 2),mPaint);

        mDetAngle += 10;//增量步长
        int count = 3;//数量
        int dist = 360 / count;//每个间隔
        for (int i=0; i<count; ++i) {
            float angle =  (i * dist - mDetAngle) % 360;
            float radian = AngleToRadian(angle);
            float x = ptCenter.x + (float)Math.sin(radian)*rInter;
            float y = ptCenter.y + (float)Math.cos(radian)*rInter;
            mPaint.setColor(mClrCircleSmall);
            canvas.drawCircle(x, y, rOuter, mPaint);
        }

        mPaintText.setTextSize(px2dip(mContext,rect2.height()/5));
        canvas.drawText("正在识别中...", ptCenter.x, ptCenter.y, mPaintText);
    }

}
