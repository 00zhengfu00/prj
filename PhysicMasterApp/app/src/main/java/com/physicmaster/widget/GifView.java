package com.physicmaster.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.physicmaster.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by Cuneyt on 4.10.2015.
 * Gifview
 */
public class GifView extends View {

    private static final int DEFAULT_MOVIE_VIEW_DURATION = 1000;

    private int mMovieResourceId;
    private Movie movie;

    private long mMovieStart;
    private int mCurrentAnimationTime;


    /**
     * Position for drawing animation frames in the center of the view.
     */
    private float mLeft;
    private float mTop;

    /**
     * GifView
     * Scaling factor to fit the animation within view bounds.
     */
    private float mScale;

    /**
     * Scaled movie frames width and height.
     */
    private int mMeasuredMovieWidth;
    private int mMeasuredMovieHeight;

    private volatile boolean mPaused;
    private boolean mVisible = true;

    private String DOWNLOAD_ADDR = "http://img3.3lian" +
            ".com/2006/013/08/20051105155416881.gif";

    private int playTime = -1;
    private OnPlayFinishListener onPlayFinishListener;

    public GifView(Context context) {
        this(context, null);
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, R.styleable.CustomTheme_gifViewStyle);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setViewAttributes(context, attrs, defStyle);
    }

    @SuppressLint("NewApi")
    private void setViewAttributes(Context context, AttributeSet attrs, int defStyle) {

        /**
         * Starting from HONEYCOMB(Api Level:11) have to turn off HW acceleration to draw
         * Movie on Canvas.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        final TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.GifView, defStyle, R.style.Widget_GifView);

        //-1 is default value
        mMovieResourceId = array.getResourceId(R.styleable.GifView_gif, -1);
        mPaused = array.getBoolean(R.styleable.GifView_paused, false);

        array.recycle();

        if (mMovieResourceId != -1) {
            movie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        }
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public void setOnPlayFinishListener(OnPlayFinishListener onPlayFinishListener) {
        this.onPlayFinishListener = onPlayFinishListener;
    }

    public void setGifResource(int movieResourceId) {
        this.mMovieResourceId = movieResourceId;
        movie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        requestLayout();
        if (playTime != -1) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pause();
                    onPlayFinishListener.onPlayFinish();
                }
            }, movie.duration() * playTime);
        }
    }

    public int getGifResource() {
        return this.mMovieResourceId;
    }


    public void play() {
        if (this.mPaused) {
            this.mPaused = false;

            /**
             * Calculate new movie start time, so that it resumes from the same
             * frame.
             */
            mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;

            invalidate();
        }
    }

    public void pause() {
        if (!this.mPaused) {
            this.mPaused = true;

            invalidate();
        }

    }


    public boolean isPaused() {
        return this.mPaused;
    }

    public boolean isPlaying() {
        return !this.mPaused;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (movie != null) {
            int movieWidth = movie.width();
            int movieHeight = movie.height();

			/*
             * Calculate horizontal scaling
			 */
            float scaleH = 1f;
            int measureModeWidth = MeasureSpec.getMode(widthMeasureSpec);

            if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
                int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
                scaleH = (float) maximumWidth / (float) movieWidth;
            }

			/*
             * calculate vertical scaling
			 */
            float scaleW = 1f;
            int measureModeHeight = MeasureSpec.getMode(heightMeasureSpec);

            if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
                int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
                scaleW = (float) maximumHeight / (float) movieHeight;
            }

			/*
             * calculate overall scale
			 */
            mScale = Math.min(scaleH, scaleW);

            mMeasuredMovieWidth = (int) (movieWidth * mScale);
            mMeasuredMovieHeight = (int) (movieHeight * mScale);

            setMeasuredDimension(mMeasuredMovieWidth, mMeasuredMovieHeight);

        } else {
            /*
             * No movie set, just set minimum available size.
			 */
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        /*
         * Calculate mLeft / mTop for drawing in center
		 */
        mLeft = (getWidth() - mMeasuredMovieWidth) / 2f;
        mTop = (getHeight() - mMeasuredMovieHeight) / 2f;

        mVisible = getVisibility() == View.VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (movie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    /**
     * Invalidates view only if it is mVisible.
     * <br>
     * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
     */
    @SuppressLint("NewApi")
    private void invalidateView() {
        if (mVisible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }
        }
    }

    /**
     * Calculate current animation time
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        int dur = movie.duration();

        if (dur == 0) {
            dur = DEFAULT_MOVIE_VIEW_DURATION;
        }

        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    /**
     * Draw current GIF frame
     */
    private void drawMovieFrame(Canvas canvas) {

        movie.setTime(mCurrentAnimationTime);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(mScale, mScale);
        movie.draw(canvas, mLeft / mScale, mTop / mScale);
        canvas.restore();
    }

    @SuppressLint("NewApi")
    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        mVisible = screenState == SCREEN_STATE_ON;
        invalidateView();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    public void loadGif(String url) {
        this.DOWNLOAD_ADDR = url;
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpTest();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void httpTest() {
        try {
            URL url = new URL(DOWNLOAD_ADDR);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            int size = connection.getContentLength();
            Log.e(TAG, "size = " + size);
            InputStream in = connection.getInputStream();
            byte[] array = streamToBytes(in);
            movie = Movie.decodeByteArray(array, 0, array.length);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] streamToBytes(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = is.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (java.io.IOException e) {
        }
        return os.toByteArray();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    requestLayout();
                    invalidate();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public interface OnPlayFinishListener {
        void onPlayFinish();
    }
}