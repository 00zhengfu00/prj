package com.physicmaster.modules.mine.activity.notebook.cameratool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huashigen on 2017-12-22.
 */

public class ReferenceLine extends View {
    private Paint mPaint;
    private static final int strokeWidth = 1;//dp

    public ReferenceLine(Context context) {
        this(context, null);
        init();
    }

    public ReferenceLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public ReferenceLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(getResources().getDisplayMetrics().density * strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画一个九宫格的参考线
        int screenWidth = getWidth();
        int screenHeight = getHeight();
        //第一条横线
        canvas.drawLine(0, screenHeight / 3, screenWidth, screenHeight / 3, mPaint);
        //第二条横线
        canvas.drawLine(0, screenHeight / 3 * 2, screenWidth, screenHeight / 3 * 2, mPaint);
        //第一条竖线
        canvas.drawLine(screenWidth / 3, 0, screenWidth / 3, screenHeight, mPaint);
        //第二条竖线
        canvas.drawLine(screenWidth / 3 * 2, 0, screenWidth / 3 * 2, screenHeight, mPaint);
    }
}
