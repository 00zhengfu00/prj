package com.physicmaster.modules.guide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;

import static com.physicmaster.R.id.view;

/**
 * Created by huashigen on 2017-07-05.
 */

public class PaintView3 extends RelativeLayout {
    private Paint mPaint;
    private static final int TEXTSIZE = 20;//sp
    private float desity = 1.0f;
    private ImageView image1;
    private ImageView image2, imageLine, imageHand;
    private TextView tv1, tv2;
    private OnClickListener onClickListener;
    private ExposureView view1;
    private ExposureView view2;
    private int SPACE = 4;//DP

    public PaintView3(Context context) {
        this(context, null);
    }

    public PaintView3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        imageLine.setImageResource(R.mipmap.line3);
        addView(imageLine);

        tv1 = new TextView(getContext());
        tv1.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "fonts/happy_font.ttf"));
        tv1.setText("可滚动切换章节");
        tv1.setTextSize(TEXTSIZE);
        tv1.setTextColor(Color.WHITE);
        addView(tv1);
        invalidate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startHandAnimation();
            }
        }, 500);
    }

    private void startHandAnimation() {
        float moveDistans = 6 * desity;
        TranslateAnimation animation = new TranslateAnimation(0, 0, -moveDistans, moveDistans);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setRepeatCount(3);
        imageHand.startAnimation(animation);
    }

    public void addView2(ExposureView view) {
        view2 = view;
        image2 = new ImageView(getContext());
        image2.setBackground(getResources().getDrawable(R.drawable.dashed_rectangle));
        addView(image2);

        imageHand = new ImageView(getContext());
        imageHand.setImageResource(R.mipmap.hand);
        addView(imageHand);

        tv2 = new TextView(getContext());
        tv2.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "fonts/happy_font.ttf"));
        tv2.setText("点击这里切换\n版本和年级");
        tv2.setTextSize(TEXTSIZE);
        tv2.setTextColor(Color.WHITE);

        addView(tv2);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        image1.layout(view1.getLeft() - SPACE, view1.getTop() - SPACE, view1.getRight() + SPACE, view1.getBottom() + SPACE);

        imageLine.layout(image1.getWidth() / 2, image1.getBottom(), image1.getWidth() / 2 + imageLine.getWidth(), image1.getBottom() + imageLine.getHeight());

        tv1.layout(imageLine.getLeft(), imageLine.getBottom(), imageLine.getLeft() + tv1.getWidth(), imageLine.getBottom() + tv1.getHeight());
        image2.layout(view2.getLeft() - SPACE, view2.getTop() - SPACE, view2.getRight() + SPACE, view2.getBottom() + SPACE);
        imageHand.setRotation(120.0f);
        imageHand.layout(view2.getLeft() - imageHand.getWidth(), (int) (image2.getTop() + 10 * desity), view2.getLeft(),
                (int) (image2.getTop() + 10 * desity + imageHand.getWidth()));
        tv2.layout(view2.getLeft() - tv2.getWidth(), imageHand.getBottom(), view2.getLeft(), imageHand.getBottom() + tv2.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect1 = new Rect(0, 0, getRight(), view1.getTop());
        Rect rect3 = new Rect(view1.getRight(), view1.getTop(), getRight(), view2.getTop());
        Rect rect2 = new Rect(0, view1.getBottom(), view1.getRight(), getBottom());
        Rect rect4 = new Rect(view1.getRight(), view2.getTop(), view2.getLeft(), getBottom());
        Rect rect5 = new Rect(view2.getLeft(), view2.getBottom(), getRight(), getBottom());
        Rect rect6 = new Rect(0, view1.getTop(), view1.getLeft(), view1.getBottom());
        Rect rect7 = new Rect(view2.getRight(), view2.getTop(), getRight(), view2.getBottom());
        canvas.drawRect(rect1, mPaint);
        canvas.drawRect(rect2, mPaint);
        canvas.drawRect(rect3, mPaint);
        canvas.drawRect(rect4, mPaint);
        canvas.drawRect(rect5, mPaint);
        canvas.drawRect(rect6, mPaint);
        canvas.drawRect(rect7, mPaint);
    }

    public void setBtnOkOnClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            image2.setOnClickListener(onClickListener);
        }
    }
}
