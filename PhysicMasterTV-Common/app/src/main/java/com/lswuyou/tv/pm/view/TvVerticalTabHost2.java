package com.lswuyou.tv.pm.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;

import java.util.ArrayList;
import java.util.List;

import reco.frame.tv.util.ViewUtil;
import reco.frame.tv.view.component.TvSlowViewPager;
import reco.frame.tv.view.component.TvUtil;

/**
 * 通过对viewpager再加工而成,当前版本只适用多fragment布局
 *
 * @author keYence
 */
public class TvVerticalTabHost2 extends RelativeLayout {

    private final String TAG = "TvTabHost";
    /**
     * 光标
     */
    private View cursor;
    /**
     * 光标资源
     */
    private int cursorRes;
    /**
     * 可否缩放
     */
    private boolean scalable;
    /**
     * 放大比率
     */
    private float scale;
    /**
     * 放大用时
     */
    private int durationLarge = 100;
    /**
     * 缩小用时
     */
    private int durationSmall = 100;
    /**
     * 翻页滚动时间
     */
    private int durationScroll;
    /**
     * 触发延迟
     */
    private int delay = 10;
    /**
     * 光标宽高 包括阴影
     */
    private int cursorWidth, cursorHeight;
    /**
     * 光标相对位置
     */
    private int cursorMarginLeft, cursorMarginTop, cursorMarginRight,
            cursorMarginBottom;
    private int paddingLeft, paddingTop;
    public int curPage, initPage, savePage;
    /**
     * 页宽 页高
     */
    private int pageWidth, pageHeight;
    /**
     * 页数 当前页
     */
    private int pageCount, pageCurrent;
    /**
     * 顶部栏与页间隔高度
     */
    private int dividerHeight;
    /**
     * 标题宽高 间隔及文字大小
     */
    private int titleWidth, titleHeight, titleSpace, textSize;
    /**
     * 页面回收标记
     */
    private boolean isRestore = false;
    /**
     * 顶部栏获得焦点
     */
    private boolean isTopFocused = false;
    private ScrollPageChangerListener scrollPageChangerListener;
    private OnTopBarFocusChange onTopBarFocusChange;
    private FragmentManager fragmentManager;
    private List<Fragment> fragList = new ArrayList<Fragment>();
    private List<View> titleList;
    private FrameLayout container;
    private int notFocusableCount = 0;
    /**
     * 键为ID 值为对应页面
     */
    private SparseArray<Integer> idToPages;
    private int titleMarginLeft;
    private int textColorDefault, textColorSelected;
    private Drawable drawableLock, drawableUnLock;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
        }
    };

    public TvVerticalTabHost2(Context context) {
        this(context, null);
    }

    public TvVerticalTabHost2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TvVerticalTabHost2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray custom = getContext().obtainStyledAttributes(attrs,
                R.styleable.TvTabHost);
        this.cursorRes = custom.getResourceId(R.styleable.TvTabHost_cursorRes,
                0);
        this.titleWidth = (int) custom.getDimension(
                R.styleable.TvTabHost_titleWidth, 10);
        this.titleHeight = (int) custom.getDimension(
                R.styleable.TvTabHost_titleHeight, 10);
        this.titleSpace = (int) custom.getDimension(
                R.styleable.TvTabHost_titleSpace, 10);
        this.textColorDefault = custom.getColor(
                R.styleable.TvTabHost_textColorDefault, Color.BLACK);
        this.textColorSelected = custom.getColor(
                R.styleable.TvTabHost_textColorSelected, Color.WHITE);
        this.textSize = (int) custom.getDimension(
                R.styleable.TvTabHost_textSize, 10);

        this.dividerHeight = (int) custom.getDimension(
                R.styleable.TvTabHost_dividerHeight, 10);

        this.cursorWidth = (int) custom.getDimension(
                R.styleable.TvTabHost_cursorWidth, 0);

        this.cursorHeight = (int) custom.getDimension(
                R.styleable.TvTabHost_cursorHeight, 0);
        this.cursorMarginTop = (int) custom.getDimension(
                R.styleable.TvTabHost_cursorMarginTop, 0);
        this.cursorMarginRight = (int) custom.getDimension(
                R.styleable.TvTabHost_cursorMarginRight, 0);
        this.cursorMarginRight = (int) custom.getDimension(
                R.styleable.TvTabHost_cursorMarginRight, 0);
        this.cursorMarginBottom = (int) custom.getDimension(
                R.styleable.TvTabHost_cursorMarginBottom, 0);

        this.scalable = custom.getBoolean(R.styleable.TvTabHost_scalable, true);
        this.scale = custom.getFloat(R.styleable.TvTabHost_scale, 1.1f);
        this.delay = custom.getInteger(R.styleable.TvTabHost_delay, 110);
        this.durationLarge = custom.getInteger(
                R.styleable.TvTabHost_durationLarge, 100);
        this.durationSmall = custom.getInteger(
                R.styleable.TvTabHost_durationSmall, 100);
        this.durationScroll = custom.getInteger(
                R.styleable.TvTabHost_durationScroll, 370);

        custom.recycle();

        paddingLeft = (int) (titleWidth * (scale - 1) / 2 + 3 + this
                .getPaddingLeft());
        paddingTop = (int) (titleHeight * (scale - 1) / 2 + 3 + this
                .getPaddingTop());

        init();
    }

    private void init() {
        titleList = new ArrayList<View>();
        idToPages = new SparseArray<Integer>();
        drawableLock = getResources().getDrawable(R.mipmap.lock);
        drawableLock.setBounds(0, 0, drawableLock.getMinimumWidth(), drawableLock
                .getMinimumHeight());
        drawableUnLock = getResources().getDrawable(R.mipmap.unlock);
        drawableUnLock.setBounds(0, 0, drawableUnLock.getMinimumWidth(), drawableUnLock
                .getMinimumHeight());
    }

    /**
     * 添加页
     *
     * @param frag
     */
    public void addPage(FragmentManager fm, final Fragment frag, String title, final boolean
            focusable) {
        this.fragmentManager = fm;
        this.fragList.add(frag);
        final TextView tv = new TextView(getContext());
        tv.setFocusable(focusable);
        tv.setTextColor(textColorDefault);
        tv.setTextSize(ViewUtil.Px2Dp(getContext(), textSize));
        tv.setText(title);
        tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        if (!focusable) {
            tv.setCompoundDrawables(drawableLock, null, null, null);
            notFocusableCount++;
        } else {
            tv.setCompoundDrawables(drawableUnLock, null, null, null);
        }
        int tempId = TvUtil.buildId();
        final int index = titleList.size();
        tv.setId(tempId + titleList.size() + 1);
        idToPages.put(tempId + titleList.size() + 1, titleList.size());
        tv.setTag(titleList.size());

        tv.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(final View item, boolean focus) {
                if (focus) {
                    // 翻页
                    int targetPage = Integer.parseInt(item.getTag().toString());
                    // Log.e(TAG,
                    // "targetPage="+targetPage+"---"+pageCurrent+"---"+isTopFocused);
                    if (pageCurrent != targetPage && !isTopFocused) {
                        titleList.get(pageCurrent).requestFocus();
                        return;
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (item.isFocused()) {
                                moveCover(item);
                            }
                        }
                    }, delay);
                    if (isTopFocused && tv.isFocusable()) {
//                        pageContainer.setCurrentItem(targetPage, false);
                        showFragment(index);
                        flushTopBar(index);
                    }
                    isTopFocused = true;
                } else {
                    returnCover(item);
                }
            }
        });
        tv.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int id, KeyEvent key) {
                if (key.getAction() == KeyEvent.ACTION_DOWN) {
                    if (key.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        isTopFocused = false;
                    } else if (key.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                        if (pageCurrent == 0) {
                            isTopFocused = false;
                        }
                    } else if (key.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (pageCurrent == pageCount - 1 || pageCurrent == (pageCount -
                                notFocusableCount - 1)) {
                            isTopFocused = false;
                        }
                    }
                    if (onTopBarFocusChange != null) {
                        onTopBarFocusChange.onFocusChange(isTopFocused,
                                pageCurrent, key);
                    }
                }
                return false;
            }
        });
        titleList.add(tv);
    }

    private void buildTitle() {

    }

    private void buildPage() {

    }

    private void showFragment(int index) {
        for (Fragment fragment : fragList) {
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_in)
                    .hide(fragment).commit();
        }
        fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_in)
                .show(fragList.get(index)).commit();
        pageCurrent = index;
    }

    /**
     * 加载完毕后调用
     */
    public void buildLayout() {
        /**
         * 修改frag添加方式使用adapter 然后 addView();
         */
        LayoutParams params = (LayoutParams) getLayoutParams();

        LayoutParams newParams = new LayoutParams(
                params.width, params.height);
        newParams.setMargins(params.leftMargin, params.topMargin,
                params.rightMargin, params.bottomMargin);
        this.setLayoutParams(newParams);

        pageWidth = params.width - titleWidth;
        pageHeight = params.height - titleHeight - dividerHeight;
        if (pageWidth < 10) {
            pageWidth = LayoutParams.MATCH_PARENT;
        }
        if (pageHeight < 10) {
            pageHeight = LayoutParams.MATCH_PARENT;
        }
        pageContainer = new TvSlowViewPager(getContext(), durationScroll);
        container = (FrameLayout) View.inflate(getContext(), R.layout.fragment_container, null);
        LayoutParams vpParams = new LayoutParams(pageWidth, pageHeight);
//      vpParams.setMargins(titleWidth, (titleHeight + dividerHeight), 0, 0);
        vpParams.setMargins(titleWidth, 0, 0, 0);
        vpParams.addRule(RelativeLayout.ALIGN_LEFT, this.getId());
        vpParams.addRule(RelativeLayout.ALIGN_TOP, this.getId());

        RelativeLayout parent = (RelativeLayout) this.getParent();
//        parent.addView(pageContainer, vpParams);
        parent.addView(container, vpParams);
        for (Fragment fragment : fragList) {
            fragmentManager.beginTransaction().add(R.id.container, fragment)
                    .commit();
            fragmentManager.beginTransaction().hide(fragment).commit();
        }
        fragmentManager.beginTransaction().show(fragList.get(0)).commit();
        pageCount = fragList.size();
//        pageContainer.setOnPageChangeListener(new OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int postion) {
//                pageCurrent = postion;
//                if (scrollPageChangerListener != null) {
//                    scrollPageChangerListener.onPageSelected(postion);
//                }
//                flushTopBar(postion);
//            }
//
//            @Override
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int arg0) {
//            }
//        });

//        titleMarginLeft = (params.width - (titleList.size() * titleWidth + (titleList
//                .size() - 1) * titleSpace)) / 2;
        titleMarginLeft = 0;
        // 加载标题
        for (int i = 0; i < titleList.size(); i++) {
            TextView title = (TextView) titleList.get(i);
            LayoutParams rlp = new LayoutParams(titleWidth, titleHeight);
            // 默认居中
//            rlp.setMargins((titleSpace + titleWidth) * i + titleMarginLeft, 0, 0, 0);
            rlp.setMargins(0, 0, 0, 0);

            this.addView(title, rlp);
        }

        // 加载页
        if (isRestore) {
            this.curPage = savePage;
        }
        pageContainer.setPageTransformer(true, new ZoomOutPageTransformer());
        setFocusByIndex(0);
    }

    private void flushTopBar(int position) {
        if (this.findFocus() != null) {
            titleList.get(position).requestFocus();
        }
        for (int i = 0; i < titleList.size(); i++) {
            // 变色
            TextView title = (TextView) titleList.get(i);
            if (i == position) {
                title.setTextColor(textColorSelected);
                title.setBackgroundColor(getResources().getColor(R.color.selected_bg_color));
//                title.setBackground(getResources().getDrawable(R.drawable.shape_selected_bg));
            } else {
                title.setTextColor(textColorDefault);
                title.setBackgroundColor(getResources().getColor(R.color.item_bg_color1));
            }

        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        /**
         * 记录如果是wrap_content是设置的宽和高
         */
        int width = 0;
        int height = 0;

        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        /**
         * 根据childView计算的出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是warp_content时
         */

        for (int i = 0; i < cCount; i++) {

            View childView = getChildAt(i);

            if (childView instanceof ViewGroup) {
                width = sizeWidth;
                height = sizeHeight;
            } else {
                cWidth = childView.getMeasuredWidth();
                cHeight = childView.getMeasuredHeight();
                width += cWidth;
                height += cHeight;
            }

        }
        /**
         * 如果是wrap_content设置为我们计算的值 否则：直接设置为父容器计算的值
         */
        setMeasuredDimension(
                (widthMode == MeasureSpec.EXACTLY || width == 0) ? sizeWidth
                        : width,
                (heightMode == MeasureSpec.EXACTLY || height == 0) ? sizeHeight
                        : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (changed) {
            pageHeight = b;
            int cCount = getChildCount();
            int cWidth = 0;
            int cHeight = 0;
            int titleIndex = 0;
            /**
             * 遍历所有childView根据其宽和高，以及margin进行布局
             */
            for (int i = 0; i < cCount; i++) {
                View childView = getChildAt(i);

                if (childView instanceof ViewGroup) {
                    // 页布局
                    childView.layout(0, t + titleHeight, pageWidth, b
                            + titleHeight);
                } else {

                    cWidth = childView.getMeasuredWidth();
                    cHeight = childView.getMeasuredHeight();

                    int cl = 0, ct = 0, cr = 0, cb = 0;
//					cl = titleIndex * (titleWidth + titleSpace)
//							+ titleMarginLeft + paddingLeft;
                    cl = titleMarginLeft + paddingLeft;
//					ct = 0;
                    ct = titleIndex * (titleHeight);
                    cr = cl + cWidth;
                    cb = cHeight + ct;
                    childView.layout(cl, ct + paddingTop, cr, cb + paddingTop);
                    titleIndex++;
                }

            }
        }
    }

    /**
     * 滑至指定页面
     *
     * @param position
     */
    public void setCurrentPage(int position) {
        pageCurrent = position;
        pageContainer.setCurrentItem(position, false);
        flushTopBar(position);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        // 保存当前状态
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // 读取保存状态
        isRestore = true;
        super.onRestoreInstanceState(state);
    }

    /**
     * 光标移动 到达后 与控件同时放大
     */
    private void moveCover(View item) {

//        if (cursor == null) {
//            cursor = new ImageView(getContext());
//            cursor.setBackgroundResource(cursorRes);
//            this.addView(cursor);
//        }
//        setBorderParams(item);
        item.bringToFront();
//        cursor.bringToFront();
        if (scalable) {
            scaleToLarge(item);
        }

    }

    /**
     * 还原控件状态
     */

    public void returnCover(View item) {
        if (cursor != null) {
            cursor.setVisibility(View.INVISIBLE);
        }

        if (scalable) {
            scaleToNormal(item);
        }
    }

    private AnimatorSet animatorSet;
    private ObjectAnimator largeX;
    private TvSlowViewPager pageContainer;

    private void scaleToLarge(View item) {

        if (!item.isFocused()) {
            return;
        }

        animatorSet = new AnimatorSet();
        largeX = ObjectAnimator.ofFloat(item, "ScaleX", 1f, scale);
        ObjectAnimator largeY = ObjectAnimator.ofFloat(item, "ScaleY", 1f,
                scale);
//        ObjectAnimator cursorX = ObjectAnimator.ofFloat(cursor, "ScaleX", 1f,
//                scale);
//        ObjectAnimator cursorY = ObjectAnimator.ofFloat(cursor, "ScaleY", 1f,
//                scale);

        animatorSet.setDuration(durationLarge);
        animatorSet.play(largeX).with(largeY);

        animatorSet.start();

//        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f);
//        scaleAnimation.setDuration(durationLarge);
//        scaleAnimation.setFillAfter(true);
//        item.startAnimation(scaleAnimation);
    }

    public void scaleToNormal(View item) {
        if (animatorSet == null) {
            return;
        }
        if (animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        ObjectAnimator oa = ObjectAnimator.ofFloat(item, "ScaleX", 1f);
        oa.setDuration(durationSmall);
        oa.start();
        ObjectAnimator oa2 = ObjectAnimator.ofFloat(item, "ScaleY", 1f);
        oa2.setDuration(durationSmall);
        oa2.start();
    }

    /**
     * 指定光标相对位置
     */
    private void setBorderParams(View item) {
        cursor.clearAnimation();
        cursor.setVisibility(View.VISIBLE);
        // 判断类型
        LayoutParams params = (LayoutParams) item
                .getLayoutParams();

        int l, t, r, b;

        l = params.leftMargin + paddingLeft + cursorMarginLeft;
        t = params.topMargin + paddingTop + cursorMarginTop;
        r = l + cursorWidth;
        b = t + cursorHeight;
        cursor.layout(l, t, r, b);

        // Log.e(VIEW_LOG_TAG, l + "---" + t+ "---" + r + "---" + b);

    }

    public void setOnPageChangeListener(ScrollPageChangerListener listener) {
        this.scrollPageChangerListener = listener;
    }

    public void setOnTopBarFocusChangeListener(OnTopBarFocusChange listener) {
        this.onTopBarFocusChange = listener;
    }

    public interface ScrollPageChangerListener {
        public void onPageSelected(int pageCurrent);
    }

    public interface OnTopBarFocusChange {
        public void onFocusChange(boolean hasFocus, int postion, KeyEvent key);
    }

    public void refreshTitleState() {
        for (int i = 0; i < titleList.size(); i++) {
            titleList.get(i).setFocusable(true);
            ((TextView) (titleList.get(i))).setCompoundDrawables(drawableUnLock, null, null, null);
        }
        notFocusableCount = 0;
    }

    /**
     * 设定指定的titlefocus
     *
     * @param index
     */
    public void setFocusByIndex(int index) {
        View view = findViewById(idToPages.keyAt(index));
        if (view != null) {
            view.requestFocus();
        }
//        titleList.get(index).requestFocus();
    }
}
