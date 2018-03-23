package com.physicmaster.base;

        import com.tencent.tinker.loader.app.TinkerApplication;
        import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by huashigen on 2017/2/17.
 */

public class MyTinkerApplication extends TinkerApplication {
    public MyTinkerApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.physicmaster.base.BaseApplication",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
