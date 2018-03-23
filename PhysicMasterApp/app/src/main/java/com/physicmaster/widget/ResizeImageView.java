package com.physicmaster.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;

/**
 * Created by huashigen on 2017-10-17.
 */

public class ResizeImageView extends AppCompatImageView {
    public ResizeImageView(Context context) {
        super(context);
    }

    public ResizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
    }

    @Override
    public void setImageAlpha(int alpha) {
        super.setImageAlpha(alpha);
    }

    @Override
    public void setImageDrawable(@Nullable final Drawable drawable) {
        super.setImageDrawable(drawable);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                Drawable drawableDest = getDestDrawable(drawable, getResources().getDisplayMetrics().densityDpi, R.mipmap.wodewenda);
                setImageDrawable(drawableDest);
            }
        }, 1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Drawable getDestDrawable(Drawable drawable, int density, int resourceId) {
        int height = getHeight();
        int width = getWidth();
        int drawableHeight = drawable.getBounds().height();
        int drawableWidth = drawable.getBounds().width();
        //如果当前控件大小大于drawable大小,那么图片展示出来就会模糊,此时应该寻求更高分辨率的图片
        if (height > drawableHeight && width > drawableWidth) {
            density += 1;
            drawable = getResources().getDrawableForDensity(resourceId, density);
            int resourceIds = getResources().getIdentifier("wodewenda", "mipmap", BaseApplication.getPackageName());
            return getDestDrawable(drawable, density, resourceId);
        } else {
            return drawable;
        }
    }

    @Override
    public void setImageIcon(@Nullable Icon icon) {
        super.setImageIcon(icon);
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
    }

    @Override
    public void setImageMatrix(Matrix matrix) {
        super.setImageMatrix(matrix);
    }

    @Override
    public void setImageLevel(int level) {
        super.setImageLevel(level);
    }

    @Override
    public void setImageTintList(@Nullable ColorStateList tint) {
        super.setImageTintList(tint);
    }

    @Override
    public void setImageState(int[] state, boolean merge) {
        super.setImageState(state, merge);
    }

    @Override
    public void setImageTintMode(@Nullable PorterDuff.Mode tintMode) {
        super.setImageTintMode(tintMode);
    }

    public Drawable getPerfectDrawable(int resourceId) {
        Drawable drawble = getResources().getDrawable(resourceId);
        float density = getResources().getDisplayMetrics().density;
        return getDestDrawable(drawble, (int) density, resourceId);
    }
}
