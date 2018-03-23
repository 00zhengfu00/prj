package com.physicmaster.modules.study.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.modules.mine.activity.question.AnswerDetailsActivity;
import com.physicmaster.modules.study.fragment.dialogfragment.CommonDialogFragment;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.PetInfoResponse;
import com.physicmaster.net.service.user.PetInfoService;
import com.physicmaster.net.response.user.PetInfoResponse.DataBean;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.gif.AlxGifHelper;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

/**
 * Created by huashigen on 2017-07-03.
 */

public class DriverHouseActivity extends BaseActivity implements View.OnClickListener {
    private int mImageViewArray[] = {R.mipmap.task, R.mipmap.coin, R.mipmap.backpack, R.mipmap.message};
    private ImageView ivBack;
    private View viewTask;
    private View viewCoin;
    private View viewBackpack;
    private View viewMessage;
    private DataBean dataBean;
    private SoundPool soundPool;
    private Map<String, Integer> map = new HashMap<>();

    @Override
    protected void findViewById() {
        viewTask = findViewById(R.id.layout_task);
        viewCoin = findViewById(R.id.layout_coin);
        viewBackpack = findViewById(R.id.layout_backpack);
        viewMessage = findViewById(R.id.layout_message);
        ivBack = (ImageView) findViewById(R.id.title_left_imageview);
    }

    @Override
    protected void initView() {
        getIconView(0, viewTask);
        getIconView(1, viewCoin);
        getIconView(2, viewBackpack);
        getIconView(3, viewMessage);

        viewTask.setOnClickListener(this);
        viewCoin.setOnClickListener(this);
        viewBackpack.setOnClickListener(this);
        viewMessage.setOnClickListener(this);

        ivBack.setOnClickListener(v -> DriverHouseActivity.this.finish());
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        map.put("buttonClick", soundPool.load(DriverHouseActivity.this, R.raw.btnclick, 1));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_driver_house;
    }

    private View getIconView(int position, View view) {
        view.setBackgroundColor(Color.TRANSPARENT);
        String[] mTabNames = getResources().getStringArray(R.array.game_funcs);
        ImageView iv = (ImageView) view.findViewById(R.id.iv);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) iv.getLayoutParams();
        params.width = getResources().getDimensionPixelSize(
                R.dimen.dimen_44);
        params.height = getResources().getDimensionPixelSize(
                R.dimen.dimen_35);
        iv.setLayoutParams(params);
        iv.setImageResource(mImageViewArray[position]);
        TextView tv = (TextView) view.findViewById(R.id.tv1);
        tv.setTextColor(0xfff8a318);
        tv.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.size_13));
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
        tv.setText(mTabNames[position]);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (BaseApplication.getUserData().isTourist == 1) {
            Utils.gotoLogin(DriverHouseActivity.this);
            return;
        }
        Boolean isSoundSwitch = SpUtils.getBoolean(mContext, "isSoundSwitch", true);
        if (isSoundSwitch) {
            soundPool.play(map.get("buttonClick"), 1.0f, 1.0f, 1, 0, 1);
        }
        switch (v.getId()) {
            case R.id.layout_task:
                startActivity(new Intent(this, TaskActivity.class));
                break;
            case R.id.layout_coin:
                startActivity(new Intent(this, GoldActivity.class));
                break;
            case R.id.layout_backpack:
                startActivity(new Intent(this, BackpackActivity.class));
                break;
            case R.id.layout_message:
                Intent intent = new Intent(this, InformationActivity.class);
                if (dataBean != null && dataBean.newMsgCountVo != null) {
                    intent.putExtra("newMsgs", dataBean.newMsgCountVo);
                }
                startActivity(intent);
                break;
            case R.id.rl_gif:
                Intent intent2 = new Intent(DriverHouseActivity.this, PetActivity.class);
                if (dataBean != null && dataBean.petVo != null) {
                    intent2.putExtra("petInfo", dataBean.petVo);
                    startActivity(intent2);
                } else {
                    UIUtils.showToast(DriverHouseActivity.this, "获取宠物信息失败");
                }
                break;
        }
    }

    /**
     * 获取宠物信息
     */
    private void getPetInfo() {
        PetInfoService service = new PetInfoService(this);
        service.setCallback(new IOpenApiDataServiceCallback<PetInfoResponse>() {
            @Override
            public void onGetData(PetInfoResponse data) {
                dataBean = data.data;
                refreshUI();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(DriverHouseActivity.this, errorMsg);
            }
        });
        service.postLogined("", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPetInfo();
    }

    /**
     * 刷新页面
     */
    private void refreshUI() {
        if (null == dataBean) {
            return;
        }
        if (1 == dataBean.hasNewMsg) {
            viewMessage.findViewById(R.id.view_feedback_red).setVisibility(View.VISIBLE);
        } else {
            viewMessage.findViewById(R.id.view_feedback_red).setVisibility(View.GONE);
        }
        if (1 == dataBean.petVo.isUpStage) {
            findViewById(R.id.iv_upgrade).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.iv_upgrade).setVisibility(View.GONE);
        }
        DataBean.PetVoBean petVo = dataBean.petVo;
        if (!TextUtils.isEmpty(petVo.petImg)) {
            View gifGroup = findViewById(R.id.rl_gif);
            AlxGifHelper.displayImage(petVo.petImg,
                    (GifImageView) gifGroup.findViewById(R.id.gif_photo_view),
                    (ProgressWheel) gifGroup.findViewById(R.id.progress_wheel),
                    (TextView) gifGroup.findViewById(R.id.tv_progress), gifGroup.getWidth()
            );
            gifGroup.setOnClickListener(this);
            TextView tvPetName = (TextView) findViewById(R.id.tv_pet_name);
            tvPetName.setText(petVo.petName + "Lv" + petVo.petLevel);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            int progress = 0;
            try {
                progress = (int) ((petVo.petPoint - petVo.minPetPoint) / (float) (petVo.maxPetPoint - petVo.minPetPoint) * 100);
                progress = (progress < 0) ? 0 : progress;
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressBar.setProgress(progress);
            TextView tvJoke = (TextView) findViewById(R.id.tv_joke);
            if (!TextUtils.isEmpty(dataBean.petJoke)) {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/happy_font.ttf");
                tvJoke.setTypeface(typeface);
                tvJoke.setText(dataBean.petJoke);
            } else {
                tvJoke.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        if (null != soundPool) {
            soundPool.release();
            soundPool = null;
        }
        super.onDestroy();
    }
}
