package com.physicmaster.modules.mine.activity.notebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseFragment2;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.guide.ExposureView;
import com.physicmaster.modules.guide.GuideNoteBookDialogFragment2;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.notebook.GetPoolCountResponse;
import com.physicmaster.net.service.notebook.GetPoolCountService;
import com.physicmaster.widget.GifView;
import com.physicmaster.widget.TitleBuilder;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

public class NoteBookActivity extends BaseActivity {

    private ViewPager                vpInformation;
    private MagicIndicator           magicIndicator;
    private String[]                 mTabNames;
    private StartupResponse.DataBean dataBean;

    @Override
    protected void findViewById() {
        magicIndicator = findViewById(R.id.magic_indicator);
        vpInformation = findViewById(R.id.vp_information);

        dataBean = (StartupResponse.DataBean) CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys.STARTUP_INFO, StartupResponse.DataBean.class);

        initTitle();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(view -> finish())
                .setRightImageRes(R.mipmap.add_note)
                .setRightTextOrImageListener(v -> {
                    Intent intent = new Intent(NoteBookActivity.this, RecordQuActivity.class);
                    intent.putExtra("subjectId", dataBean.bookMenu.get(vpInformation.getCurrentItem()).i+"");
                    startActivityForResult(intent, 2);
                })
                .setMiddleTitleText("我的错题");
    }

    @Override
    protected void initView() {
        //        mTabNames = ScreenUtils.getStringArray(R.array.grade_tab_names);

        vpInformation.setAdapter(new SubjectAdapter(getSupportFragmentManager(), dataBean.bookMenu));
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        if (dataBean.bookMenu.size() <= 3) {
            commonNavigator.setAdjustMode(true);
        } else {
            commonNavigator.setEnablePivotScroll(true);
        }
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return dataBean.bookMenu == null ? 0 : dataBean.bookMenu.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);

                simplePagerTitleView.setText(dataBean.bookMenu.get(index).n);
                simplePagerTitleView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.size_16));
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
        vpInformation.setCurrentItem(0);

        getPoolCount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getPoolCount();
    }

    /**
     * 获取错题池错题总数量
     */
    private void getPoolCount() {
        GetPoolCountService service = new GetPoolCountService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetPoolCountResponse>() {
            @Override
            public void onGetData(GetPoolCountResponse data) {
                refreshUI(data.data.s_1 + data.data.s_2 + data.data.s_4);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.postLogined("", false);
    }

    private void refreshUI(int totalCount) {
        TextView tvMsg = findViewById(R.id.tv_msg);
        TextView tvQuNum = findViewById(R.id.tv_qu_pool_num);
        LinearLayout llMsg = findViewById(R.id.ll_msg);
        GifView gfMascot = findViewById(R.id.gf_mascot);
        RelativeLayout llPool = findViewById(R.id.rl_pool);
        ViewPager viewPager = findViewById(R.id.vp_information);
        RelativeLayout.LayoutParams vpLayoutParams = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
        LinearLayout llBottom = findViewById(R.id.view_bottom);
        LinearLayout.LayoutParams llPoolLayoutParams = (LinearLayout.LayoutParams) llPool.getLayoutParams();
        int bottomHeight;
        int mascotImg;
        String msg;
        if (totalCount <= 10) {
            bottomHeight = getResources().getDimensionPixelSize(R.dimen.dimen_45);
            mascotImg = R.mipmap.mascot_2;
            msg = "消灭错题，水位会变低，快来试试吧~";
        } else if (totalCount > 10 && totalCount <= 30) {
            bottomHeight = getResources().getDimensionPixelSize(R.dimen.dimen_55);
            mascotImg = R.mipmap.mascot_3;
            msg = "水越来越深了，是时候清理一波错题了~";
        } else if (totalCount > 30 && totalCount <= 80) {
            bottomHeight = getResources().getDimensionPixelSize(R.dimen.dimen_65);
            mascotImg = R.mipmap.mascot_4;
            msg = "跪求主人消灭错题，我我我不会游泳啊~";
        } else {
            bottomHeight = getResources().getDimensionPixelSize(R.dimen.dimen_75);
            mascotImg = R.mipmap.mascot_5;
            msg = "主人再不消灭错题，潜水艇都救不了我~";
        }
        llPoolLayoutParams.height = bottomHeight;
        llPoolLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        llPool.setLayoutParams(llPoolLayoutParams);

        int vpMargin = llPoolLayoutParams.height;
        viewPager.setPadding(0, 0, 0, vpMargin);
        viewPager.setLayoutParams(vpLayoutParams);
        //设置精灵图片
        gfMascot.setGifResource(mascotImg);
        tvQuNum.setText("错题池（" + totalCount + "）");
        View.OnClickListener onClickListener = v -> {
            Intent intent = new Intent(this, NoteBookWebActivity.class);
            intent.putExtra("pageType", 0);
            intent.putExtra("subjectId", dataBean.bookMenu.get(vpInformation.getCurrentItem()).i);
            startActivityForResult(intent, 1);
        };
        tvQuNum.setOnClickListener(onClickListener);
        llPool.setOnClickListener(onClickListener);
        tvMsg.setText(msg);
        GifView gfMascotDefault = findViewById(R.id.gf_mascot_default);
        gfMascot.setOnClickListener(v -> {
            gfMascot.setVisibility(View.GONE);
            //设置俏皮话
            llMsg.setVisibility(View.GONE);
            gfMascotDefault.setVisibility(View.VISIBLE);
        });
        gfMascotDefault.setOnClickListener(v -> {
            gfMascot.setVisibility(View.VISIBLE);
            //设置俏皮话
            llMsg.setVisibility(View.VISIBLE);
            gfMascotDefault.setVisibility(View.GONE);
        });
        WaveBgDrawable tvQuNumBg = new WaveBgDrawable(this);
        llPool.setBackground(tvQuNumBg);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_note_book;
    }

    class SubjectAdapter extends FragmentPagerAdapter {

        private SparseArray<BaseFragment2>                  sFragments = new SparseArray<>();
        private List<StartupResponse.DataBean.BookMenuBean> list       = new ArrayList<>();

        public SubjectAdapter(FragmentManager fm, List<StartupResponse.DataBean.BookMenuBean> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment2 fragment = sFragments.get(position);
            Bundle bundle = new Bundle();
            if (fragment == null) {
                fragment = new NoteBookFragment();
                bundle.putString("subjectId", list.get(position).i + "");
                //                switch (position) {
                //                    case 0:
                //                        fragment = new NoteBookFragment();
                //                        bundle.putString("subjectId", Constant.SUBJECTID_PM + "");
                //                        break;
                //                    case 1:
                //                        fragment = new NoteBookFragment();
                //                        bundle.putString("subjectId", Constant.SUBJECTID_CM + "");
                //                        break;
                //                    case 2:
                //                        fragment = new NoteBookFragment();
                //                        bundle.putString("subjectId", Constant.SUBJECTID_MM + "");
                //                        break;
                //                    case 3:
                //                        fragment = new NoteBookFragment();
                //                        bundle.putString("subjectId", Constant.SUBJECTID_HPM + "");
                //                        break;
                //                    case 4:
                //                        fragment = new NoteBookFragment();
                //                        bundle.putString("subjectId", Constant.SUBJECTID_NS + "");
                //                        break;
                //                }
                fragment.setArguments(bundle);
                sFragments.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return dataBean.bookMenu.size();
        }

        public CharSequence getPageTitle(int position) {
            return dataBean.bookMenu.get(position).n;
        }
    }

    /**
     * 错题本展示引导
     */
    public void showGuide() {
        int[] location = new int[2];
        View ivAdd = findViewById(R.id.title_right_imageview);
        ivAdd.getLocationOnScreen(location);
        int padding = getResources().getDimensionPixelOffset(R.dimen.dimen_10);
        ExposureView exposureView1 = new ExposureView(location[0] + padding, location[1] + padding, location[0] + ivAdd.getWidth() - padding, location[1] +
                ivAdd.getHeight() -
                padding);
        View tvQuNum = findViewById(R.id.tv_qu_pool_num);
        int[] location2 = new int[2];
        tvQuNum.getLocationOnScreen(location2);
        ExposureView exposureView2 = new ExposureView(location2[0], location2[1], location2[0] + tvQuNum.getWidth(), location2[1] + tvQuNum.getHeight());
        Bundle data = new Bundle();
        data.putParcelable("view1", exposureView1);
        data.putParcelable("view2", exposureView2);
        GuideNoteBookDialogFragment2 fragment2 = new GuideNoteBookDialogFragment2(this, data);
        fragment2.setPaintViewOnClickListener(v -> {
                    startActivity(new Intent(NoteBookActivity.this, RecordQuActivity.class));
                    fragment2.dismiss();
                }
        );
    }
}
