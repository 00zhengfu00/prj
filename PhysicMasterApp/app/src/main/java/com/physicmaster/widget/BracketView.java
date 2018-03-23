package com.physicmaster.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by huashigen on 2017-07-21.
 */

public class BracketView extends View {
    private Paint mPaint;

    public BracketView(Context context) {
        this(context, null);
    }

    public BracketView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BracketView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getWidth();
        int height = getHeight();
    }
}
