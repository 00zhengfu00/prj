package com.physicmaster.modules.study.fragment.widget.dynamicbg;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;

import com.physicmaster.R;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by huashigen on 2017-06-26.
 */

public class DynamicBackgroundDrawable extends Drawable implements RecyclerViewScrollToPercentageListener.Listener {
    private final Paint paintBg = new Paint();
    private final Paint paintStar = new Paint();
    private final int color1;
    private final int color2;
    private final int density;
    private final int headerLen;
    private float scrollRatio = 100.0F;//滚动距离比值
    private int scrollOffset = 0;
    //水泡
    private final Scenery[] bubbles;
    //龙虾
    private Scenery lonster;
    //水母
    private Scenery jellyFish;
    //大鱼
    private Scenery bigFish;
    //波浪
    private Scenery wave1, wave2;
    private boolean isInited = false;
    private static final String TAG = "DynamicBackgroundDrawable";
    private RecyclerView recyclerView;
    private Bitmap[] bubblesBitmap;
    private Bitmap wave, bigFishBitmap, jellyFishBitmap, lonsterBitmap;

    public DynamicBackgroundDrawable(Resources paramResources) {
        this.color1 = paramResources.getColor(R.color.startColor);
        this.color2 = paramResources.getColor(R.color.endColor);
        this.paintBg.setColor(color1);
        //防抖动
        this.paintBg.setDither(true);
        this.paintStar.setColor(0xffffffff);
        this.paintStar.setDither(true);
        this.density = Math.max(1, (int) TypedValue.applyDimension(1, 1.0F, paramResources.getDisplayMetrics()));
        this.headerLen = paramResources.getDimensionPixelSize(R.dimen.dimen_280);
        this.bubbles = new Scenery[4];
        this.bubblesBitmap = new Bitmap[4];
        this.lonsterBitmap = ((BitmapDrawable) paramResources.getDrawable(R.mipmap.lonster)).getBitmap();
        this.bigFishBitmap = ((BitmapDrawable) paramResources.getDrawable(R.mipmap.bigfish)).getBitmap();
        this.bubblesBitmap[0] = ((BitmapDrawable) paramResources.getDrawable(R.mipmap.bubble1)).getBitmap();
        this.bubblesBitmap[1] = ((BitmapDrawable) paramResources.getDrawable(R.mipmap.bubble2)).getBitmap();
        this.bubblesBitmap[2] = ((BitmapDrawable) paramResources.getDrawable(R.mipmap.bubble3)).getBitmap();
        this.bubblesBitmap[3] = ((BitmapDrawable) paramResources.getDrawable(R.mipmap.bubble4)).getBitmap();
        this.wave = ((BitmapDrawable) paramResources.getDrawable(R.mipmap.wave_small)).getBitmap();
        this.jellyFishBitmap = ((BitmapDrawable) paramResources.getDrawable(R.mipmap.jellyfish)).getBitmap();
        this.bigFishBitmap = ((BitmapDrawable) paramResources.getDrawable(R.mipmap.bigfish)).getBitmap();
    }

    /**
     * 绘制星星
     *
     * @param paramArrayOfStar
     * @param paramCanvas
     */
    private void drawBubbles(Scenery[] paramArrayOfStar, Canvas paramCanvas) {
        int k = 0;
        int number = paramArrayOfStar.length;
        while (k < number) {
            Scenery localStar = paramArrayOfStar[k];
            paramCanvas.drawBitmap(bubblesBitmap[k], localStar.x, localStar.y, paintBg);
            k++;
        }
    }

    public void attach(RecyclerView paramRecyclerView) {
        recyclerView = paramRecyclerView;
        paramRecyclerView.setBackground(this);
        paramRecyclerView.addOnScrollListener(new RecyclerViewScrollToPercentageListener(this));
    }

    @Override
    public void draw(@NonNull Canvas paramCanvas) {
        if (this.scrollRatio >= 50.0F) {
            float centerX = getBounds().centerX();
            float height = getBounds().height();
            int startColor = this.color1;
            int m = (int) (2.0F * (100.0F - this.scrollRatio));
            int endColor = this.color2;
            float f5 = m / 100.0F;
            int i2 = 0xFF & endColor >> 24;//取透明度
            int i3 = 0xFF & endColor >> 16;
            int i4 = 0xFF & endColor >> 8;
            int i5 = 0xFF & endColor;
            int i6 = 0xFF & startColor >> 24;
            int i7 = 0xFF & startColor >> 16;
            int i8 = 0xFF & startColor >> 8;
            int i9 = 0xFF & startColor;
            LinearGradient localLinearGradient = new LinearGradient(centerX, 0.0F, centerX,
                    height, startColor, i2 + (int) (f5 * (i6 - i2)) << 24 | i3 + (int) (f5 * (i7 - i3)) << 16 | i4 + (int)
                    (f5 * (i8 - i4)) << 8 | i5 + (int) (f5 * (i9 - i5)), Shader.TileMode.CLAMP);
            this.paintBg.setShader(localLinearGradient);
            paramCanvas.drawPaint(this.paintBg);
            if (jellyFish == null && scrollRatio > 60.0f) {
                this.jellyFish = new Scenery(0, -scrollOffset, jellyFishBitmap);
            }
            if (lonster == null && scrollRatio > 70.0f) {
                this.lonster = new Scenery(getBounds().width() - lonsterBitmap.getWidth() - 20 * density, -scrollOffset, lonsterBitmap);
            }
            if (bigFish == null && scrollRatio > 99.0f) {
                this.bigFish = new Scenery(getBounds().width() - bigFishBitmap.getWidth(), -scrollOffset + getBounds().width() * 3 / 4, bigFishBitmap);
            }
        } else {
            this.paintBg.setColor(this.color1);
            this.paintBg.setShader(null);
            paramCanvas.drawPaint(this.paintBg);
        }
        paramCanvas.save();
        paramCanvas.translate(0.0F, scrollOffset);
        drawWaves(paramCanvas);
        drawBubbles(this.bubbles, paramCanvas);
        drawLonster(paramCanvas);
        drawJellyFishs(paramCanvas);
        drawBigfish(paramCanvas);
        paramCanvas.restore();
    }

    private void drawJellyFishs(Canvas canvas) {
        if (null == jellyFish) {
            return;
        }
        canvas.drawBitmap(jellyFishBitmap, jellyFish.x, jellyFish.y, paintBg);
    }

    private void drawLonster(Canvas canvas) {
        if (null == lonster) {
            return;
        }
        canvas.drawBitmap(lonsterBitmap, lonster.x, lonster.y, paintBg);
    }

    private void drawBigfish(Canvas canvas) {
        if (null == bigFish) {
            return;
        }
        canvas.drawBitmap(bigFishBitmap, bigFish.x, bigFish.y, paintBg);
    }

    //画白色波浪
    private void drawWaves(Canvas canvas) {
        canvas.drawBitmap(wave, wave1.x, wave1.y, paintBg);
        canvas.drawBitmap(wave, wave2.x, wave2.y, paintBg);
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
    protected void onBoundsChange(Rect paramRect) {
        super.onBoundsChange(paramRect);
        if ((paramRect.height() > 0) && (paramRect.width() > 0) && (!this.isInited)) {
            SecureRandom localSecureRandom = new SecureRandom();
            int m = paramRect.width();
            int i2 = paramRect.height() * 2;
            for (int i1 = 0; i1 < this.bubbles.length; i1++) {
                this.bubbles[i1] = createScenery(localSecureRandom, headerLen, m, i2, bubblesBitmap[i1]);
            }
            this.wave1 = new Scenery(40 * density, headerLen, wave);
            this.wave2 = new Scenery(paramRect.width() - 20 * density - wave.getWidth(), headerLen + 20 * density, wave);
            this.isInited = true;
        }
    }

    private Scenery createScenery(Random paramRandom, int start, int x, int
            y, Bitmap bitmap) {
        return new Scenery(paramRandom.nextInt(x), paramRandom.nextInt(y) + start, bitmap);
    }

    @Override
    public void onScroll(float scrollRatio, int scrollOffset) {
        this.scrollRatio = scrollRatio;
        this.scrollOffset = scrollOffset;
        invalidateSelf();
        if (onScrollListener != null) {
            onScrollListener.onScroll(scrollRatio);
        }
    }

    static class Scenery {
        final float x;
        final float y;
        final Bitmap bitmap;

        Scenery(float x, float y, Bitmap bitmap) {
            this.x = x;
            this.y = y;
            this.bitmap = bitmap;
        }
    }

    /**
     * 将所有景物参数置为null，根据新位置重新绘制
     */
    public void removeAllScenes() {
        jellyFish = null;
        lonster = null;
        bigFish = null;
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    private OnScrollListener onScrollListener;


    public interface OnScrollListener {
        public void onScroll(float scrollRatio);
    }
}
