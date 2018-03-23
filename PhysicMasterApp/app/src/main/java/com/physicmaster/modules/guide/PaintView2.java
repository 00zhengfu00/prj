package com.physicmaster.modules.guide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;

import static android.R.attr.animation;
import static com.physicmaster.R.id.add;
import static com.physicmaster.R.id.image;
import static com.physicmaster.R.id.tv;
import static com.physicmaster.R.id.tv2;
import static com.physicmaster.R.id.view;
import static com.physicmaster.R.id.view2;
import static com.physicmaster.R.id.view3;

/**
 * Created by huashigen on 2017-07-05.
 */

public class PaintView2 extends RelativeLayout {
    private Paint mPaint;
    private static final int TEXTSIZE = 20;//sp
    private float desity = 1.0f;
    private ImageView btnSwitch;
    private ImageView image2, imageLine, imageHand;
    private TextView tv1, tv2;
    private OnClickListener onClickListener;
    private ExposureView view1;
    private ExposureView view2;
    private int SPACE = 4;//DP

    public PaintView2(Context context) {
        this(context, null);
    }

    public PaintView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        btnSwitch = new ImageView(getContext());
        btnSwitch.setBackground(getResources().getDrawable(R.drawable.dashed_rectangle));
        if (view.getBitmap() != null) {
            btnSwitch.setImageBitmap(view.getBitmap());
        }
        addView(btnSwitch);

        imageHand = new ImageView(getContext());
        imageHand.setImageResource(R.mipmap.hand);
        addView(imageHand);

        tv1 = new TextView(getContext());
        tv1.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "fonts/happy_font.ttf"));
        tv1.setText("点击这里切换章节");
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
        TranslateAnimation animation = new TranslateAnimation(-moveDistans, moveDistans,
                -moveDistans, moveDistans);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        animation.setRepeatCount(3);
        imageHand.startAnimation(animation);
    }

    public void addView2(ExposureView view) {
        view2 = view;
        image2 = new ImageView(getContext());
        image2.setBackground(getResources().getDrawable(R.drawable.dashed_rectangle));
        if (view.getBitmap() != null) {
            image2.setImageBitmap(view.getBitmap());
        }
        LayoutParams btnParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        image2.setLayoutParams(btnParams);
        addView(image2);

        imageLine = new ImageView(getContext());
        imageLine.setImageResource(R.mipmap.line1);
        addView(imageLine);

        tv2 = new TextView(getContext());
        tv2.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "fonts/happy_font.ttf"));
        tv2.setText("每个知识海星代表一个知识点，\n快来征服并点亮它们吧！");
        tv2.setTextSize(TEXTSIZE);
        tv2.setTextColor(Color.WHITE);

        addView(tv2);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        btnSwitch.layout(view1.getLeft() - SPACE, view1.getTop() - SPACE, view1.getRight() + SPACE,
                view1.getBottom() + SPACE);
        imageHand.layout(btnSwitch.getRight() - (int) (20 * getResources().getDisplayMetrics()
                        .density),
                (int) (btnSwitch.getBottom() - 20 * desity),
                (int) (btnSwitch.getRight() - 20 * desity +
                        imageHand.getWidth()), (int) (btnSwitch.getBottom() - 20 * getResources()
                        .getDisplayMetrics().density + imageHand.getHeight()));
        tv1.layout((int) (imageHand.getLeft() - 20 * desity),
                (int) (imageHand.getBottom() + 10 * desity),
                (int) (imageHand.getLeft() - 20 * getResources().getDisplayMetrics()
                        .density + tv1.getWidth()), (int) (imageHand.getBottom() + 10 *
                        desity + tv1.getHeight()));
        image2.layout(view2.getLeft() - SPACE, view2.getTop() - SPACE, view2.getRight() + SPACE,
                view2.getBottom() + SPACE);
        imageLine.layout(view2.getLeft() - imageLine.getWidth(), view2.getTop() +
                image2.getHeight() / 2 - imageLine.getHeight(), view2.getLeft(), view2.getTop() +
                image2.getHeight() / 2);
        tv2.layout((getWidth() - tv2.getWidth()) / 2,
                imageLine.getTop() - tv2.getHeight(), (getWidth() - tv2.getWidth()) / 2 +
                        tv2.getWidth(), imageLine.getTop());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect1 = new Rect(0, 0, getRight(), view1.getTop());
        Rect rect2 = new Rect(0, view1.getTop(), view1.getLeft(), view1.getBottom());
        Rect rect3 = new Rect(view1.getRight(), view1.getTop(), getRight(), view1.getBottom());
        Rect rect4 = new Rect(0, view1.getBottom(), getRight(), view2.getTop());
        Rect rect5 = new Rect(0, view2.getTop(), view2.getLeft(), view2.getBottom());
        Rect rect6 = new Rect(view2.getRight(), view2.getTop(), getRight(), view2.getBottom());
        Rect rect7 = new Rect(0, view2.getBottom(), getRight(), getBottom());
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
            btnSwitch.setOnClickListener(onClickListener);
        }
    }
}
