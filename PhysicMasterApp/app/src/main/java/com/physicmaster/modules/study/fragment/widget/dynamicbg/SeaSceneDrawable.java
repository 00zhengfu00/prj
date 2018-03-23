package com.physicmaster.modules.study.fragment.widget.dynamicbg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.physicmaster.R;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2017-06-28.
 */

public class SeaSceneDrawable extends Drawable {
    private final int moveMaxInstance = 1000;//px
    private float density;
    private final Paint paintBg = new Paint();
    private final Paint paintScene = new Paint();
    private List<Cloud> firstGroup = new ArrayList<>();
    private List<Cloud> secondGroup = new ArrayList<>();
    private List<Cloud> thirdGroup = new ArrayList<>();
    private Bitmap bitmapSun, bitmapWave, bitmapLight, bitmapBird1, bitmapBird2;
    private static final String TAG = "SeaSceneDrawable";
    private Logger logger = AndroidLogger.getLogger(TAG);
    private RectF waveRect;
    private RectF sunRect;
    private RectF lightRect;
    private RectF bird1Rect;
    private RectF bird2Rect;

    public SeaSceneDrawable(Resources resources) {
        paintBg.setColor(0xff50D7FD);
        paintScene.setAntiAlias(true);
        bitmapSun = BitmapFactory.decodeResource(resources, R.mipmap.sun);
        bitmapWave = BitmapFactory.decodeResource(resources, R.mipmap.wave);
        bitmapLight = BitmapFactory.decodeResource(resources, R.mipmap.lighthouse);
        bitmapBird1 = BitmapFactory.decodeResource(resources, R.mipmap.bird1);
        bitmapBird2 = BitmapFactory.decodeResource(resources, R.mipmap.bird2);
        density = resources.getDisplayMetrics().density;
    }

    private Handler handler = new Handler();

    @Override
    public void draw(@NonNull Canvas canvas) {
        //画背景
        canvas.drawPaint(paintBg);
        //画太阳
        canvas.drawBitmap(bitmapSun, null, sunRect, paintBg);
        //画第一层云
        drawFirstGroupClouds(canvas);
        //画第二层云
        drawSecondGroupClouds(canvas);
        //画第三层云
        drawThirdGroupClouds(canvas);
        //画静态波浪
        canvas.drawBitmap(bitmapWave, null, waveRect, paintBg);
        //画灯塔
        canvas.drawBitmap(bitmapLight, null, lightRect, paintBg);
        //画海鸥-灯塔附近
        canvas.drawBitmap(bitmapBird1, null, bird1Rect, paintBg);
        canvas.drawBitmap(bitmapBird2, null, bird2Rect, paintBg);
    }

    private void drawThirdGroupClouds(Canvas canvas) {
        paintScene.setColor(0xffffffff);
        for (Cloud cloud : thirdGroup) {
            canvas.drawCircle(cloud.curX, cloud.curY, cloud.radius, paintScene);
        }
    }

    private void drawSecondGroupClouds(Canvas canvas) {
        paintScene.setColor(0xffd8f5fd);
        for (Cloud cloud : secondGroup) {
            canvas.drawCircle(cloud.curX, cloud.curY, cloud.radius, paintScene);
        }
    }

    private void drawFirstGroupClouds(Canvas canvas) {
        paintScene.setColor(0xff95e7fd);
        for (Cloud cloud : firstGroup) {
            canvas.drawCircle(cloud.curX, cloud.curY, cloud.radius, paintScene);
        }
    }

    private void moveCloud(Cloud cloud, float deltaX) {
        cloud.curX = cloud.x + deltaX;
    }

    private void moveBackCloud(Cloud cloud, float deltaX) {
        cloud.curX = cloud.curX + deltaX;
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

            Cloud fCloud1 = new Cloud(boundWidth / 4, boundHeight * 5 / 12 + 10 * density, boundWidth / 8);
            firstGroup.add(fCloud1);
            Cloud fCloud2 = new Cloud(boundWidth * 5 / 12, boundHeight / 2, boundWidth / 6);
            firstGroup.add(fCloud2);
            Cloud fCloud3 = new Cloud(boundWidth * 3 / 5, boundHeight / 2, boundWidth / 6);
            firstGroup.add(fCloud3);
            Cloud fCloud4 = new Cloud(boundWidth * 5 / 6 - 10 * density, boundHeight * 2 / 3 - 10 * density, boundWidth / 6);
            firstGroup.add(fCloud4);
            Cloud fCloud5 = new Cloud(boundWidth * 9 / 10, boundHeight * 2 / 3, boundWidth / 6);
            firstGroup.add(fCloud5);

            Cloud sCloud1 = new Cloud(boundWidth / 10, boundHeight * 3 / 5, boundWidth / 6);
            secondGroup.add(sCloud1);
            Cloud sCloud2 = new Cloud(boundWidth / 4, boundHeight * 3 / 5, boundWidth / 5);
            secondGroup.add(sCloud2);
            Cloud sCloud3 = new Cloud(boundWidth / 2, boundHeight * 3 / 4, boundWidth / 5);
            secondGroup.add(sCloud3);
            Cloud sCloud4 = new Cloud(boundWidth * 2 / 3, boundHeight * 4 / 5, boundWidth / 6);
            secondGroup.add(sCloud4);
            Cloud sCloud5 = new Cloud(boundWidth * 6 / 7, boundHeight * 3 / 4, boundWidth / 5);
            secondGroup.add(sCloud5);

            Cloud tCloud1 = new Cloud(boundWidth / 10, boundHeight * 7 / 8, boundWidth / 6);
            thirdGroup.add(tCloud1);
            Cloud tCloud2 = new Cloud(boundWidth / 4 + 10 * density, boundHeight * 9 / 10, boundWidth / 8);
            thirdGroup.add(tCloud2);
            Cloud tCloud3 = new Cloud(boundWidth / 2, boundHeight * 7 / 8, boundWidth / 6);
            thirdGroup.add(tCloud3);
            Cloud tCloud4 = new Cloud(boundWidth * 2 / 3, boundHeight * 7 / 8, boundWidth / 6);
            thirdGroup.add(tCloud4);
            Cloud tCloud5 = new Cloud(boundWidth, boundHeight * 4 / 5, boundWidth / 6);
            thirdGroup.add(tCloud5);

            waveRect = new RectF(0, getBounds().height() * 9 / 10 - 22 * density, getBounds().width(), getBounds().height() * 9 / 10 - 22 * density +
                    bitmapWave.getHeight());
            sunRect = new RectF(getBounds().width() / 9 + 10 * density, getBounds().height()
                    / 9 + 10 * density, getBounds().width() / 9 + 10 * density + bitmapSun.getWidth(), getBounds().height()
                    / 9 + 10 * density + bitmapSun.getHeight());
            lightRect = new RectF(getBounds().width() * 4 / 5, getBounds().height() -
                    bitmapLight.getHeight() - 20 * density, getBounds().width() * 4 / 5 + bitmapLight.getWidth(), getBounds().height() -
                    bitmapLight.getHeight() - 20 * density + bitmapLight.getHeight());
            bird1Rect = new RectF(getBounds().width() * 4 / 5 - density * 20, getBounds()
                    .height() -
                    bitmapLight.getHeight() - 40 * density, getBounds().width() * 4 / 5 - density * 20 + bitmapBird1.getWidth(), getBounds()
                    .height() -
                    bitmapLight.getHeight() - 40 * density + bitmapBird1.getHeight());
            bird2Rect = new RectF(getBounds().width() * 4 / 5 + bitmapLight.getWidth(),
                    getBounds().height() - bitmapLight.getHeight() - 10 * density, getBounds().width() * 4 / 5 + bitmapLight.getWidth() + bitmapBird2.getWidth(),
                    getBounds().height() - bitmapLight.getHeight() - 10 * density + bitmapBird2.getHeight());
        }
    }

    static class Cloud {
        //运动增量x
        float deltaX;
        //运动增量y
        float deltaY;
        //起始x坐标
        final float x;
        //起始y坐标
        final float y;
        //当前x坐标
        float curX;
        //当前y坐标
        float curY;
        //圆半径
        final int radius;

        Cloud(float x, float y, int radius) {
            deltaX = 10;
            deltaY = 10;
            this.x = x;
            this.y = y;
            this.radius = radius;
            curX = x;
            curY = y;
        }
    }

    /**
     * 云层立即恢复到原始位置
     */
    private void resetCloudsPositon() {
        for (Cloud cloud : firstGroup) {
            cloud.curX = cloud.x;
        }
        for (Cloud cloud : secondGroup) {
            cloud.curX = cloud.x;
        }
        for (Cloud cloud : thirdGroup) {
            cloud.curX = cloud.x;
        }
        invalidateSelf();
    }

    public void moveClouds(float deltaY) {
        if (deltaY == 0) {
            moveBackClouds();
        } else if (-1 == deltaY) {
            resetCloudsPositon();
        } else {
            moveCloudsByGroup(1, deltaY);
            moveCloudsByGroup(2, deltaY);
            moveCloudsByGroup(3, deltaY);
            invalidateSelf();
        }
    }

    private void moveCloudsByGroup(int index, float deltax) {
        if (1 == index) {
            //移动第一组云
            for (Cloud cloud : firstGroup) {
                moveCloud(cloud, deltax);
            }
        } else if (2 == index) {
            //移动第二组云
            for (Cloud cloud : secondGroup) {
                moveCloud(cloud, -deltax);
            }
        } else {
            //移动第三组云
            for (Cloud cloud : thirdGroup) {
                moveCloud(cloud, deltax);
            }
        }
    }

    /**
     * 将所有云彩缓慢移动到原位
     */
    private void moveBackClouds() {
        handler.post(new RefreshProgressRunnable());
    }

    private class RefreshProgressRunnable implements Runnable {
        public void run() {
            synchronized (SeaSceneDrawable.this) {
                long start = System.currentTimeMillis();

                calculatePath();

                invalidateSelf();
                //每秒约60帧
                long gap = 5 - (System.currentTimeMillis() - start);
                Cloud cloud = firstGroup.get(0);
                if (cloud.curX > cloud.x) {
                    handler.postDelayed(this, 0);
                }
            }
        }
    }

    private void calculatePath() {
        for (int i = 0; i < 5; i++) {
            moveBackCloud(firstGroup.get(i), -5);
            moveBackCloud(secondGroup.get(i), 5);
            moveBackCloud(thirdGroup.get(i), -5);
        }
    }
}
