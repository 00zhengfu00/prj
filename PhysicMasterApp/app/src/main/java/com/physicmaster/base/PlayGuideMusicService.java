package com.physicmaster.base;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.physicmaster.R;

/**
 * Created by huashigen on 2017-08-25.
 */

public class PlayGuideMusicService extends Service {
    private MediaPlayer mediaPlayer;
    private LocalBroadcastManager localBroadcastManager;
    public static final String FILTER_STOP_SERVICE = "filter_stop_service";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.guide_bgm);
        mediaPlayer.setLooping(true);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(stopReceiver, new IntentFilter(FILTER_STOP_SERVICE));
    }

    private BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer = null;
            }
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }
}
