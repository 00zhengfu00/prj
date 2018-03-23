package cn.cloudwalk.libproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import cn.cloudwalk.libproject.TemplatedActivity;
import cn.cloudwalk.libproject.util.ImgUtil;
import cn.cloudwalk.libproject.util.Util;

public class BankCardResultActivity extends TemplatedActivity {

    private TextView mTvissuer;//发行方
    private TextView mTvnumber;//卡号
    private TextView mTvname;//卡名
    private TextView mTvtype;//卡类型
    ImageView imgView = null;
    //TextView txtScore = null;

    Bitmap mBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cloudwalk_activity_bank_card_result);

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null!=mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
        System.gc();
    }

    /**
     * 布局等初始化
     */
    private void initView() {
        setTitle(getResources().getString(R.string.bank_result));

        mTvissuer = (TextView) findViewById(R.id.bank_issuer);
        mTvnumber = (TextView) findViewById(R.id.card_number);
        mTvname = (TextView) findViewById(R.id.card_name);
        mTvtype = (TextView) findViewById(R.id.card_type);
        imgView = (ImageView) findViewById(R.id.imgView);
        //txtScore = (TextView) findViewById(R.id.txtScore);

        //txtScore.setVisibility(View.GONE);
    }

    private void initData() {
//        String CardNum = getIntent().getStringExtra("CardNum");
//        String BankName = getIntent().getStringExtra("BankName");
//        String CardName = getIntent().getStringExtra("CardName");
//        String CardType = getIntent().getStringExtra("CardType");

//        String spNum = "";
//        int count = 0;
//        for (int i=1;i<=CardNum.length();i++) {
//            char ch = CardNum.charAt(i-1);
//            if (' '==ch)
//                continue;
//            spNum += ch;
//            ++count;
//            if (count % 4 == 0) {
//                spNum += ' ';
//            }
//        }

//        mTvissuer.setText(BankName);
//        mTvnumber.setText(CardNum);
//        mTvname.setText(CardName);
//        mTvtype.setText(CardType);
//        float score = getIntent().getFloatExtra(CloudwalkBankCardOCRActivity.SCORE_KEY, 0);
//        txtScore.setText(String.format("%.2f", score));

        String path = Util.getDiskCacheDir(this) + "/" + "bankcard.jpg";
        mBitmap = ImgUtil.getBitmapByPath(path);
        imgView.setImageBitmap(mBitmap);
    }

    @Override
    public void onLeftClick(View v) {
        super.onLeftClick(v);
    }

}

