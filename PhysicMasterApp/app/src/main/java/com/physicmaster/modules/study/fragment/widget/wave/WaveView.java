package com.physicmaster.modules.study.fragment.widget.wave;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.physicmaster.R;


/**
 * Created by John on 2014/10/15.
 */
public class WaveView extends RelativeLayout {
    protected static final int LARGE = 1;
    protected static final int MIDDLE = 2;
    protected static final int LITTLE = 3;

    private int mAboveWaveColor;
    private int mBelowWaveColor;
    private int mProgress;
    private int mWaveHeight;
    private int mWaveMultiple;
    private int mWaveHz;

    private int mWaveToTop;

    private Wave mWave;
    private Solid mSolid;
    private Boat mBoat;

    private OnClickListener onBoatClickListener;

    private final int DEFAULT_ABOVE_WAVE_COLOR = Color.WHITE;
    private final int DEFAULT_BLOW_WAVE_COLOR = Color.WHITE;
    private final int DEFAULT_PROGRESS = 80;
    private final int SOLID_ID = 1;
    private final int WAVE_ID = 2;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //load styled attributes.
        final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R
                .styleable.WaveView, R.attr.waveViewStyle, 0);
        mAboveWaveColor = attributes.getColor(R.styleable.WaveView_above_wave_color,
                DEFAULT_ABOVE_WAVE_COLOR);
        mBelowWaveColor = attributes.getColor(R.styleable.WaveView_blow_wave_color,
                DEFAULT_BLOW_WAVE_COLOR);
        mProgress = attributes.getInt(R.styleable.WaveView_progress, DEFAULT_PROGRESS);
        mWaveHeight = attributes.getInt(R.styleable.WaveView_wave_height, MIDDLE);
        mWaveMultiple = attributes.getInt(R.styleable.WaveView_wave_length, LARGE);
        mWaveHz = attributes.getInt(R.styleable.WaveView_wave_hz, MIDDLE);
        attributes.recycle();

        mWave = new Wave(context, null);
        mWave.initializeWaveSize(mWaveMultiple, mWaveHeight, mWaveHz);
        mWave.setAboveWaveColor(mAboveWaveColor);
        mWave.setBelowWaveColor(mBelowWaveColor);
        mWave.initializePainters();
        mWave.setId(WAVE_ID);

        mSolid = new Solid(context, null);
        mSolid.setAboveWavePaint(mWave.getAboveWavePaint());
        mSolid.setBelowWavePaint(mWave.getBelowWavePaint());
        mSolid.setId(SOLID_ID);

        mBoat = new Boat(context, null);

        LayoutParams paramsSolid = (LayoutParams) mSolid.getLayoutParams();
        paramsSolid.addRule(ALIGN_PARENT_BOTTOM);
        LayoutParams paramsWave = (LayoutParams) mWave.getLayoutParams();
        paramsWave.addRule(ABOVE, mSolid.getId());

        LayoutParams paramsBoat = (LayoutParams) mBoat.getLayoutParams();
        paramsBoat.addRule(ABOVE, mWave.getId());
        paramsBoat.addRule(CENTER_HORIZONTAL);
        float desity = getResources().getDisplayMetrics().density;
        paramsBoat.bottomMargin = (int) (-8 * desity);
        addView(mBoat, paramsBoat);
        addView(mWave, paramsWave);
        addView(mSolid, paramsSolid);

        setProgress(mProgress);
    }

    public void setOnBoatClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            mBoat.setOnClickListener(onClickListener);
        }
    }

    public void setProgress(int progress) {
        this.mProgress = progress > 100 ? 100 : progress;
        computeWaveToTop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            computeWaveToTop();
        }
    }

    private void computeWaveToTop() {
//        mWaveToTop = (int) (getHeight() * (1f - mProgress / 100f));
//        ViewGroup.LayoutParams params = mWave.getLayoutParams();
//        if (params != null) {
//            ((LayoutParams) params).topMargin = mWaveToTop;
//        }
//        mWave.setLayoutParams(params);
//
//        ViewGroup.LayoutParams paramsBoat = mBoat.getLayoutParams();
//        if (params != null) {
//            ((LayoutParams) params).bottomMargin = (int) (getHeight() * (mProgress * 2 / 100f));
//        }
//        mBoat.setLayoutParams(paramsBoat);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Force our ancestor class to save its state
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.progress = mProgress;
        return ss;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
    }

    private static class SavedState extends BaseSavedState {
        int progress;

        /**
         * Constructor called from {@link android.widget.ProgressBar#onSaveInstanceState()}
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void upgradeBoat(Bitmap bitmapbBat, boolean hasNewMsg) {
        if (bitmapbBat != null) {
            mBoat.upgradeBoat(bitmapbBat, hasNewMsg);
        }
    }
}
