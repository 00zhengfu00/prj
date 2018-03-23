package com.physicmaster.modules.study.fragment.section;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.physicmaster.R;
import com.physicmaster.base.BaseFragment;
import com.physicmaster.modules.study.activity.exercise.ExcerciseActivity;
import com.physicmaster.modules.study.activity.exercise.OnAnswerSubmmit;
import com.physicmaster.modules.study.activity.exercise.OnResultSubmmit;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.excercise.AddMemoResponse;
import com.physicmaster.net.response.excercise.GetExerciseResponse;
import com.physicmaster.net.service.excercise.AddMemoService;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.OptionWebview;
import com.physicmaster.widget.ProgressLoadingDialog;

import java.util.List;

/**
 * Created by songrui on 16/11/29.
 */

public class ExcerciseFragment extends BaseFragment implements View.OnClickListener {

    private OptionWebview tvContent;
    private RelativeLayout rlOptionsA;
    private OptionWebview tvOptionsA;
    private ImageView ivRightA;
    private ImageView ivWrongA;
    private RelativeLayout rlOptionsB;
    private OptionWebview tvOptionsB;
    private ImageView ivRightB;
    private ImageView ivWrongB;
    private RelativeLayout rlOptionsC;
    private OptionWebview tvOptionsC;
    private ImageView ivRightC;
    private ImageView ivWrongC;
    private RelativeLayout rlOptionsD;
    private OptionWebview tvOptionsD;
    private ImageView ivRightD;
    private ImageView ivWrongD;
    private OptionWebview tvResolve;
    private Button btnResolve;
    private Button btnSubmit;

    private int isSelected;
    private int rightSelected;
    private boolean lastExcerciseRight = false;
    private String selecterAnswer;
    private int questionid;
    private ExcerciseActivity mContext;

    private OnAnswerSubmmit onAnswerSubmmitListener;
    private OnResultSubmmit onResultSubmmitListener;

    private int index;
    private int exercise_size;
    private Button btnAddMemo;
    private Button btnPerish;
    private Bundle bundle;

    public OnAnswerSubmmit getOnAnswerSubmmitListener() {
        return onAnswerSubmmitListener;
    }

    public void setOnAnswerSubmmitListener(OnAnswerSubmmit onAnswerSubmmitListener) {
        this.onAnswerSubmmitListener = onAnswerSubmmitListener;
    }

    public void setOnResultSubmmitListener(OnResultSubmmit onResultSubmmitListener) {
        this.onResultSubmmitListener = onResultSubmmitListener;
    }

    @Override
    protected void initView() {

        mContext = (ExcerciseActivity) getActivity();


        tvContent = (OptionWebview) rootView.findViewById(R.id.tv_content);

        rlOptionsA = (RelativeLayout) rootView.findViewById(R.id.rl_options_A);
        tvOptionsA = (OptionWebview) rootView.findViewById(R.id.tv_options_A);
        ivRightA = (ImageView) rootView.findViewById(R.id.iv_right_A);
        ivWrongA = (ImageView) rootView.findViewById(R.id.iv_wrong_A);

        rlOptionsB = (RelativeLayout) rootView.findViewById(R.id.rl_options_B);
        tvOptionsB = (OptionWebview) rootView.findViewById(R.id.tv_options_B);
        ivRightB = (ImageView) rootView.findViewById(R.id.iv_right_B);
        ivWrongB = (ImageView) rootView.findViewById(R.id.iv_wrong_B);

        rlOptionsC = (RelativeLayout) rootView.findViewById(R.id.rl_options_C);
        tvOptionsC = (OptionWebview) rootView.findViewById(R.id.tv_options_C);
        ivRightC = (ImageView) rootView.findViewById(R.id.iv_right_C);
        ivWrongC = (ImageView) rootView.findViewById(R.id.iv_wrong_C);

        rlOptionsD = (RelativeLayout) rootView.findViewById(R.id.rl_options_D);
        tvOptionsD = (OptionWebview) rootView.findViewById(R.id.tv_options_D);
        ivRightD = (ImageView) rootView.findViewById(R.id.iv_right_D);
        ivWrongD = (ImageView) rootView.findViewById(R.id.iv_wrong_D);

        tvResolve = (OptionWebview) rootView.findViewById(R.id.tv_resolve);
        btnResolve = (Button) rootView.findViewById(R.id.btn_resolve);
        btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);
        btnPerish = (Button) rootView.findViewById(R.id.btn_perish);
        btnAddMemo = (Button) rootView.findViewById(R.id.btn_add_memo);

        tvContent.getSettings().setJavaScriptEnabled(true);
        tvOptionsA.getSettings().setJavaScriptEnabled(true);
        tvOptionsB.getSettings().setJavaScriptEnabled(true);
        tvOptionsC.getSettings().setJavaScriptEnabled(true);
        tvOptionsD.getSettings().setJavaScriptEnabled(true);
        tvResolve.getSettings().setJavaScriptEnabled(true);

        tvContent.setWebChromeClient(new WebChromeClient());
        tvOptionsA.setWebChromeClient(new WebChromeClient());
        tvOptionsB.setWebChromeClient(new WebChromeClient());
        tvOptionsC.setWebChromeClient(new WebChromeClient());
        tvOptionsD.setWebChromeClient(new WebChromeClient());
        tvResolve.setWebChromeClient(new WebChromeClient());

        rlOptionsA.setOnClickListener(this);
        rlOptionsB.setOnClickListener(this);
        rlOptionsC.setOnClickListener(this);
        rlOptionsD.setOnClickListener(this);

        btnAddMemo.setOnClickListener(this);


        setdata();

    }

    private void setdata() {
        Bundle bundle = getArguments();
        GetExerciseResponse.DataBean.QuestionListBean exercise = (GetExerciseResponse.DataBean
                .QuestionListBean) bundle.getSerializable("exercise");
        index = bundle.getInt("index", 0);
        exercise_size = bundle.getInt("exercise_size", 0);
        lastExcerciseRight = bundle.getBoolean("last_excercise_right", false);
        questionid = exercise.quId;

        tvResolve.setVisibility(View.GONE);
        fillWebview(exercise);
        if ("A".equals(exercise.answer)) {
            rightSelected = 1;
        } else if ("B".equals(exercise.answer)) {
            rightSelected = 2;
        } else if ("C".equals(exercise.answer)) {
            rightSelected = 3;
        } else if ("D".equals(exercise.answer)) {
            rightSelected = 4;
        }
    }


    /**
     * 填充webview数据
     */
    private void fillWebview(GetExerciseResponse.DataBean.QuestionListBean exercise) {
        String titleTemp = HtmlTemplate.TITLE_TEMPLATE.replace("###REPLACEMENT###", exercise
                .content);
        tvContent.loadDataWithBaseURL("file:///android_asset/", titleTemp, "text/html", "UTF-8",
                "");

        List<GetExerciseResponse.DataBean.QuestionListBean.ItemListBean> options = exercise
                .itemList;
        if (options == null || options.size() == 0) {
            return;
        }
        String optionA = HtmlTemplate.ITEM_TEMPLATE.replace("###REPLACEMENT###", options.get
                (0).questionItem);
        if (options.size() == 1) {
            return;
        }
        tvOptionsA.loadDataWithBaseURL("file:///android_asset/", optionA, "text/html", "UTF-8", "");
        String optionB = HtmlTemplate.ITEM_TEMPLATE.replace("###REPLACEMENT###", options.get
                (1).questionItem);
        if (options.size() == 2) {
            return;
        }
        tvOptionsB.loadDataWithBaseURL("file:///android_asset/", optionB, "text/html", "UTF-8", "");
        String optionC = HtmlTemplate.ITEM_TEMPLATE.replace("###REPLACEMENT###", options.get
                (2).questionItem);
        if (options.size() == 3) {
            return;
        }
        tvOptionsC.loadDataWithBaseURL("file:///android_asset/", optionC, "text/html", "UTF-8", "");
        String optionD = HtmlTemplate.ITEM_TEMPLATE.replace("###REPLACEMENT###", options.get
                (3).questionItem);
        tvOptionsD.loadDataWithBaseURL("file:///android_asset/", optionD, "text/html", "UTF-8", "");

        String questionResolve = HtmlTemplate.ANALYSIS_TEMPLATE.replace("###REPLACEMENT###",
                exercise.answerExplain);
        tvResolve.loadDataWithBaseURL("file:///android_asset/", questionResolve, "text/html",
                "UTF-8", "");
//        tvOptionsA.setText(exercise.itemList.get(0).questionItem);
//        tvOptionsB.setText(exercise.itemList.get(1).questionItem);
//        tvOptionsC.setText(exercise.itemList.get(2).questionItem);
//        tvOptionsD.setText(exercise.itemList.get(3).questionItem);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_excercise;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_options_A:
                rlOptionsA.setSelected(true);
                rlOptionsB.setSelected(false);
                rlOptionsC.setSelected(false);
                rlOptionsD.setSelected(false);
                isSelected = 1;
                clickSubmit();
                break;
            case R.id.rl_options_B:
                rlOptionsA.setSelected(false);
                rlOptionsB.setSelected(true);
                rlOptionsC.setSelected(false);
                rlOptionsD.setSelected(false);
                isSelected = 2;
                clickSubmit();
                break;
            case R.id.rl_options_C:
                rlOptionsA.setSelected(false);
                rlOptionsB.setSelected(false);
                rlOptionsC.setSelected(true);
                rlOptionsD.setSelected(false);
                isSelected = 3;
                clickSubmit();
                break;
            case R.id.rl_options_D:
                rlOptionsA.setSelected(false);
                rlOptionsB.setSelected(false);
                rlOptionsC.setSelected(false);
                rlOptionsD.setSelected(true);
                isSelected = 4;
                clickSubmit();
                break;
            case R.id.btn_submit:
                click();
                break;
            case R.id.btn_add_memo:
                addMemo();
                break;

        }
    }

    private void addMemo() {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(mContext);

        final AddMemoService service = new AddMemoService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<AddMemoResponse>() {

            @Override
            public void onGetData(AddMemoResponse data) {
                loadingDialog.dismissDialog();
                UIUtils.showToast(mContext, "加入成功");
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(mContext, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("quId=" + questionid, false);
    }

    private void clickSubmit() {

        if (isSelected == 1 || isSelected == 2 || isSelected == 3 || isSelected == 4) {
            btnSubmit.setBackgroundResource(R.color.colorTitleBlue);
            btnSubmit.setOnClickListener(this);
        }
    }

    public void click() {
        rlOptionsA.setEnabled(false);
        rlOptionsB.setEnabled(false);
        rlOptionsC.setEnabled(false);
        rlOptionsD.setEnabled(false);

        btnSubmit.setVisibility(View.GONE);
        btnResolve.setVisibility(View.VISIBLE);


        btnAddMemo.setEnabled(true);
        btnAddMemo.setVisibility(View.GONE);

        if (isSelected == rightSelected) {
            onResultSubmmitListener.submmitResult(true);
            if (isSelected == 1) {
                ivRightA.setVisibility(View.VISIBLE);

            } else if (isSelected == 2) {
                ivRightB.setVisibility(View.VISIBLE);

            } else if (isSelected == 3) {
                ivRightC.setVisibility(View.VISIBLE);

            } else if (isSelected == 4) {
                ivRightD.setVisibility(View.VISIBLE);

            }
        } else {
            onResultSubmmitListener.submmitResult(false);
            if (isSelected == 1) {
                ivWrongA.setVisibility(View.VISIBLE);

            } else if (isSelected == 2) {
                ivWrongB.setVisibility(View.VISIBLE);

            } else if (isSelected == 3) {
                ivWrongC.setVisibility(View.VISIBLE);

            } else if (isSelected == 4) {
                ivWrongD.setVisibility(View.VISIBLE);

            }
            if (rightSelected == 1) {
                ivRightA.setVisibility(View.VISIBLE);

            } else if (rightSelected == 2) {
                ivRightB.setVisibility(View.VISIBLE);

            } else if (rightSelected == 3) {
                ivRightC.setVisibility(View.VISIBLE);

            } else if (rightSelected == 4) {
                ivRightD.setVisibility(View.VISIBLE);

            }
            tvResolve.setVisibility(View.VISIBLE);
        }

        btnResolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelected == 1) {
                    selecterAnswer = "A";
                } else if (isSelected == 2) {
                    selecterAnswer = "B";
                } else if (isSelected == 3) {
                    selecterAnswer = "C";
                } else if (isSelected == 4) {
                    selecterAnswer = "D";
                }
                boolean right = false;
                if (isSelected == rightSelected) {
                    right = true;
                }
                onAnswerSubmmitListener.submmit(questionid, selecterAnswer);
                btnResolve.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (btnResolve != null) {
                            btnResolve.setEnabled(true);
                        }
                    }
                }, 2000);
            }
        });
    }
}
