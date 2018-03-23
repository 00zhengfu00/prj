package com.physicmaster.modules.mine.fragment.topicmap;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.physicmaster.R;
import com.physicmaster.base.BaseFragment;
import com.physicmaster.modules.study.activity.exercise.OnAnswerSubmmit;
import com.physicmaster.modules.study.fragment.section.HtmlTemplate;
import com.physicmaster.net.response.excercise.GetTopicmapAnalysisResponse;
import com.physicmaster.widget.OptionWebview;

import java.util.List;

/**
 * Created by huashigen on 16/11/29.
 */

public class TopicmapAnalysisFragment extends BaseFragment {

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
    private Button btnSubmit;

    private int isSelected;

    private OnAnswerSubmmit onAnswerSubmmitListener;

    private int index;
    private int exercise_size;
    private Bundle bundle;

    public OnAnswerSubmmit getOnAnswerSubmmitListener() {
        return onAnswerSubmmitListener;
    }

    public void setOnAnswerSubmmitListener(OnAnswerSubmmit onAnswerSubmmitListener) {
        this.onAnswerSubmmitListener = onAnswerSubmmitListener;
    }

    @Override
    protected void initView() {

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
        btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);

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

        setdata();
    }

    private void setdata() {
        Bundle bundle = getArguments();
        GetTopicmapAnalysisResponse.DataBean.QuestionWrongListBean exercise = (GetTopicmapAnalysisResponse.DataBean.QuestionWrongListBean) bundle.getSerializable("exercise");
        index = bundle.getInt("index", 0);
        exercise_size = bundle.getInt("exercise_size", 0);

        fillWebview(exercise);
        if ("A".equals(exercise.answer)) {
            ivRightA.setVisibility(View.VISIBLE);
        } else if ("B".equals(exercise.answer)) {
            ivRightB.setVisibility(View.VISIBLE);
        } else if ("C".equals(exercise.answer)) {
            ivRightC.setVisibility(View.VISIBLE);
        } else if ("D".equals(exercise.answer)) {
            ivRightD.setVisibility(View.VISIBLE);
        }

        if ("A".equals(exercise.wrongAnswer)) {
            rlOptionsA.setSelected(true);
            ivWrongA.setVisibility(View.VISIBLE);
        } else if ("B".equals(exercise.wrongAnswer)) {
            rlOptionsB.setSelected(true);
            ivWrongB.setVisibility(View.VISIBLE);
        } else if ("C".equals(exercise.wrongAnswer)) {
            rlOptionsC.setSelected(true);
            ivWrongC.setVisibility(View.VISIBLE);
        } else if ("D".equals(exercise.wrongAnswer)) {
            rlOptionsD.setSelected(true);
            ivWrongD.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 填充webview数据
     */
    private void fillWebview(GetTopicmapAnalysisResponse.DataBean.QuestionWrongListBean exercise) {
        String titleTemp = HtmlTemplate.TITLE_TEMPLATE.replace("###REPLACEMENT###", exercise
                .content);
        tvContent.loadDataWithBaseURL("file:///android_asset/", titleTemp, "text/html", "UTF-8",
                "");
        List<GetTopicmapAnalysisResponse.DataBean.QuestionWrongListBean.ItemListBean> options = exercise
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
        return R.layout.fragment_analysis;
    }

}
