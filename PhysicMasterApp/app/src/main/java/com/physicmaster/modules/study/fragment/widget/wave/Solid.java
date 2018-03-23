package com.physicmaster.modules.study.fragment.widget.wave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by John on 2014/10/15.
 */
class Solid extends View {

    private Paint aboveWavePaint;
    private Paint belowWavePaint;

    public Solid(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Solid(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        float desity = getResources().getDisplayMetrics().density;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, (int) (30 * desity));
        setLayoutParams(params);
    }

    public void setAboveWavePaint(Paint aboveWavePaint) {
        this.aboveWavePaint = aboveWavePaint;
    }

    public void setBelowWavePaint(Paint belowWavePaint) {
        this.belowWavePaint = belowWavePaint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(getLeft(), 0, getRight(), getBottom(), belowWavePaint);
        canvas.drawRect(getLeft(), 0, getRight(), getBottom(), aboveWavePaint);
    }
}
