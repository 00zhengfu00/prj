package com.physicmaster.modules.discuss.common.infra;

public interface DefaultTaskCallback {
    void onFinish(String key, int result, Object attachment);
}