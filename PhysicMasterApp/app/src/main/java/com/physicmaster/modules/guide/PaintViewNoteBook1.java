package com.physicmaster.modules.guide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;

/**
 * Created by huashigen on 2017-07-05.
 */

public class PaintViewNoteBook1 extends RelativeLayout {
    private Paint mPaint;
    private static final int TEXTSIZE = 20;//sp
    private float desity = 1.0f;
    private ImageView image1;
    private ImageView imageLine, imageOk;
    private TextView tv1;
    private ExposureView view1;
    private int SPACE = 4;//DP

    public PaintViewNoteBook1(Context context) {
        this(context, null);
    }

    public PaintViewNoteBook1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintViewNoteBook1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(R.color.half_transparent));
        mPaint.setAntiAlias(true);
        desity = getResources().getDisplayMetrics().density;
        SPACE = (int) (SPACE * desity);
    }

    public void addView(ExposureView view) {
        view1 = view;
        image1 = new ImageView(getContext());
        image1.setBackground(getResources().getDrawable(R.drawable.dashed_rectangle));
        addView(image1);

        imageLine = new ImageView(getContext());
        imageLine.setImageResource(R.mipmap.line4);
        addView(imageLine);

        tv1 = new TextView(getContext());
        tv1.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/happy_font.ttf"));
        tv1.setText("添加目录\n快速找错题不再难");
        tv1.setTextSize(TEXTSIZE);
        tv1.setTextColor(Color.WHITE);
        addView(tv1);

        imageOk = new ImageView(getContext());
        imageOk.setImageResource(R.mipmap.iknow);
        addView(imageOk);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int left = (view1.getLeft() == 0) ? SPACE * 2 : view1.getLeft();
        int right = (view1.getRight() == r) ? view1.getRight() - SPACE * 2 : view1.getRight();
        image1.layout(left - SPACE, view1.getTop() - SPACE, right + SPACE, view1.getBottom() + SPACE);
        imageLine.layout(image1.getWidth() / 2, image1.getBottom(), image1.getWidth() / 2 + imageLine.getWidth(), image1.getBottom() + imageLine.getHeight());

        tv1.layout((r - tv1.getWidth()) / 2, imageLine.getBottom(), (r - tv1.getWidth()) / 2 + tv1.getWidth(), imageLine.getBottom() + tv1.getHeight());
        imageOk.layout((r - imageOk.getWidth()) / 2, tv1.getBottom(), (r - imageOk
                .getWidth()) / 2 + imageOk.getWidth(), tv1.getBottom() + (int) (20 * getResources().getDisplayMetrics().density) + imageOk.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect1 = new Rect(0, 0, getRight(), view1.getTop());
        int left = (view1.getLeft() == 0) ? SPACE * 2 : view1.getLeft();
        int right = (view1.getRight() == getWidth()) ? view1.getRight() - SPACE * 2 : view1.getRight();
        Rect rect2 = new Rect(0, view1.getTop(), left, view1.getBottom());
        Rect rect3 = new Rect(right, view1.getTop(), getRight(), view1.getBottom());
        Rect rect4 = new Rect(0, view1.getBottom(), getRight(), getBottom());
        canvas.drawRect(rect1, mPaint);
        canvas.drawRect(rect2, mPaint);
        canvas.drawRect(rect3, mPaint);
        canvas.drawRect(rect4, mPaint);
    }

    public void setOnBtnOkClickListener(OnClickListener onBtnOkClickListener) {
        imageOk.setOnClickListener(onBtnOkClickListener);
    }
}