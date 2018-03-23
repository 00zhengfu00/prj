package com.physicmaster.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.physicmaster.R;

/**
 * Created by huashigen on 2016/12/20.
 */

public class DialogueTextView extends TextView {
    private Paint paint;

    public DialogueTextView(Context context) {
        this(context, null);
    }

    public DialogueTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogueTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getResources().getColor(R.color.colorBindGray));
        int width = getWidth();
    }
}
