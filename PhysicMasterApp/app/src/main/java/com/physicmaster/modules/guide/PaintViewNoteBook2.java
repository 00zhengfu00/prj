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

/**
 * Created by huashigen on 2017-07-05.
 */

public class PaintViewNoteBook2 extends RelativeLayout {
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

    public PaintViewNoteBook2(Context context) {
        this(context, null);
    }

    public PaintViewNoteBook2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintViewNoteBook2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        imageHand = new ImageView(getContext());
        imageHand.setImageResource(R.mipmap.hand);
        addView(imageHand);

        tv1 = new TextView(getContext());
        tv1.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "fonts/happy_font.ttf"));
        tv1.setText("点击进入自录错题页\n图片、文字、标签任意加");
        tv1.setTextSize(TEXTSIZE);
        tv1.setTextColor(Color.WHITE);
        addView(tv1);
        invalidate();
        new Handler().postDelayed(() -> startHandAnimation(), 500);
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

        imageLine = new ImageView(getContext());
        imageLine.setImageResource(R.mipmap.line1);
        addView(imageLine);

        tv2 = new TextView(getContext());
        tv2.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "fonts/happy_font.ttf"));
        tv2.setText("三科闯关的错题越多水位越高\n及时清理获金币，米卡也免受水淹之灾");
        tv2.setTextSize(TEXTSIZE);
        tv2.setTextColor(Color.WHITE);

        addView(tv2);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        image1.layout(view1.getLeft() - SPACE, view1.getTop() - SPACE, view1.getRight() + SPACE, view1.getBottom() + SPACE);

        imageHand.setRotation(45.0f);
        imageHand.layout(r - imageHand.getWidth(), (int) (image1.getBottom() + 10 * desity), r,
                (int) (image1.getBottom() + 10 * desity + imageHand.getHeight()));
        tv1.layout(getWidth() - tv1.getWidth(), imageHand.getBottom(), getWidth(), imageHand.getBottom() + tv1.getHeight());

        image2.layout(view2.getLeft() - SPACE, view2.getTop() - SPACE, view2.getRight() + SPACE, view2.getBottom() + SPACE);
        imageLine.layout(r / 2, image2.getTop() - imageLine.getHeight(), r / 2 + imageLine.getWidth(), image2.getTop());
        tv2.layout((r - tv2.getWidth()) / 2, imageLine.getTop() - tv2.getHeight(), (r - tv2.getWidth()) / 2 + tv2.getWidth(), imageLine.getTop());
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
            image1.setOnClickListener(onClickListener);
        }
    }
}
