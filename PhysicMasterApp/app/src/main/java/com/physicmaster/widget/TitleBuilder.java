package com.physicmaster.widget;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.physicmaster.R;


/**
 * 类 名 称 :
 * 软件版权 : 无届网络科技有限公司
 * 系统版本 : 1.0
 * 开发人员 : songrui
 * 开发时间 : 2016/10/12 14:07
 * 功能说明 :
 * 审核人员 :
 * 相关文档 :
 * 修改记录 : 需求编号    修改日期    修改人员    修改说明
 */
public class TitleBuilder {

    /**
     * title栏根布局
     */
    private View titleView;
    private TextView left_textview;
    private ImageView left_imageview;
    private TextView middle_textview;
    private TextView right_textview;
    private ImageView right_imageview;


    public TitleBuilder(Activity context) {

        titleView = context.findViewById(R.id.title_bar);

        left_textview = (TextView) titleView.findViewById(R.id.title_left_textview);
        left_imageview = (ImageView) titleView.findViewById(R.id.title_left_imageview);

        middle_textview = (TextView) titleView.findViewById(R.id.title_middle_textview);

        right_textview = (TextView) titleView.findViewById(R.id.title_right_textview);
        right_imageview = (ImageView) titleView.findViewById(R.id.title_right_imageview);

    }

    public TitleBuilder(View context) {

        titleView = context.findViewById(R.id.title_bar);

        left_textview = (TextView) titleView.findViewById(R.id.title_left_textview);
        left_imageview = (ImageView) titleView.findViewById(R.id.title_left_imageview);

        middle_textview = (TextView) titleView.findViewById(R.id.title_middle_textview);

        right_textview = (TextView) titleView.findViewById(R.id.title_right_textview);
        right_imageview = (ImageView) titleView.findViewById(R.id.title_right_imageview);

    }

    /**
     * title 的背景色
     */

    public TitleBuilder setMiddleTitleBgRes(int resid) {

        middle_textview.setBackgroundResource(resid);

        return this;
    }

    /**
     * title的文本
     *
     * @param text
     * @return
     */
    public TitleBuilder setMiddleTitleText(String text) {

        middle_textview.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        middle_textview.setText(text);

        return this;
    }

    /**
     * left
     */
    /**
     * 图片按钮
     *
     * @param resId
     * @return
     */
    public TitleBuilder setLeftImageRes(int resId) {

        left_imageview.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        left_imageview.setImageResource(resId);

        return this;
    }

    /**
     * 左边文字按钮
     *
     * @param text
     * @return
     */
    public TitleBuilder setLeftText(String text) {

        left_textview.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        left_textview.setText(text);

        return this;
    }

    /**
     * 设置左边的事件
     */
    public TitleBuilder setLeftTextOrImageListener(View.OnClickListener listener) {

        if (left_imageview.getVisibility() == View.VISIBLE) {

            left_imageview.setOnClickListener(listener);

        }
        if (left_textview.getVisibility() == View.VISIBLE) {

            left_textview.setOnClickListener(listener);

        }

        return this;
    }

    /**
     * right
     */
    /**
     * 右边图片按钮
     *
     * @param resId
     * @return
     */
    public TitleBuilder setRightImageRes(int resId) {

        right_imageview.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        right_imageview.setImageResource(resId);

        return this;
    }

    /**
     * 右边文字按钮
     *
     * @param text
     * @return
     */
    public TitleBuilder setRightText(String text) {

        right_textview.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        right_textview.setText(text);

        return this;
    }

    /**
     * 设置右边的事件
     */
    public TitleBuilder setRightTextOrImageListener(View.OnClickListener listener) {

        if (right_imageview.getVisibility() == View.VISIBLE) {

            right_imageview.setOnClickListener(listener);

        } else if (right_textview.getVisibility() == View.VISIBLE) {

            right_textview.setOnClickListener(listener);

        }

        return this;
    }

    public View build() {

        return titleView;
    }

}
