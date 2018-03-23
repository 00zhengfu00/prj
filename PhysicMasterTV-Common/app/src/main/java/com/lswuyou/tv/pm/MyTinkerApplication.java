package com.lswuyou.tv.pm;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

public class MyTinkerApplication extends TinkerApplication {
    public MyTinkerApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.lswuyou.tv.pm.BaseApplication",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
