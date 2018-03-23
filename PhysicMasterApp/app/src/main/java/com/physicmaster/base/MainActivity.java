package com.physicmaster.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.IUnreadCountCallback;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.physicmaster.R;
import com.physicmaster.modules.account.LoginDialogActivity;
import com.physicmaster.modules.discuss.NimLoginService;
import com.physicmaster.modules.discuss.fragment.DiscussFragment;
import com.physicmaster.modules.discuss.session.SessionHelper;
import com.physicmaster.modules.explore.fragment.ExploreFragment;
import com.physicmaster.modules.mine.fragment.MyFragment;
import com.physicmaster.modules.ranking.fragment.RankingFragment;
import com.physicmaster.modules.study.fragment.StudyFragmentV2;
import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;
import com.physicmaster.modules.videoplay.cache.db.VideoManager;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.CustomFragmentTabHost;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private int mImageViewArray[] = {R.drawable.study_selector,
            R.drawable.course_selector, R.drawable.discuss_selector, R.drawable.ranking_selector, R.drawable.mine_selector
    };
    private CustomFragmentTabHost mTabhost;
    private View mineIcon, msgIcon;
    private boolean showFirstTab = false;
    private AsyncTask<String, Integer, Boolean> checkDownloadedVideos;
    private String lastTabId = "tab1";

    @Override
    protected void findViewById() {
        mTabhost = (CustomFragmentTabHost) findViewById(R.id.tabhost);
    }

    @Override
    protected void initView() {
        mTabhost.setup(this, getSupportFragmentManager(), R.id.fl_realcontent);
        final TabHost.TabSpec tab1 = mTabhost.newTabSpec("tab1").setIndicator(getIconView(0));
        TabHost.TabSpec tab2 = mTabhost.newTabSpec("tab2").setIndicator(getIconView(1));
        TabHost.TabSpec tab3 = mTabhost.newTabSpec("tab3").setIndicator(msgIcon = getIconView(2));
        TabHost.TabSpec tab4 = mTabhost.newTabSpec("tab4").setIndicator(getIconView(3));
        TabHost.TabSpec tab5 = mTabhost.newTabSpec("tab5").setIndicator(mineIcon = getIconView(4));
        mTabhost.addTab(tab1, StudyFragmentV2.class, null);
        mTabhost.addTab(tab2, ExploreFragment.class, null);
        mTabhost.addTab(tab3, DiscussFragment.class, null);
        mTabhost.addTab(tab4, RankingFragment.class, null);
        mTabhost.addTab(tab5, MyFragment.class, null);
        mTabhost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);

        mTabhost.setOnTabChagedListener(new CustomFragmentTabHost.OnTabChagedListener() {
            @Override
            public boolean onTabChanged(String tabId) {
                if (tabId.equals("tab5")) {
                    if (1 == BaseApplication.getUserData().isTourist) {
                        Utils.gotoLogin(MainActivity.this);
                        mTabhost.setCurrentTabByTag(lastTabId);
                        return false;
                    }
                }
                lastTabId = tabId;
                return true;
            }
        });

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(unReadMsgReceiver, new IntentFilter(UNREADMSG));
        onParseIntent();
    }

    private View getIconView(int position) {
        String[] mTabNames = getResources().getStringArray(R.array.tab_names);
        View view = getLayoutInflater().inflate(R.layout.item_icon, null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv.getLayoutParams();
        params.width = getResources().getDimensionPixelSize(
                R.dimen.home_icon_width);
        params.height = getResources().getDimensionPixelSize(
                R.dimen.home_icon_height);
        iv.setLayoutParams(params);
        iv.setImageResource(mImageViewArray[position]);
        TextView tv = (TextView) view.findViewById(R.id.tv1);
        tv.setText(mTabNames[position]);
        return view;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
        if (!isExit) {
            UIUtils.showToast(MainActivity.this, "再按一次退出程序");
            mHandler.sendEmptyMessageDelayed(0, 3000);
            isExit = true;
        } else {
            super.onBackPressed();
        }
    }

    private boolean isExit = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        boolean barExist2 = navigationBarExist2(this);
//        if (barExist2) {
//            int screenHeight = ScreenUtils.getScreenHeight();
//            if (screenHeight < 2000) {
//                if (hasFocus && Build.VERSION.SDK_INT >= 19) {
//                    View decorView = getWindow().getDecorView();
//                    decorView.setSystemUiVisibility(
//                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//                }
//            }
//        }
    }

    /**
     * 判断是否存在导航栏
     * 此方法在模拟器还是在真机都是完全正确
     *
     * @param activity
     * @return
     */
    public static boolean navigationBarExist2(Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    private LocalBroadcastManager localBroadcastManager;
    public static final String UNREADMSG = "unreadmsg";
    private BroadcastReceiver unReadMsgReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean hasUnreadMsg = intent.getBooleanExtra("hasUnreadMsg", false);
            if (hasUnreadMsg) {
                msgIcon.findViewById(R.id.view_feedback_red).setVisibility(View.VISIBLE);
            } else {
                msgIcon.findViewById(R.id.view_feedback_red).setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unReadMsgReceiver != null) {
            localBroadcastManager.unregisterReceiver(unReadMsgReceiver);
        }
        if (checkDownloadedVideos != null) {
            checkDownloadedVideos.cancel(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FeedbackAPI.getFeedbackUnreadCount(new IUnreadCountCallback() {
            @Override
            public void onSuccess(final int i) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (i > 0) {
                            mineIcon.findViewById(R.id.view_feedback_red).setVisibility(View
                                    .VISIBLE);
                        } else {
                            mineIcon.findViewById(R.id.view_feedback_red).setVisibility(View
                                    .GONE);
                        }
                    }
                });
            }

            @Override
            public void onError(final int i, final String s) {
            }
        });
        deleteOldCache();
        if (showFirstTab) {
            showFirstTab = false;
            mTabhost.setCurrentTab(0);
        }
    }

    /**
     * 非会员删掉老缓存
     */
    private void deleteOldCache() {
        checkDownloadedVideos = new
                AsyncTask<String, Integer, Boolean>() {
                    @Override
                    protected Boolean doInBackground(String... params) {
                        if (isCancelled()) {
                            return false;
                        }
                        VideoManager videoManager = new VideoManager(MainActivity.this);
                        List<VideoInfo> videoInfos = videoManager.getVideosByVideoType(0, VideoInfo.STATE_DOWNLOADED);
                        for (VideoInfo videoInfo : videoInfos) {
                            videoManager.deleteVideoByVideoId(videoInfo.getId());
                        }
                        return true;
                    }

                    @Override
                    protected void onPostExecute(Boolean bool) {
                        super.onPostExecute(bool);
                    }
                };
        checkDownloadedVideos.execute("");
    }

    private void onParseIntent() {
        if (!BaseApplication.isNimLogin()) {
            UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication.getUserData();
            if (loginVoBean != null && 1 == loginVoBean.isImAutoConnect) {
                Intent loginIntent = new Intent(MainActivity.this, NimLoginService.class);
                loginIntent.putExtra("account", loginVoBean.imUserId);
                loginIntent.putExtra("token", loginVoBean.imToken);
                startService(loginIntent);
            }
        }
        Intent intent = getIntent();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra
                    (NimIntent.EXTRA_NOTIFY_CONTENT);
            if (messages != null && messages.size() > 0) {
                IMMessage message = messages.get(0);
                switch (message.getSessionType()) {
                    case P2P:
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("id", BaseApplication.getUserData().dtUserId);
                        jsonObject.put("p", BaseApplication.getUserData().portraitSmall);
                        jsonObject.put("account", message.getSessionId());
                        if (BaseApplication.getStartupDataBean().imAdminList != null &&
                                BaseApplication.getStartupDataBean().imAdminList.size() > 0) {
                            jsonObject.put("adminUserId", BaseApplication.getStartupDataBean()
                                    .imAdminList.get(0).imUserId);
                        }
                        jsonObject.put("isFriend", true);
                        SessionHelper.startP2PSession(this, jsonObject.toJSONString());
                        break;
                    case Team:
                        SessionHelper.startTeamSession(this, message.getSessionId());
                        break;
                    default:
                        break;
                }
            }
        } else if (intent.hasExtra("chapterId")) {
            showFirstTab = true;
            String chapterId = intent.getStringExtra("chapterId");
            Intent intent1 = new Intent(StudyFragmentV2.ON_SUBJECT_CHANGED);
            intent1.putExtra("chapterId", chapterId);
            localBroadcastManager.sendBroadcast(intent1);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        onParseIntent();
    }

    public int getTabHostHeight() {
        if (mTabhost != null) {
            return mTabhost.getTabWidget().getHeight();
        }
        return 0;
    }
}

