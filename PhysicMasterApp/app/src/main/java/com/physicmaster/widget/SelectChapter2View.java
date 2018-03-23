package com.physicmaster.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.physicmaster.R;
import com.physicmaster.modules.study.fragment.chapter.ChapterListFragment;
import com.physicmaster.net.response.account.StartupResponse;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huashigen on 2017-10-17.
 */

public class SelectChapter2View {
    private Context context;
    public ViewGroup decorView;//显示本view的根view
    public ViewGroup rootView;//附加view的根view
    private boolean isShowing;
    protected View clickView;//是通过哪个View弹出的
    private boolean isAnim = true;
    private ViewPager                                   vpInformation;
    private PagerTab                                    ptInformation;
    private boolean                                     dismissing;
    private OnDismissListener                           onDismissListener;
    private Animation                                   outAnim;
    private Animation                                   inAnim;
    private List<StartupResponse.DataBean.BookMenuBean> bookMenuList;
    private List<String>                                tabNames;
    private String                                      selectSubject;
    private String                                      versionAndGrade;
    private int                                         bookId;
    private List<Integer>                               selectedBookIds;
    private ChapterListFragment.OnItemSelectListener    onItemSelectListener;
    private RelativeLayout                              rlParent;
    private int                                         selectIndex;
    private MagicIndicator                              magicIndicator;


    public SelectChapter2View(Context context, ViewGroup decorView, int marginTop, int marginBottom, Bundle data) {
        this.context = context;
        this.decorView = decorView;
        initViews(decorView, marginTop, marginBottom, data);
    }

    private void initViews(ViewGroup decorView, int marginTop, int marginBottom, Bundle data) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.dialog_fragment_select_chapter2, decorView, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, marginTop, 0, marginBottom);
        rootView.setLayoutParams(layoutParams);
       // ptInformation = (PagerTab) rootView.findViewById(R.id.pt_information);

        magicIndicator = rootView.findViewById(R.id.magic_indicator);
        vpInformation =  rootView.findViewById(R.id.vp_information);

        selectSubject = data.getString("selectSubject");
        versionAndGrade = data.getString("versionGrade");
        bookId = data.getInt("bookId");
        bookMenuList = data.getParcelableArrayList("bookMenu");
        selectIndex = 0;
        tabNames = new ArrayList<>();
        selectedBookIds = new ArrayList<>();
        for (int i = 0; i < bookMenuList.size(); i++) {
            tabNames.add(bookMenuList.get(i).n);
            if (bookMenuList.get(i).n.equals(selectSubject)) {
                selectIndex = i;
                selectedBookIds.add(bookId);
            } else {
                selectedBookIds.add(bookMenuList.get(i).e.get(0).b.get(0).i);
            }
        }
        vpInformation.setAdapter(new InformationAdapter(((FragmentActivity) context).getSupportFragmentManager()));

        final CommonNavigator commonNavigator = new CommonNavigator(context);
        if (bookMenuList.size() <= 3) {
            commonNavigator.setAdjustMode(true);
        } else {
            commonNavigator.setEnablePivotScroll(true);
        }
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return bookMenuList == null ? 0 : bookMenuList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);

                simplePagerTitleView.setText(bookMenuList.get(index).n);
                simplePagerTitleView.setTextSize(0, context.getResources().getDimensionPixelSize(R.dimen.size_16));
                simplePagerTitleView.setNormalColor(Color.parseColor("#666666"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#07A7FA"));

                simplePagerTitleView.setOnClickListener(v -> {
                            vpInformation.setCurrentItem(index);
                        }
                );

                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#07A7FA"));
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setLineHeight(6);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);

        vpInformation.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                magicIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                magicIndicator.onPageScrollStateChanged(state);
            }
        });
       // vpInformation.setCurrentItem(0);

        //ptInformation.setViewPager(vpInformation);
        vpInformation.setCurrentItem(selectIndex);
       // ptInformation.selectTab(selectIndex);
        init();
    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }

    public void setOnItemSelectListener(ChapterListFragment.OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }


    class InformationAdapter extends FragmentStatePagerAdapter {
        SparseArray<ChapterListFragment> sFragments = new SparseArray<>();

        public InformationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ChapterListFragment fragment = sFragments.get(position);
            if (fragment == null) {
                fragment = new ChapterListFragment();
                fragment.setOnItemSelectListener(new ChapterListFragment.OnItemSelectListener() {
                    @Override
                    public void onSubmit(String chapterId, String name, int selectBookId, String versionGrade, String selectSubject) {
                        if (onItemSelectListener != null) {
                            onItemSelectListener.onSubmit(chapterId, name, selectBookId, versionGrade, selectSubject);
                        }
                        dismiss();
                    }
                });
                Bundle data = new Bundle();
                int bookId = getSelectBookId(position);
                StartupResponse.DataBean.BookMenuBean bookMenuBean = bookMenuList.get(position);
                data.putInt("bookId", bookId);
                data.putParcelable("bookMenuBean", bookMenuBean);
                if (position == selectIndex) {
                    data.putString("versionGrade", versionAndGrade);
                }
                data.putString("subjectName", bookMenuBean.n);
                fragment.setArguments(data);
                sFragments.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return tabNames.size();
        }

        public CharSequence getPageTitle(int position) {
            return tabNames.get(position);
        }
    }

    class ChapterAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return tabNames.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }

    /**
     * 获取已选择的bookId
     *
     * @param position
     * @return
     */
    private int getSelectBookId(int position) {
        return selectedBookIds.get(position);
    }

    /**
     * @param v      (是通过哪个View弹出的)
     * @param isAnim 是否显示动画效果
     */
    public void show(View v, boolean isAnim) {
        this.clickView = v;
        this.isAnim = isAnim;
        show();
    }

    public void show(boolean isAnim) {
        this.isAnim = isAnim;
        show();
    }

    public void show(View v) {
        this.clickView = v;
        show();
    }

    /**
     * 添加View到根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        isShowing = true;
        onAttached(rootView);
        rootView.requestFocus();
    }

    public Animation getInAnimation() {
        return AnimationUtils.loadAnimation(context, R.anim.slide_in_from_top);
    }

    public Animation getOutAnimation() {
        return AnimationUtils.loadAnimation(context, R.anim.slide_out_to_top);
    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        return rootView.getParent() != null || isShowing;
    }

    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        decorView.addView(view);
        if (isAnim) {
            rootView.startAnimation(inAnim);
        }
    }

    public void dismiss() {
        if (dismissing) {
            return;
        }

        if (isAnim) {
            //消失动画
            outAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    dismissImmediately();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            rootView.startAnimation(outAnim);
        } else {
            dismissImmediately();
        }
        dismissing = true;
    }

    public void dismissImmediately() {

        decorView.post(new Runnable() {
            @Override
            public void run() {
                //从根视图移除
                decorView.removeView(rootView);
                isShowing = false;
                dismissing = false;
                if (onDismissListener != null) {
                    onDismissListener.onDismiss();
                }
            }
        });
    }

    public interface OnDismissListener {
        public void onDismiss();
    }
}
