package com.physicmaster.modules.study.fragment.dialogfragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.modules.account.LoginActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.account.CheckInDayPlan;
import com.physicmaster.net.response.account.GetPlanListResponse;
import com.physicmaster.net.response.account.GetPlanListWraper;
import com.physicmaster.net.response.account.SignInResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.GetPlanListService;
import com.physicmaster.net.service.account.SignInAfterService;
import com.physicmaster.net.service.account.SignInService;
import com.physicmaster.utils.BitmapUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.LocusView;

import java.util.List;

import static com.physicmaster.R.id.tv;

/**
 * Created by huashigen on 2016/12/6.
 */
public class SignInDialogFragment extends DialogFragment {
    private int guide = 0;
    private View mView;
    private Button btnSign;
    private TextView btnSignAfter;
    private LocusView locusView;
    private TextView tvMsg, tvSignAfterCoinNum;
    private RelativeLayout rlSignAfter;
    private OnDismissListener onDismissListener;
    private List<CheckInDayPlan> checkInDayPlanList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.tranparentDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes()
                .height);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        mView = inflater.inflate(R.layout.dialog_fragment_sign_in, null);
        btnSign = (Button) mView.findViewById(R.id.btn_sign);
        btnSignAfter = (TextView) mView.findViewById(R.id.btn_sign_after);
        locusView = (LocusView) mView.findViewById(R.id.lv_locus);
        tvMsg = (TextView) mView.findViewById(R.id.tv_msg);
        tvSignAfterCoinNum = (TextView) mView.findViewById(R.id.tv_gold_coin);
        rlSignAfter = (RelativeLayout) mView.findViewById(R.id.rl_sign_after);
        rlSignAfter.setOnClickListener(v -> signInAfter());
        Typeface typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/happy_font.ttf");
        TextView tvNote = (TextView) mView.findViewById(R.id.tv_note1);
        tvMsg.setTypeface(typeface);
        tvNote.setTypeface(typeface);
        btnSign.setTypeface(typeface);
        btnSignAfter.setTypeface(typeface);
        tvSignAfterCoinNum.setTypeface(typeface);
        getPlanList();
        getUserPortrait();
        return mView;
    }

    /**
     * 获取签到数据
     */
    private void getPlanList() {
        GetPlanListService service = new GetPlanListService(getContext());
        service.setCallback(new IOpenApiDataServiceCallback<GetPlanListResponse>() {
            @Override
            public void onGetData(GetPlanListResponse data) {
                checkInDayPlanList = data.data.checkInDayPlanList;
                refreshUI(data.data);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.postLogined("", false);
    }

    /**
     * 刷新界面
     *
     * @param data
     */
    private void refreshUI(GetPlanListWraper data) {
        int curStep = 0;
        final List<CheckInDayPlan> checkInDayPlanList = data.checkInDayPlanList;
        //获取当前签到进度
        for (int i = 0; i < checkInDayPlanList.size(); i++) {
            if (checkInDayPlanList.get(i).isCheckIn == 0) {
                curStep = i;
                break;
            }
        }
        tvMsg.setText(data.msg);
        tvSignAfterCoinNum.setText(data.remedyCheckInGoldCoinPrice + "");
        locusView.setCurrentStep(curStep);
        locusView.setOnBoxClickListener(new LocusView.OnBoxClickListener() {
            @Override
            public void onBoxClick(int index) {
                BoxDialogFragment fragment = new BoxDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", checkInDayPlanList.get(index));
                fragment.setArguments(bundle);
                fragment.show(getFragmentManager(), "dialog");
            }
        });
        setSignInStatus(data.checkStatus);
    }

    /**
     * 签到
     */
    private void signIn() {
        SignInService service = new SignInService(getContext());
        service.setCallback(new IOpenApiDataServiceCallback<SignInResponse>() {
            @Override
            public void onGetData(SignInResponse data) {
                UIUtils.showToast(getContext(), "签到成功");
                final CheckInDayPlan checkInDayPlanbean = data.data.checkInDayPlan;
                locusView.setOnGiftBoxOpenListener(() -> {
                    BoxGiftDialogFragment fragment = new BoxGiftDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", checkInDayPlanbean);
                    fragment.setArguments(bundle);
                    fragment.show(getFragmentManager(), "dialog");
                });
                locusView.goForward();
                startAwardAnimation(data.data.checkInDayPlan.awardPoint);
                setSignInStatus(data.data.checkStatus);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getContext(), "签到失败：" + errorMsg);
            }
        });
        service.postLogined("", false);
    }

    /**
     * 显示奖励动画
     */
    private void startAwardAnimation(int award) {
        if (award <= 0) {
            return;
        }
        final TextView tvAward = (TextView) mView.findViewById(R.id.tv_award);
        String awardStr = "+" + award + "积分";
        tvAward.setText(awardStr);
        tvAward.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.setDuration(1000);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvAward.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        animationSet.addAnimation(alphaAnimation);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, 1, 0.5f, 1, 1.0f);
        animationSet.addAnimation(scaleAnimation);
        tvAward.startAnimation(animationSet);
    }

    /**
     * 补签到
     */
    private void signInAfter() {
        SignInAfterService service = new SignInAfterService(getContext());
        service.setCallback(new IOpenApiDataServiceCallback<SignInResponse>() {
            @Override
            public void onGetData(final SignInResponse data) {
                UIUtils.showToast(getContext(), "补签成功");
                locusView.setOnGiftBoxOpenListener(() -> {
                    BoxGiftDialogFragment fragment = new BoxGiftDialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", data.data.checkInDayPlan);
                    fragment.setArguments(bundle);
                    fragment.show(getFragmentManager(), "dialog");
                });
                locusView.goForward();
                startAwardAnimation(data.data.checkInDayPlan.awardPoint);
                setSignInStatus(data.data.checkStatus);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getContext(), "补签失败：" + errorMsg);
            }
        });
        service.postLogined("", false);
    }

    /**
     * 设置签到状态
     *
     * @param signInStatus
     */
    private void setSignInStatus(int signInStatus) {
        if (signInStatus == 0) {
            rlSignAfter.setVisibility(View.GONE);
            btnSign.setText("关闭");
            btnSign.setOnClickListener(v -> SignInDialogFragment.this.dismiss());
        } else if (signInStatus == 1) {
            rlSignAfter.setVisibility(View.GONE);
            btnSign.setText("今日签到");
            btnSign.setOnClickListener(v -> {
                signIn();
            });
        } else if (signInStatus == 2) {
            btnSign.setText("关闭");
            rlSignAfter.setVisibility(View.VISIBLE);
            btnSign.setOnClickListener(v -> {
                SignInDialogFragment.this.dismiss();
            });
        }
    }

    /**
     * 获取用户头像bitmap
     */
    private void getUserPortrait() {
        UserDataResponse.UserDataBean.LoginVoBean loginVoBean = BaseApplication.getUserData();
        if (loginVoBean == null) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        final String portraitUrl = loginVoBean.portrait;
        new Thread(() -> {
            Bitmap bitmap = BitmapUtils.getbitmap(portraitUrl);
            Message msg = new Message();
            msg.what = 1001;
            msg.obj = bitmap;
            handler.sendMessage(msg);
        }).start();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1001) {
                try {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    locusView.setUserPortraitBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    });

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }


    public interface OnDismissListener {
        void onDismiss();
    }
}
