
package com.physicmaster.log;

import android.util.Log;


public class AndroidLogger extends Logger {

    private static final String DEFAULT_LOGTAG = "PhysicMaster";

    private static final int v = 1;
    private static final int d = 2;
    private static final int i = 3;
    private static final int w = 4;
    private static final int e = 5;
    private int logLevel = 1;
    private String tag;

    public AndroidLogger() {
        tag = DEFAULT_LOGTAG;
    }

    public AndroidLogger(String tag) {
        this.tag = tag;
    }

    @Override
    public void debug(String message) {
        if (isDebugEnabled()) {
            Log.d(tag, message);
        }
    }

    @Override
    public void debug(String message, Throwable tr) {
        if (isDebugEnabled()) {
            Log.d(tag, message, tr);
        }
    }

    @Override
    public void info(String message) {
        if (isInfoEnabled()) {
            Log.i(tag, message);
        }
    }

    @Override
    public void info(String message, Throwable tr) {
        if (isInfoEnabled()) {
            Log.i(tag, message, tr);
        }
    }

    @Override
    public void error(String message) {
        if (isErrorEnabled()) {
            Log.e(tag, message);
        }
    }

    @Override
    public void error(String message, Throwable tr) {
        if (isErrorEnabled()) {
            Log.e(tag, message, tr);
        }
    }

    @Override
    public void warn(String message) {
        if (isWarnEnabled()) {
            Log.w(tag, message);
        }
    }

    @Override
    public void warn(String message, Throwable tr) {
        if (isWarnEnabled()) {
            Log.w(tag, message, tr);
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return logLevel <= d;
    }

    @Override
    public boolean isErrorEnabled() {
        return logLevel <= e;
    }

    @Override
    public boolean isInfoEnabled() {
        return logLevel <= i;
    }

    @Override
    public boolean isWarnEnabled() {
        return logLevel <= w;
    }

}
