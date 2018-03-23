package com.physicmaster.base;

import android.os.SystemClock;

public final class SystemParams {

    public static long getServerTime() {
        if (BaseApplication.getServerTime() != 0 && BaseApplication.getStartTime() != 0) {
            long time = BaseApplication.getServerTime() + (SystemClock.elapsedRealtime() -
                    BaseApplication.getStartTime());
            return time;
        }
        return System.currentTimeMillis();
    }

}
