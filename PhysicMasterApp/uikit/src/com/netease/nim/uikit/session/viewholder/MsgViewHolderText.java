package com.netease.nim.uikit.session.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.session.emoji.MoonUtil;
import com.netease.nim.uikit.session.module.Container;

/**
 * Created by zhoujianghua on 2015/8/4.
 */
public class MsgViewHolderText extends MsgViewHolderBase {

    public MsgViewHolderText(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_text;
    }

    @Override
    protected void inflateContentView() {
    }

    @Override
    protected void bindContentView() {
        layoutDirection();

        TextView bodyTextView = findViewById(R.id.nim_message_item_text_body);
        bodyTextView.setTextColor(isReceivedMessage() ? Color.BLACK : Color.WHITE);
        bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick();
            }
        });
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, getDisplayText(),
                ImageSpan.ALIGN_BOTTOM);
        CharSequence text = bodyTextView.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) text;
            URLSpan urls[] = sp.getSpans(0, end, URLSpan.class);
            if (0 == urls.length) {
                return;
            }
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (URLSpan urlSpan : urls) {
                MyURLSpan myURLSpan = new MyURLSpan(urlSpan.getURL());
                style.setSpan(myURLSpan, sp.getSpanStart(urlSpan),
                        sp.getSpanEnd(urlSpan),
                        Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            }
            bodyTextView.setText(style);
        }
    }

    private class MyURLSpan extends ClickableSpan {

        private String url;

        public MyURLSpan(String url) {
            this.url = url;
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            String activityName = "com.physicmaster.modules.WebviewActivity";
            Class clazz = null;
            try {
                clazz = Class.forName(activityName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(context, clazz);
            intent.putExtra("url", url);
            context.startActivity(intent);
        }

    }

    private void layoutDirection() {
        TextView bodyTextView = findViewById(R.id.nim_message_item_text_body);
        if (isReceivedMessage()) {
            bodyTextView.setBackgroundResource(R.drawable.nim_message_item_left_selector);
            bodyTextView.setPadding(ScreenUtil.dip2px(15), ScreenUtil.dip2px(8), ScreenUtil
                    .dip2px(10), ScreenUtil.dip2px(8));
        } else {
            bodyTextView.setBackgroundResource(R.drawable.nim_message_item_right_selector);
            bodyTextView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil
                    .dip2px(15), ScreenUtil.dip2px(8));
        }
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    protected String getDisplayText() {
        return message.getContent();
    }
}
