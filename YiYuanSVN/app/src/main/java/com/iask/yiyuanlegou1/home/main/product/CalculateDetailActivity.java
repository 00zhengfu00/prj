package com.iask.yiyuanlegou1.home.main.product;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.product.CalDetailResponse;
import com.iask.yiyuanlegou1.network.respose.product.CalculateBean;
import com.iask.yiyuanlegou1.network.service.product.CalDetailService;
import com.iask.yiyuanlegou1.widget.TitleBarView;

import java.util.ArrayList;
import java.util.List;

public class CalculateDetailActivity extends BaseActivity {
    private TitleBarView title;
    private ListView listView;
    private CalculateDetailAdapter adapter;
    private List<CalculateBean> calBeans = new ArrayList<CalculateBean>();

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {
        title = (TitleBarView) findViewById(R.id.title);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalculateDetailActivity.this.finish();
            }
        });
        title.setTitleText(R.string.calculate_detail);
        listView = (ListView) findViewById(R.id.listView);
        View header = View.inflate(this, R.layout.calculate_detail_header, null);
        listView.addHeaderView(header);
        adapter = new CalculateDetailAdapter(calBeans, this);
        listView.setAdapter(adapter);

        int id = getIntent().getIntExtra("id", 0);
        getData(id);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_calculate_detail;
    }

    private void getData(int id) {
        CalDetailService service = new CalDetailService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CalDetailResponse>() {
            @Override
            public void onGetData(final CalDetailResponse data) {
                try {
                    calBeans.clear();
                    calBeans.addAll(data.data.computeDetail.detailList);
                    CalculateDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI();
                            TextView tvCalResult = (TextView) findViewById(R.id.tv_cal_result);
                            TextView tvCode = (TextView) findViewById(R.id.tv_code);
                            StringBuilder builder = new StringBuilder();
                            builder.append("计算结果：\n");
                            builder.append("1：求和：");
                            builder.append(data.data.computeDetail.sum + "");
                            builder.append("（上面100条记录相加之和）\n");
                            long yushu = (data.data.computeDetail.sum % data.data
                                    .computeDetail.totalNum);
                            builder.append("2：取余：" + data.data.computeDetail.sum + "%" + data
                                    .data.computeDetail.totalNum + "=" + yushu + "\n");
                            builder.append("3：结果：" + yushu + "+10000001" + "=");
                            long code = yushu + 10000001;
                            builder.append(code);
                            tvCalResult.setText(builder.toString());
                            tvCode.setText(code + "");
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
        service.post("itemId=" + id, true);
    }

    private void refreshUI() {
        adapter.notifyDataSetChanged();
    }
}
