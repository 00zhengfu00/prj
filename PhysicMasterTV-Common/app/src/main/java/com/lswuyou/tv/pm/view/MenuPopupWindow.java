/* 
 * 系统名称：lswuyou
 * 类  名  称：MenuPopupWindow.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2016-4-21 下午3:28:20
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.lswuyou.tv.pm.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.Constant;

import static com.lswuyou.tv.pm.R.id.focus_bg;
import static com.lswuyou.tv.pm.R.id.sub_focus_bg;

public class MenuPopupWindow extends PopupWindow {
    private Context mContext;
    private OnMenuActionListener listener;
    private LinearLayout llMenu, llSublist;
    private ScrollView scrollView;
    private TextView tvXuanji, tvQuality, tvSublist;
    private View focusBgMenu, focusBgSub;
    private int singleItemHeight;
    private boolean first = true;
    private boolean isMenuFocused = true;
    private View savedFocusedView;
    private String defaultQuality;
    private int defaultJi;
    public int jishu;
    private SparseArray<View> viewIds;

    public MenuPopupWindow(Context mContext, OnMenuActionListener listener, String
            defaultQuality, int jishu, int defalutJi) {
        this.mContext = mContext;
        this.listener = listener;
        this.defaultQuality = defaultQuality;
        this.defaultJi = defalutJi;
        this.jishu = jishu;
        initPopupWindow();
    }

    private void initPopupWindow() {
        View view = View.inflate(mContext, R.layout.popupwindow_menu, null);
        setContentView(view);
        singleItemHeight = (int) mContext.getResources().getDimension(R.dimen.y100);
        viewIds = new SparseArray<>();
        llSublist = (LinearLayout) view.findViewById(R.id.ll_sublist);
        llMenu = (LinearLayout) view.findViewById(R.id.ll_menu);
        focusBgMenu = view.findViewById(focus_bg);
        focusBgSub = view.findViewById(sub_focus_bg);
        focusBgSub.setVisibility(View.INVISIBLE);
        tvXuanji = (TextView) view.findViewById(R.id.tv_xuanji);
        tvQuality = (TextView) view.findViewById(R.id.tv_quality);
        initMenuAction();
        tvSublist = (TextView) view.findViewById(R.id.tv_sublist);
        initXuanjiSubMenuAction();
        setWidth((int) mContext.getResources().getDimension(R.dimen.x444));
        setHeight((int) mContext.getResources().getDimension(R.dimen.y1080));
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setOutsideTouchable(true);

        setAnimationStyle(R.anim.trans_right_in);
        setTouchable(true);
        setFocusable(true);
        update();
        int fixDistance = (int) mContext.getResources().getDimension(R.dimen.y50);
        llMenu.scrollBy(0, -fixDistance);
    }

    public void showPopupWindow(View view) {
        if (!isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            showAtLocation(view, Gravity.NO_GRAVITY, location[0] - getWidth() + view.getWidth(),
                    location[1] + dp2px(mContext, 45));
        }
        tvXuanji.requestFocus();
    }

    public void showPopupWindow(View view, int xLocation) {
        if (!isShowing()) {
            showAtLocation(view, Gravity.NO_GRAVITY, xLocation - getWidth(),
                    0);
        }
        tvXuanji.requestFocus();
    }

    public interface OnSubmitListener {
        public void onSubmit(int action);
    }

    /**
     * dp转px
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context
                .getResources().getDisplayMetrics());
    }

    public interface OnMenuActionListener {
        public void onXuanjiSelected(int jishu);

        public void onQualitySelected(int qulity);
    }

    /**
     * 滑动指定距离
     *
     * @param distance
     */
    private void scrollByDistance(View view, int distance) {

    }

    /**
     * 菜单的事件初始化
     */
    private void initMenuAction() {
        tvXuanji.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvSublist.setText("选集");
                    savedFocusedView = v;
                    initXuanjiSubMenuAction();
                }
            }
        });

        tvXuanji.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    menuFocusActionXuanji(false);
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    llMenu.scrollBy(0, singleItemHeight);
                    tvQuality.setTextColor(mContext.getResources().getColor(R.color.white));
                    tvXuanji.setTextColor(mContext.getResources().getColor(R.color
                            .text_unselect_color));
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    return true;
                }
                return false;
            }
        });

        tvQuality.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tvSublist.setText("清晰度");
                    savedFocusedView = v;
                    initQualitySubMenuAction();
                }
            }
        });

        tvQuality.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    menuFocusActionQuality(false);
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    llMenu.scrollBy(0, -singleItemHeight);
                    tvXuanji.setTextColor(mContext.getResources().getColor(R.color.white));
                    tvQuality.setTextColor(mContext.getResources().getColor(R.color
                            .text_unselect_color));
                } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 清晰度子菜单的事件初始化
     */
    private void initQualitySubMenuAction() {
        llSublist.removeAllViews();
        llSublist.scrollTo(0, 0);
        int index = getDefaultQualityIndex();
        for (int i = 0; i < Constant.videoQualities.length; i++) {
            final TextView tvName = (TextView) View.inflate(mContext, R.layout.item_menu, null);
            tvName.setHeight((int) mContext.getResources().getDimension(R.dimen.y100));
            tvName.setText(Constant.videoQualities[i]);
            if (i == index) {
                tvName.setTextColor(mContext.getResources().getColor(R.color.selected_bg_color));
            }
            final int finalI = i;
            tvName.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            menuFocusActionQuality(true);
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            if (finalI == 3) {
                                return true;
                            }
                            llSublist.scrollBy(0, singleItemHeight);
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            if (finalI == 0) {
                                return true;
                            }
                            llSublist.scrollBy(0, -singleItemHeight);
                        }
                    }
                    return false;
                }
            });
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onQualitySelected(finalI);
                    ((TextView)viewIds.get(getDefaultQualityIndex())).setTextColor(mContext.getResources().getColorStateList(R.color.text_color_bg));
                    defaultQuality = Constant.videoQualitiesTag[finalI];
                }
            });
            tvName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (finalI == getDefaultQualityIndex()) {
                        if (hasFocus) {
                            tvName.setTextColor(mContext.getResources().getColor(R.color.white));
                        } else {
                            tvName.setTextColor(mContext.getResources().getColor(R.color.selected_bg_color));
                        }
                    }
                }
            });
            viewIds.put(i, tvName);
            llSublist.addView(tvName);
        }
        for (int i = 0; i < 10; i++) {
            TextView tvName = new TextView(mContext);
            tvName.setHeight((int) mContext.getResources().getDimension(R.dimen.y100));
            tvName.setFocusable(false);
            llSublist.addView(tvName);
        }
        int scrollDistance = computeScrollDistance(Constant.videoQualities.length, index);
        llSublist.scrollBy(0, scrollDistance);
    }

    /**
     * 选集子菜单的事件初始化
     */
    private void initXuanjiSubMenuAction() {
        viewIds.clear();
        llSublist.removeAllViews();
        llSublist.scrollTo(0, 0);
        for (int i = 0; i < jishu; i++) {
            final TextView tvName = (TextView) View.inflate(mContext, R.layout.item_menu, null);
            tvName.setHeight((int) mContext.getResources().getDimension(R.dimen.y100));
            tvName.setText((i + 1) + "");
            if (i == defaultJi) {
                tvName.setTextColor(mContext.getResources().getColor(R.color.selected_bg_color));
            }
            final int finalI = i;
            tvName.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            menuFocusActionXuanji(true);
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                            if (finalI == jishu - 1) {
                                return true;
                            }
                            llSublist.scrollBy(0, singleItemHeight);
                        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                            if (finalI == 0) {
                                return true;
                            }
                            llSublist.scrollBy(0, -singleItemHeight);
                        }
                    }
                    return false;
                }
            });
            tvName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (finalI == defaultJi) {
                        if (hasFocus) {
                            tvName.setTextColor(mContext.getResources().getColor(R.color.white));
                        } else {
                            tvName.setTextColor(mContext.getResources().getColor(R.color.selected_bg_color));
                        }
                    }
                }
            });
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onXuanjiSelected(finalI);
                    ((TextView)viewIds.get(defaultJi)).setTextColor(mContext.getResources().getColorStateList(R.color.text_color_bg));
                    defaultJi = finalI;
                }
            });
            viewIds.put(i, tvName);
            llSublist.addView(tvName);
        }
        for (int i = 0; i < 10; i++) {
            TextView tvName = new TextView(mContext);
            tvName.setHeight((int) mContext.getResources().getDimension(R.dimen.y100));
            tvName.setFocusable(false);
            llSublist.addView(tvName);
        }
        int scrollDistance = computeScrollDistance(jishu, defaultJi);
        llSublist.scrollBy(0, scrollDistance);
    }

    /**
     * "FHD", "HD", "SD", "LD"
     * 根据清晰度返回位置
     */
    private int getDefaultQualityIndex() {
        if (defaultQuality.equals("LD")) {
            return 0;
        } else if (defaultQuality.equals("SD")) {
            return 1;
        } else if (defaultQuality.equals("HD")) {
            return 2;
        } else if (defaultQuality.equals("FHD")) {
            return 3;
        } else {
            return 0;
        }
    }

    /**
     * 主菜单焦点切换处理
     */
    private void menuFocusActionXuanji(boolean bool) {
        isMenuFocused = bool;
        if (bool) {
            focusBgMenu.setVisibility(View.VISIBLE);
            focusBgSub.setVisibility(View.INVISIBLE);
            savedFocusedView.requestFocus();
            ((TextView) savedFocusedView).setTextColor(mContext.getResources().getColor(R.color
                    .text_color_bg));
            ((TextView) viewIds.get(defaultJi)).setTextColor(mContext.getResources().getColor(R
                    .color
                    .selected_bg_color));
        } else {
            focusBgMenu.setVisibility(View.INVISIBLE);
            focusBgSub.setVisibility(View.VISIBLE);
            ((TextView) savedFocusedView).setTextColor(mContext.getResources().getColor(R.color
                    .selected_bg_color));
            ((TextView) viewIds.get(defaultJi)).setTextColor(mContext.getResources().getColor(R
                    .color
                    .text_color_bg));
        }
    }

    /**
     * 主菜单焦点切换处理
     */
    private void menuFocusActionQuality(boolean bool) {
        int index = getDefaultQualityIndex();
        isMenuFocused = bool;
        if (bool) {
            focusBgMenu.setVisibility(View.VISIBLE);
            focusBgSub.setVisibility(View.INVISIBLE);
            savedFocusedView.requestFocus();
            ((TextView) savedFocusedView).setTextColor(mContext.getResources().getColor(R.color
                    .text_color_bg));
            ((TextView) viewIds.get(index)).setTextColor(mContext.getResources().getColor(R
                    .color
                    .selected_bg_color));
        } else {
            focusBgMenu.setVisibility(View.INVISIBLE);
            focusBgSub.setVisibility(View.VISIBLE);
            ((TextView) savedFocusedView).setTextColor(mContext.getResources().getColor(R.color
                    .selected_bg_color));
            ((TextView) viewIds.get(index)).setTextColor(mContext.getResources().getColor(R
                    .color
                    .text_color_bg));
        }
    }

    /**
     * 计算滚动高度
     *
     * @param totalNum
     * @param selectIndex
     * @return
     */
    private int computeScrollDistance(int totalNum, int selectIndex) {
        int selectHeight = (int) ((selectIndex + 0.5) * singleItemHeight);
        int screenCenter = (BaseApplication.getScreenHeight() - singleItemHeight) / 2;
        return selectHeight - screenCenter;
    }
}