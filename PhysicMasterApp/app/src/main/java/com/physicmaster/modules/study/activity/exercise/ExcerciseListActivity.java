package com.physicmaster.modules.study.activity.exercise;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse.DataBean.ChapterStudyBean.ExcerciseVideoBean;
import com.physicmaster.widget.TitleBuilder;

import java.util.List;

public class ExcerciseListActivity extends BaseActivity {
    private ListView lvExcercise;
    private List<ExcerciseVideoBean> list;
    private String chapterId;
    private ExcerciseAdapter adapter;

    @Override
    protected void findViewById() {
        lvExcercise = (ListView) findViewById(R.id.lv_excercise);
    }

    private void initTitle() {
        String title = getIntent().getStringExtra("title");
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText(title);
    }

    @Override
    protected void initView() {
        initTitle();
        chapterId = getIntent().getStringExtra("chapterId");
        String title = getIntent().getStringExtra("title");
        list = (List<ExcerciseVideoBean>) getIntent().getSerializableExtra("excerciseList");
        if (null == list || 0 == list.size()) {
            return;
        }
//        TextView tvTitle = new TextView(this);
//        tvTitle.setText(title);
//        tvTitle.setTextSize(18);
//        tvTitle.setTextColor(0xff333333);
//        tvTitle.setPadding((int) (15 * BaseApplication.getDensity()), (int) (10 * BaseApplication.getDensity()), 0, (int) (20 * BaseApplication.getDensity()));
//        lvExcercise.addHeaderView(tvTitle);
        adapter = new ExcerciseAdapter(this);
        lvExcercise.setAdapter(adapter);

        lvExcercise.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExcerciseListActivity.this, ExcerciseDetailActivity.class);
                intent.putExtra("videoId", list.get(position).videoId);
                intent.putExtra("chapterId", chapterId);
                intent.putExtra("title", list.get(position).title);
                intent.putExtra("position", position);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_base_excercise;
    }


    class ExcerciseAdapter extends BaseAdapter {
        private Context mContext;

        public ExcerciseAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.excercise_item,
                        parent, false);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_title);
            holder.ivFinish = (ImageView) convertView.findViewById(R.id.iv_finish);
            ExcerciseVideoBean videoBean = list.get(position);
            holder.tvName.setText(videoBean.title);
            if (videoBean.isWatch == 1) {
                holder.ivFinish.setVisibility(View.VISIBLE);
            } else {
                holder.ivFinish.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView tvName;
            ImageView ivFinish;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        int position = data.getIntExtra("position", -1);
        if (position != -1) {
            list.get(position).isWatch = 1;
            adapter.notifyDataSetChanged();
        }
    }
}
