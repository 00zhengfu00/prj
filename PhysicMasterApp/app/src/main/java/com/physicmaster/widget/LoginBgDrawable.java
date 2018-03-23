package com.physicmaster.widget;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2017-06-28.
 */

public class LoginBgDrawable extends Drawable {
    private final int moveMaxInstance = 20;//px
    private List<Circle> circles;
    private final int BACKGROUNDCOLOR = 0xff2C93D6;
    private final int CIRCLECOLOR = 0xff3599D7;
    private Paint circlePaint;
    private Paint bgPaint;
    private static final String TAG = "LoginBgDrawable";
    private Logger logger = AndroidLogger.getLogger(TAG);

    public LoginBgDrawable(Resources resource) {
        //初始化背景paint画笔
        bgPaint = new Paint();
        bgPaint.setColor(BACKGROUNDCOLOR);
        //初始化圆圈paint画笔
        circlePaint = new Paint();
        circlePaint.setColor(CIRCLECOLOR);
        circlePaint.setAntiAlias(true);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (null == circles || 0 == circles.size()) {
            return;
        }
        canvas.drawPaint(bgPaint);
        for (Circle circle : circles) {
            canvas.drawCircle(circle.curX, circle.curY, circle.radius, circlePaint);
            moveCircle(circle);
        }
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 圆圈的移动-随机方向运动一定距离然后返回
     *
     * @param circle
     */
    private void moveCircle(Circle circle) {
        if (((circle.curX + circle.deltaX) < circle.x + moveMaxInstance) && ((circle.curX +
                circle.deltaX) >
                circle.x - moveMaxInstance)) {
            circle.curX += circle.deltaX;
//            logger.debug("curX:" + circle.curX);
        } else {
            //
            SecureRandom sRandom = new SecureRandom();
            circle.deltaX = sRandom.nextInt(4) - 2;
            if (circle.deltaX == 0) {
                circle.deltaX = 1;
            }
//            logger.debug("deltaX:" + circle.deltaX);
        }
        if (((circle.curY + circle.deltaY) < circle.y + moveMaxInstance) && ((circle.curY +
                circle.deltaY) >
                circle.y - moveMaxInstance)) {
            circle.curY += circle.deltaY;
//            logger.debug("curY:" + circle.curY);
        } else {
            //
            SecureRandom sRandom = new SecureRandom();
            circle.deltaY = sRandom.nextInt(4) - 2;
            if (circle.deltaY == 0) {
                circle.deltaY = 1;
            }
//            logger.debug("deltaY:" + circle.deltaY);
        }
        invalidateSelf();
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        int boundWidth = bounds.width();
        int boundHeight = bounds.height();
        if (bounds.height() > 0 && bounds.width() > 0) {
            circles = new ArrayList<>();
            Circle circle1 = new Circle(boundWidth / 4, boundHeight / 7, boundWidth / 4);
            circles.add(circle1);
            Circle circle2 = new Circle(boundWidth * 4 / 5, boundHeight * 2 / 20, boundWidth / 10);
            circles.add(circle2);
            Circle circle3 = new Circle(boundWidth * 3 / 5, boundHeight / 20, boundWidth / 10);
            circles.add(circle3);
            Circle circle4 = new Circle(boundWidth * 2 / 3, boundHeight / 3, boundWidth / 3);
            circles.add(circle4);
            Circle circle5 = new Circle(boundWidth / 4, boundHeight / 2, boundWidth / 4);
            circles.add(circle5);
            Circle circle6 = new Circle(boundWidth / 6, boundHeight * 3 / 4, boundWidth / 5);
            circles.add(circle6);
            Circle circle7 = new Circle(boundWidth * 5 / 8, boundHeight * 3 / 4, boundWidth / 5);
            circles.add(circle7);
            Circle circle8 = new Circle(boundWidth, boundHeight * 5 / 8, boundWidth / 4);
            circles.add(circle8);
            Circle circle9 = new Circle(boundWidth / 4, boundHeight * 9 / 10, boundWidth / 6);
            circles.add(circle9);
            Circle circle10 = new Circle(boundWidth * 3 / 4, boundHeight * 11 / 12, boundWidth / 6);
            circles.add(circle10);
        }
    }

    static class Circle {
        //运动增量x
        int deltaX;
        //运动增量y
        int deltaY;
        //起始x坐标
        final int x;
        //起始y坐标
        final int y;
        //当前x坐标
        int curX;
        //当前y坐标
        int curY;
        //圆半径
        final int radius;

        Circle(int x, int y, int radius) {
            deltaX = 10;
            deltaY = 10;
            this.x = x;
            this.y = y;
            this.radius = radius;
            curX = x;
            curY = y;
        }
    }
}
