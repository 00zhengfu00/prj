package fm.jiecao.jcvideoplayer_lib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by huashigen on 2017-08-09.
 */

public class JCVideoPlayerNoCtl extends JCVideoPlayer {
    public JCVideoPlayerNoCtl(Context context) {
        super(context);
    }

    public JCVideoPlayerNoCtl(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_layout_no_ctl;
    }

    public void startPlayLogic() {
        prepareMediaPlayer();
        onEvent(JCUserActionStandard.ON_CLICK_START_THUMB);
    }

    public void restartPlay() {
        try {
            JCMediaManager.instance().simpleExoPlayer.seekTo(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
