package com.physicmaster.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.physicmaster.R;

/**
 * Created by huashigen on 2016/12/19.
 */

public class FinishView extends View {
    private Paint paint;

    public FinishView(Context context) {
        this(context, null);
    }

    public FinishView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FinishView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int halfWidth = width / 2;
        //画蓝色背景圆
        paint.setColor(getResources().getColor(R.color.colorTitleBlue));
        paint.setAntiAlias(true);
        canvas.drawCircle(halfWidth, halfWidth, halfWidth, paint);

        //画白色圆圈
        paint.setColor(Color.WHITE);
        canvas.drawCircle(halfWidth, halfWidth, halfWidth * 0.9f, paint);

        //画中心蓝色圆圈
        paint.setColor(getResources().getColor(R.color.colorTitleBlue));
        canvas.drawCircle(halfWidth, halfWidth, halfWidth * 0.85f, paint);
        Drawable drawable = getResources().getDrawable(R.mipmap.finish_tick);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        canvas.drawBitmap(bitmap, halfWidth * 0.6f, halfWidth * 0.7f, paint);
//        canvas.drawBitmap(BitmapUtils.);
        //画对勾(两个矩形)
//        R.mipmap.finish_tick;
//        paint.setColor(Color.WHITE);
//        paint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.dimen_40));
//        canvas.drawLine(halfWidth * 0.8f, halfWidth * 1.1f, halfWidth * 1.3f, halfWidth * 0.6f,
//                paint);
//        paint.setColor(getResources().getColor(R.color.colorTitleBlue));
//        canvas.drawLine(halfWidth * 0.8f, halfWidth * 1.1f - getResources().getDimensionPixelSize
//                (R.dimen.dimen_18), halfWidth * 1.3f - getResources().getDimensionPixelSize(R
//                .dimen.dimen_10), halfWidth * 0.6f - getResources().getDimensionPixelSize(R.dimen
//                .dimen_10), paint);
    }
}
