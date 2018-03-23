package com.iask.yiyuanlegou1.home.share;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.product.ShareOrderDetailBean;
import com.iask.yiyuanlegou1.network.respose.product.ShareOrderDetailResponse;
import com.iask.yiyuanlegou1.network.service.product.ShareOrderDetailService;
import com.iask.yiyuanlegou1.widget.TitleBarView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ShareDetailActivity extends BaseActivity {
    private TitleBarView title;
    private ShareOrderDetailBean detailBean;

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDetailActivity.this.finish();
            }
        });
        title.setTitleText(R.string.share_detail);
        int sdId = getIntent().getIntExtra("sdId", 0);
        getData(sdId);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_share_detail;
    }

    private void getData(int sdId) {
        ShareOrderDetailService service = new ShareOrderDetailService(this);
        service.setCallback(new IOpenApiDataServiceCallback<ShareOrderDetailResponse>() {
            @Override
            public void onGetData(ShareOrderDetailResponse data) {
                try {
                    detailBean = data.data.orderDetail;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.post("sdId=" + sdId, false);
    }

    private void refreshUI() {
        RoundedImageView imageView = (RoundedImageView) findViewById(R.id.user_head_icon);
        Glide.with(this).load(detailBean.getPhoto()).into(imageView);
        ((TextView) findViewById(R.id.tv_title)).setText(detailBean.getTitle());
        ((TextView) findViewById(R.id.lcode_num)).setText(detailBean.getCode() + "");
        ((TextView) findViewById(R.id.par_count)).setText(detailBean.getPartakeTimes() + "");
        ((TextView) findViewById(R.id.announce_time)).setText(detailBean.getAnnounceTime());
        ((TextView) findViewById(R.id.tv_nickname)).setText(detailBean.getUserName());
        ((TextView) findViewById(R.id.tv_time)).setText(detailBean.getTime());
        ((TextView) findViewById(R.id.tv_comment_title)).setText(detailBean.getOrderTitle());
        ((TextView) findViewById(R.id.tv_comment_content)).setText(detailBean.getContent());
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_pic);
        showSharePic(layout, detailBean.getImage());
    }

    private void showSharePic(LinearLayout layout, List<String> urls) {
        int marginTop = getResources().getDimensionPixelOffset(R.dimen.pic_offset);
        for (String url : urls) {
            ImageView image = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, marginTop, 0, 0);
            image.setLayoutParams(params);
            Glide.with(this).load(url).into(image);
            layout.addView(image);
        }
    }
}
