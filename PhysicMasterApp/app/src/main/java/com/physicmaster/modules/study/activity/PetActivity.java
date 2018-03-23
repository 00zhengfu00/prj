package com.physicmaster.modules.study.activity;

import android.graphics.Typeface;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.user.PetInfoResponse;
import com.physicmaster.net.response.user.PetInfoResponse.DataBean.PetVoBean;
import com.physicmaster.net.service.user.PetAdvancedService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.AdvancePopupWindow;
import com.physicmaster.widget.gif.AlxGifHelper;
import com.pnikosis.materialishprogress.ProgressWheel;

import pl.droidsonroids.gif.GifImageView;

public class PetActivity extends BaseActivity {


    private static final String TAG = "PetActivity";
    private TextView tvAttack;
    private TextView tvAttres;
    private TextView tvSpecailAttack;
    private TextView tvSpecailDefense;
    private TextView tvDefense;
    private TextView tvHp;
    private TextView tvPet;
    private TextView tvMiddlePet;
    private TextView tvSpeed;
    private View petContainer;
    private TextView tvNature;
    private TextView tvJia;
    private TextView tvJian;
    private Button btnAdvanced;
    private PetVoBean petInfo;
    private View gifGroup;

    @Override
    protected void findViewById() {

        tvAttack = (TextView) findViewById(R.id.tv_attack);
        tvAttres = (TextView) findViewById(R.id.tv_attres);
        tvSpecailAttack = (TextView) findViewById(R.id.tv_specail_attack);
        tvSpecailDefense = (TextView) findViewById(R.id.tv_specail_defense);
        tvDefense = (TextView) findViewById(R.id.tv_defense);
        tvSpeed = (TextView) findViewById(R.id.tv_speed);
        tvHp = (TextView) findViewById(R.id.tv_hp);
        tvPet = (TextView) findViewById(R.id.tv_pet);
        tvMiddlePet = (TextView) findViewById(R.id.title_middle_pet);
        // gifPet = (GifView) findViewById(R.id.gif_pet);


        tvNature = (TextView) findViewById(R.id.tv_nature);
        tvJia = (TextView) findViewById(R.id.tv_jia);
        tvJian = (TextView) findViewById(R.id.tv_jian);


        ImageView ivLeftBack = (ImageView) findViewById(R.id.title_left_back);
        btnAdvanced = (Button) findViewById(R.id.btn_advanced);
        Typeface mtypeface = Typeface.createFromAsset(getAssets(), "fonts/happy_font.ttf");
        tvAttack.setTypeface(mtypeface);
        tvSpecailAttack.setTypeface(mtypeface);
        tvSpecailDefense.setTypeface(mtypeface);
        tvDefense.setTypeface(mtypeface);
        tvSpeed.setTypeface(mtypeface);
        tvHp.setTypeface(mtypeface);
        tvPet.setTypeface(mtypeface);

        ivLeftBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isTranslucentStatusBar()) {
                Window window = getWindow();
                // Translucent status bar
                window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        petInfo = getIntent().getParcelableExtra("petInfo");
        RelativeLayout rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlTitle.getLayoutParams();
        layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);
        rlTitle.setLayoutParams(layoutParams);
        showPetInfo(petInfo);
    }

    private void advanced() {
        final PetAdvancedService service = new PetAdvancedService(PetActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<PetInfoResponse>() {

            @Override
            public void onGetData(final PetInfoResponse data) {
                Log.d(TAG, "加载完成：onGetData: " + data.msg);
                UIUtils.showToast(PetActivity.this, "恭喜你，进阶成功");
                AdvancePopupWindow window = new AdvancePopupWindow(PetActivity.this);
                window.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        showPetInfo(data.data.petVo);
                    }
                });
                window.showPopupWindow(gifGroup);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "加载失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(PetActivity.this, errorMsg);
            }
        });
        service.postLogined("userPetId=" + petInfo.userPetId, false);
    }

    private void showPetInfo(PetVoBean petInfo) {
        if (petInfo.isUpStage == 0) {
            btnAdvanced.setVisibility(View.GONE);
        } else {
            btnAdvanced.setVisibility(View.VISIBLE);
        }

        btnAdvanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advanced();
            }
        });

        if (!TextUtils.isEmpty(petInfo.petImg)) {
            //Glide.with(this).load(petInfo.petImg).asGif().into(ivPet);
            //ivPet.setImageBitmap(BitmapUtils.ratio(petInfo.petImg,1f,1f));
            // gifPet.loadGif(petInfo.petImg);
            gifGroup = findViewById(R.id.gif_pet);
            AlxGifHelper.displayImage(petInfo.petImg,
                    (GifImageView) gifGroup.findViewById(R.id.gif_photo_view),
                    (ProgressWheel) gifGroup.findViewById(R.id.progress_wheel),
                    (TextView) gifGroup.findViewById(R.id.tv_progress), gifGroup
                            .getWidth()
            );
        }

        if (!TextUtils.isEmpty(petInfo.attrAttack + "")) {
            final String name = "攻击   ";
            String str = name + petInfo.attrAttack;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFF23A0BF), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvAttack.setText(sp);
        } else {
            tvAttack.setText("");
        }
        if (!TextUtils.isEmpty(petInfo.nature1 + "") && !TextUtils.isEmpty(petInfo.nature2 + "")) {
            tvAttres.setText(petInfo.nature1 + " | " + petInfo.nature2);
        } else {
            tvAttres.setText("");
        }
        if (!TextUtils.isEmpty(petInfo.attrSpecialAttack + "")) {
            final String name = "特攻   ";
            String str = name + petInfo.attrSpecialAttack;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFF23A0BF), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvSpecailAttack.setText(sp);
        } else {
            tvSpecailAttack.setText("");
        }
        if (!TextUtils.isEmpty(petInfo.attrSpecialDefense + "")) {
            final String name = "特防   ";
            String str = name + petInfo.attrSpecialDefense;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFF23A0BF), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvSpecailDefense.setText(sp);
        } else {
            tvSpecailDefense.setText("");
        }
        if (!TextUtils.isEmpty(petInfo.attrDefense + "")) {
            final String name = "防御   ";
            String str = name + petInfo.attrDefense;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFF23A0BF), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvDefense.setText(sp);
        } else {
            tvDefense.setText("");
        }
        if (!TextUtils.isEmpty(petInfo.attrHp + "")) {
            final String name = "H P   ";
            String str = name + petInfo.attrHp;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFF23A0BF), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvHp.setText(sp);
        } else {
            tvHp.setText("");
        }
        if (!TextUtils.isEmpty(petInfo.attrSpeed + "")) {
            final String name = "速度   ";
            String str = name + petInfo.attrSpeed;
            final SpannableStringBuilder sp = new SpannableStringBuilder(str);
            sp.setSpan(new ForegroundColorSpan(0xFF23A0BF), name.length(), str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
            tvSpeed.setText(sp);
        } else {
            tvSpeed.setText("");
        }

        if (!TextUtils.isEmpty(petInfo.characterName + "")) {
            tvNature.setText("性格:   " + petInfo.characterName);
        } else {
            tvNature.setText("");
        }

        if (!TextUtils.isEmpty(petInfo.promoteAttrStr + "")) {
            tvJia.setText(petInfo.promoteAttrStr + "");
        } else {
            tvJia.setText("");
        }

        if (!TextUtils.isEmpty(petInfo.suppressAttrStr + "")) {
            tvJian.setText(petInfo.suppressAttrStr + "");
        } else {
            tvJian.setText("");
        }


        if (!TextUtils.isEmpty(petInfo.petName + "") && !TextUtils.isEmpty(petInfo.petLevel + "")) {
            tvPet.setText(petInfo.petName + "   Lv" + petInfo.petLevel);
        } else {
            tvPet.setText("");
        }
        if (!TextUtils.isEmpty(petInfo.petName + "")) {
            tvMiddlePet.setText(petInfo.petName + "");
        } else {
            tvMiddlePet.setText("");
        }
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_pet;
    }
}
