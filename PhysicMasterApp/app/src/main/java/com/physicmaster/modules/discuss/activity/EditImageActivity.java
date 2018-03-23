/* 
 * 系统名称：lswuyou
 * 类  名  称：EditImageActivity.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-11-23 下午1:55:53
 * 功能说明： 用于编辑选择的图片-主要是删除功能
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.modules.discuss.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.utils.BitmapUtils;
import com.physicmaster.widget.FixedViewPager;
import com.physicmaster.widget.TitleBuilder;
import com.physicmaster.widget.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;


public class EditImageActivity extends BaseActivity {
    private FixedViewPager viewPager;
    private ArrayList<String> mImagePathList = new ArrayList<>();
    private List<ImageView> mImageViewList;
    private LruCache<String, Bitmap> lruCache;
    private ImagePagerAdapter adapter;
    /**
     * 缓存大小：单位-M
     */
    private int cacheSize = 10;
    /**
     * 当前所在页
     */
    private int mCurrentPosition;
    private Handler handler = new Handler();

    //for user define broadcast
    public static final String EXTRA_PICTURE_SELECT_USER_DEFINE_EVENT =
            "EXTRA_PICTURE_GETTED_USER_DEFINE_EVENT";
    public static final String EXTRA_PICTURE_SELECT_USER_DEFINE_DATA =
            "EXTRA_PICTURE_GETTED_USER_DEFINE_DATA";
    public static final String EXTRA_PICTURE_SELECT_READ_ONLY = "EXTRA_PICTURE_SELECT_READ_ONLY";
    private String mUserDefineEvent;
    private String mUserData;
    private boolean mReadOnly = false;

    private TitleBuilder titleBuilder;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_edit_image;
    }

    @Override
    protected void findViewById() {
        viewPager = (FixedViewPager) findViewById(R.id.viewPager);
    }

    @Override
    protected void initView() {
        initData();
        adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(mPageChangeListener);
        viewPager.setCurrentItem(mCurrentPosition);

        setTitleText();
    }

    /**
     * 设置标题栏中心文字
     */
    private void setTitleText() {
        titleBuilder = new TitleBuilder(this);
        titleBuilder.setLeftTextOrImageListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFinish();
            }
        });
        if (!mReadOnly) {
            titleBuilder.setRightText("删除");
            titleBuilder.setRightTextOrImageListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeViews();
                }
            });
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(mCurrentPosition + 1 + "/" +
                mImagePathList.size());
        ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.BLACK);
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(getResources().getColor(R.color
                .pink));
        builder.setSpan(blueSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(whiteSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        titleBuilder.setMiddleTitleText(builder.toString());
    }

    private void removeViews() {
        String path = mImagePathList.get(mCurrentPosition);
        lruCache.remove(path);
        mImagePathList.remove(mCurrentPosition);
        mImageViewList.remove(mCurrentPosition);
        int count = mImagePathList.size();
        if (0 == count) {
            myFinish();
        } else {
            viewPager.removeAllViews();
            adapter.notifyDataSetChanged();
            setTitleText();
        }
    }

    private void initData() {
        Intent intent = getIntent();
        mUserDefineEvent = intent.getStringExtra(EXTRA_PICTURE_SELECT_USER_DEFINE_EVENT);
        mUserData = intent.getStringExtra(EXTRA_PICTURE_SELECT_USER_DEFINE_DATA);
        mReadOnly = intent.getBooleanExtra(EXTRA_PICTURE_SELECT_READ_ONLY, false);

        mCurrentPosition = intent.getIntExtra(QuestionPublishActivity.EXTRA_PICTURE_POSITION, 0);
        mImagePathList = intent.getStringArrayListExtra(QuestionPublishActivity
                .EXTRA_PICTURE_PATHS);
        mImageViewList = new ArrayList<ImageView>();
        for (int i = mImagePathList.size(); i > 0; i--) {
            PhotoView view = new PhotoView(this);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                    .MATCH_PARENT));
            mImageViewList.add(view);
        }
        lruCache = new LruCache<String, Bitmap>(cacheSize * 1024 * 1024);
    }

    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPosition = position;
            setTitleText();
        }

    };

    class ImagePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            if (position < mImageViewList.size()) {
                ((ViewPager) container).removeView(mImageViewList.get(position));
            }
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ImageView view = mImageViewList.get(position);
            displayBmp(view, mImagePathList.get(position));
            ((ViewPager) container).addView(view);
            return view;
        }
    }

    /**
     * 渲染图片
     */
    public void displayBmp(final ImageView iv, final String sourcePath) {
        if (sourcePath.contains("http://") || sourcePath.contains("https://")) {
            //img = BitmapUtils.getbitmap(sourcePath);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Glide.with(EditImageActivity.this).load(sourcePath).into(iv);
                }
            });
            return;
        }
        if (iv.getTag() != null && iv.getTag().equals(sourcePath)) {
            return;
        }
        /** 先显示默认图片，待图片获取到了再加载获取到的图片 */
        showDefault(iv);

        iv.setTag(sourcePath);
        Bitmap bitmap = lruCache.get(sourcePath);

        if (null != bitmap) {
            refreshView(iv, bitmap, sourcePath);
            return;
        } else {
            // 不在缓存则加载图片
            new Thread() {
                Bitmap img;

                public void run() {
                    try {

                        img = BitmapUtils.decodeBitmapFromFile(sourcePath);
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                refreshView(iv, img, sourcePath);
                            }
                        });
                        lruCache.put(sourcePath, img);
                    } catch (Exception e) {
                    }
                }
            }.start();
        }
    }

    private void showDefault(ImageView iv) {
        iv.setBackgroundResource(R.color.black);
    }

    private void refreshView(ImageView imageView, Bitmap bitmap, String path) {
        if (imageView != null && bitmap != null) {
            if (path != null) {
                imageView.setImageBitmap(bitmap);
                imageView.setTag(path);
            }
        }
    }

    private void myFinish() {
        if (null != mUserDefineEvent) {
            Intent intent = new Intent(mUserDefineEvent);
            intent.putStringArrayListExtra(QuestionPublishActivity.EXTRA_PICTURE_PATHS,
                    mImagePathList);
            intent.putExtra(EXTRA_PICTURE_SELECT_USER_DEFINE_DATA, mUserData);
            sendBroadcast(intent);
        } else {
            Intent data = new Intent();
            data.putStringArrayListExtra(QuestionPublishActivity.EXTRA_PICTURE_PATHS,
                    mImagePathList);
            int type = getIntent().getIntExtra("type", -1);
            if (type != -1) {
                data.putExtra("type", type);
            }
            setResult(RESULT_OK, data);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        myFinish();
    }
}
