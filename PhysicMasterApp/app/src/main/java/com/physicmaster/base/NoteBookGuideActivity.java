package com.physicmaster.base;

import android.content.Intent;

import com.netease.nimlib.sdk.NimIntent;
import com.physicmaster.R;
import com.physicmaster.net.response.user.UserDataResponse;

public class NoteBookGuideActivity extends BaseActivity {


    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_save_mika).setOnClickListener(v -> {
            UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication.getUserData();
            if (loginVoBean == null || loginVoBean.isInitial == 0) {
                startActivity(new Intent(this, GuideActivity.class));
            } else {
                Intent intent = getIntent();
                Intent tIntent = new Intent(this, MainActivity.class);
                if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                    tIntent.putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, intent.getSerializableExtra
                            (NimIntent.EXTRA_NOTIFY_CONTENT));
                }
                startActivity(tIntent);
            }
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_note_book_guide;
    }
}
