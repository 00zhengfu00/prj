package com.iask.yiyuanlegou1.home.main.product;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.product.ProductPicResponse;
import com.iask.yiyuanlegou1.network.service.product.ProductDetailPicService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

public class ProductPicDetailActivity extends BaseActivity {
    private WebView webView;
    private TitleBarView title;

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
                ProductPicDetailActivity.this.finish();
            }
        });
        title.setTitleTextStr("图文详情");
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setDefaultTextEncodingName("utf-8 ");
        int id = getIntent().getIntExtra("id", 0);
        getData(id);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_product_pic_detail;
    }

    private void getData(int itemId) {
        ProductDetailPicService service = new ProductDetailPicService(this);
        service.setCallback(new IOpenApiDataServiceCallback<ProductPicResponse>() {
            @Override
            public void onGetData(ProductPicResponse data) {
                try {
                    String content = data.data.itemDetail;
                    webView.loadDataWithBaseURL("",content, "text/html", "utf-8", "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.post("itemId=" + itemId, true);
    }
}
