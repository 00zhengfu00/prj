package com.lswuyou.tv.pm.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.adapter.TvGridAdapter;
import com.lswuyou.tv.pm.cache.CachedPlayRecord;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.net.response.video.VideoDetaiInfo;
import com.lswuyou.tv.pm.utils.UIUtils;
import com.lswuyou.tv.pm.view.EditRecordDialog;
import com.lswuyou.tv.pm.view.TitleBarView;

import java.util.ArrayList;
import java.util.List;

import reco.frame.tv.view.TvGridView;


public class PlayRecordActivity extends BaseFragmentActivity {
    private TitleBarView mTitleBarView;
    private List<VideoDetaiInfo> playRecords;
    private TvGridAdapter tvGridAdapter;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.playRecord);
        loadFrag();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_play_record;
    }

    private void loadFrag() {
        Object objPlayRecord = CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys.PLAY_RECORD, CachedPlayRecord.class);
        if (objPlayRecord == null) {
            return;
        }
        CachedPlayRecord records = (CachedPlayRecord) objPlayRecord;
        playRecords = new ArrayList<>();
        if (records.videoDetaiInfoList != null) {
            playRecords.addAll(records.videoDetaiInfoList);
        }
        TvGridView tvGridView = (TvGridView) findViewById(R.id.tgv_imagelist);
        tvGridAdapter = new TvGridAdapter(PlayRecordActivity.this, playRecords);
        tvGridAdapter.setOnMenuClickListener(new TvGridAdapter.OnMenuClickListener() {
            @Override
            public void onMenuClick(final int position) {
                final EditRecordDialog editRecordDialog = new EditRecordDialog();
                editRecordDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.btn_delete) {
                            playRecords.remove(position);
                        } else {
                            playRecords.clear();
                        }
                        editRecordDialog.dismiss();
                        tvGridAdapter.notifyDataSetChanged();
                        CachedPlayRecord record = new CachedPlayRecord();
                        record.videoDetaiInfoList = playRecords;
                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.PLAY_RECORD, record);
                    }
                });
                editRecordDialog.show(getFragmentManager(), "editRecord");
            }
        });
        tvGridView.setAdapter(tvGridAdapter);
        tvGridView.setOnItemClickListener(new TvGridView.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                VideoDetaiInfo videoDetaiInfo = playRecords.get(position);
                if (1 == videoDetaiInfo.tvVideoType) {
                    Intent intent = new Intent(PlayRecordActivity.this, VideoIntroActivity.class);
                    intent.putExtra("videoId", videoDetaiInfo.videoId);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(PlayRecordActivity.this, VideoIntroForExcActivity.class);
                    intent.putExtra("videoId", videoDetaiInfo.videoId);
                    startActivity(intent);
                }
            }
        });
    }
}
