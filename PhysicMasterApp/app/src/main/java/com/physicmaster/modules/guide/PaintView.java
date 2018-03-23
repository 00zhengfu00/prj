package com.physicmaster.modules.guide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;

/**
 * Created by huashigen on 2017-07-05.
 */

public class PaintView extends RelativeLayout {
    private Paint mPaint;
    private static final int TEXTSIZE = 20;//sp
    private Button btnOk;
    private OnClickListener onClickListener;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        mPaint = new Paint();
//        mPaint.setColor(Color.WHITE);
//        mPaint.setAntiAlias(true);
//        mPaint.setTextSize(getResources().getDisplayMetrics().density * TEXTSIZE);
//        mPaint.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
// "fonts/happy_font" +
//                ".ttf"));
        TextView tvTitle = new TextView(getContext());
        tvTitle.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "fonts/happy_font.ttf"));
        tvTitle.setText("温馨提示");
        tvTitle.setTextSize(TEXTSIZE);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setId(1);
        RelativeLayout.LayoutParams paramsTitle = new LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTitle.leftMargin = (int) (20 * getResources().getDisplayMetrics().density);
        paramsTitle.topMargin = (int) (150 * getResources().getDisplayMetrics().density);
        tvTitle.setLayoutParams(paramsTitle);

        TextView tvContent = new TextView(getContext());
        tvContent.setTypeface(Typeface.createFromAsset(getResources().getAssets(),
                "fonts/happy_font.ttf"));
        tvContent.setText("游客身份体验，学习数据将不被保存，请尽快以注册账号登录使用哦！");
        tvContent.setTextSize(TEXTSIZE);
        tvContent.setTextColor(Color.WHITE);
        tvContent.setLineSpacing(0, 1.2f);
        tvContent.setId(2);
        RelativeLayout.LayoutParams paramsContent = new LayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsContent.addRule(CENTER_HORIZONTAL);
        paramsContent.addRule(BELOW, 1);
        paramsContent.leftMargin = (int) (20 * getResources().getDisplayMetrics().density);
        paramsContent.rightMargin = (int) (20 * getResources().getDisplayMetrics().density);
        paramsContent.topMargin = (int) (20 * getResources().getDisplayMetrics().density);
        tvContent.setLayoutParams(paramsContent);

        btnOk = new Button(getContext());
        btnOk.setBackground(getResources().getDrawable(R.mipmap.iknow));
        LayoutParams btnParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT);
        btnParams.addRule(BELOW, 2);
        btnParams.addRule(CENTER_HORIZONTAL);
        btnParams.topMargin = (int) (40 * getResources().getDisplayMetrics().density);
        btnOk.setLayoutParams(btnParams);

        addView(tvTitle);
        addView(tvContent);
        addView(btnOk);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setBtnOkOnClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            btnOk.setOnClickListener(onClickListener);
        }
    }
}
