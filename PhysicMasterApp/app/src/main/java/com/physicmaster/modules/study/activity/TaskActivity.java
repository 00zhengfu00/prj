package com.physicmaster.modules.study.activity;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.response.game.GetAwardResponse;
import com.physicmaster.net.response.game.GetTaskResponse;
import com.physicmaster.net.response.game.TaskBean;
import com.physicmaster.net.service.game.GetAwardService;
import com.physicmaster.net.service.game.GetTaskService;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.physicmaster.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskActivity extends BaseActivity {


    private static final String TAG = "TaskActivity";

    private static final int TYPE_TITLE  = 1; //标题布局
    private static final int TYPE_NORMAL = 0; //普通布局
    private ListView    mLvTask;
    private TaskAdapter mTaskAdapter;
    private Map<String, Integer> map = new HashMap<String, Integer>();


    private List<TaskBean> mEveryDayList;     //每日任务集合
    private List<TaskBean> mAdvanceList;//进阶任务集合
    private List<TaskBean> mNewComerList;//新手集合
    private SoundPool      soundPool;
    private boolean        isSoundSwitch;
    private RelativeLayout rlEmpty;

    @Override
    protected void findViewById() {
        mLvTask = (ListView) findViewById(R.id.lv_tasks);
        rlEmpty = (RelativeLayout) findViewById(R.id.rl_empty);

        initTitle();
    }

    private void initTitle() {
        /**
         * 1.设置左边的图片按钮显示，以及事件
         * 2.设置中间TextView显示的文字
         */
        new TitleBuilder(this).setLeftImageRes(R.mipmap.fanhui).setLeftText("返回")
                .setLeftTextOrImageListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                })
                .setMiddleTitleText("任务");
    }

    @Override
    protected void initView() {

        showTask();
        isSoundSwitch = SpUtils.getBoolean(this, "isSoundSwitch", true);
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        //第三个参数还没有开启作用
        map.put("buttonClick", soundPool.load(this, R.raw.answerright, 1));

    }

    private void showTask() {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final GetTaskService service = new GetTaskService(TaskActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GetTaskResponse>() {


            @Override
            public void onGetData(GetTaskResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                loadingDialog.dismissDialog();
                mNewComerList = data.data.newcomerList;
                mAdvanceList = data.data.advancedList;
                mEveryDayList = data.data.dailyList;


                if (mNewComerList == null && mAdvanceList == null && mEveryDayList == null) {
                    mLvTask.setVisibility(View.GONE);
                    rlEmpty.setVisibility(View.VISIBLE);
                } else {
                    mLvTask.setVisibility(View.VISIBLE);
                    rlEmpty.setVisibility(View.GONE);
                    if (mNewComerList == null) {
                        mNewComerList = new ArrayList<>();
                    }
                    if (mAdvanceList == null) {
                        mAdvanceList = new ArrayList<>();
                    }
                    if (mEveryDayList == null) {
                        mEveryDayList = new ArrayList<>();
                    }

                    mTaskAdapter = new TaskAdapter();
                    mLvTask.setAdapter(mTaskAdapter);
                }


            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(TaskActivity.this, errorMsg);
                mLvTask.setVisibility(View.GONE);
                rlEmpty.setVisibility(View.VISIBLE);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("", false);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_task;
    }

    class TaskAdapter extends BaseAdapter {

        private TaskBean item;

        @Override
        public int getCount() {
            return mEveryDayList.size() + mAdvanceList.size() + mNewComerList.size() + 3;//增加三个标题栏
        }

        @Override
        public TaskBean getItem(int position) {
            if (position == 0 || position == mNewComerList.size() + 1 || position == mNewComerList.size() + mEveryDayList.size() + 2) {
                //标题栏
                return null;
            }

            if (position < mNewComerList.size() + 1) {
                //新手集合
                return mNewComerList.get(position - 1);//减掉一个标题栏占位
            } else if (position < mNewComerList.size() + mEveryDayList.size() + 2) {
                //每日任务集合
                return mEveryDayList.get(position - mNewComerList.size() - 2);//减掉两个标题栏和新手任务
            } else {
                //进阶任务集合
                return mAdvanceList.get(position - mNewComerList.size() - mEveryDayList.size() - 3);//减掉三个标题栏和新手任务,每日任务
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //返回布局类型个数
        @Override
        public int getViewTypeCount() {
            return 2;
        }

        //根据位置返回当前位置的布局类型
        @Override
        public int getItemViewType(int position) {
            if (position == 0 || position == mNewComerList.size() + 1 || position == mNewComerList.size() + mEveryDayList.size() + 2) {
                //标题栏
                return TYPE_TITLE;
            } else {
                return TYPE_NORMAL;
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //根据当前布局类型,来加载不同布局
            int type = getItemViewType(position);

            switch (type) {
                case TYPE_NORMAL:
                    //普通布局
                    ViewHolder holder;
                    if (convertView == null) {
                        convertView = View.inflate(TaskActivity.this, R.layout.list_item_task, null);
                        holder = new ViewHolder();
                        holder.tvTasks = (TextView) convertView
                                .findViewById(R.id.tv_tasks);
                        holder.tvGetGold = (TextView) convertView
                                .findViewById(R.id.tv_get_gold);
                        holder.tvFinish = (TextView) convertView
                                .findViewById(R.id.tv_finish);
                        holder.btnTask = (Button) convertView
                                .findViewById(R.id.btn_task);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    final TaskBean item = getItem(position);

                    String str = "<font color='#ffa14c'>" + item.completeCount + "</font><font color='#a7a7a7'>/" + item.targetCount + "</font>";
                    if (item.targetCount <= item.completeCount) {
                        holder.tvFinish.setVisibility(View.GONE);
                        holder.btnTask.setVisibility(View.VISIBLE);
                        if (item.isAward == 0) {
                            holder.btnTask.setEnabled(true);
                            holder.btnTask.setText("领取奖励");
                            holder.btnTask.setBackgroundResource(R.drawable.blue_btn_background);
                            holder.btnTask.setTextColor(getResources().getColor(R.color.colorWhite));
                        } else {
                            holder.btnTask.setEnabled(false);
                            holder.btnTask.setText("已领取");
                            holder.btnTask.setBackgroundColor(Color.TRANSPARENT);
                            holder.btnTask.setTextColor(getResources().getColor(R.color.colorGray));
                        }
                    } else if (item.targetCount > item.completeCount) {
                        holder.btnTask.setVisibility(View.GONE);
                        holder.tvFinish.setVisibility(View.VISIBLE);
                        holder.tvFinish.setText(Html.fromHtml(str));
                    }
                    holder.tvTasks.setText(item.missionMame + "");

                    if (item.awardGoldCoin == 0 && item.awardPropCount == 0) {
                        holder.tvGetGold.setText("积分+" + item.awardPoint);
                    } else {
                        if (item.awardGoldCoin != 0) {
                            holder.tvGetGold.setText("积分+" + item.awardPoint + " 金币+" + item.awardGoldCoin);
                        }
                        if (item.awardPropCount != 0) {
                            holder.tvGetGold.setText("积分+" + item.awardPoint + " 道具+" + item.awardPropCount);
                        }

                        if (item.awardPropCount != 0 && item.awardGoldCoin != 0) {
                            holder.tvGetGold.setText("积分+" + item.awardPoint + " 金币+" + item.awardGoldCoin + " 道具+" + item.awardPropCount);
                        }

                    }

                    holder.btnTask.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int mPosition = 0;
                            if (position < mNewComerList.size() + 1) {
                                //新手集合
                                mPosition = position - 1;
                                getAward(item.userMissionId, item.missionType, mPosition);
                            } else if (position < mNewComerList.size() + mEveryDayList.size() + 2) {
                                //每日任务集合
                                mPosition = position - mNewComerList.size() - 2;
                                getAward(item.userMissionId, item.missionType, mPosition);
                            } else {
                                //进阶任务集合
                                mPosition = position - mNewComerList.size() - mEveryDayList.size() - 3;
                                getAward(item.userMissionId, item.missionType, mPosition);
                            }
                            if (isSoundSwitch) {
                                soundPool.play(map.get("buttonClick"), 1.0f, 1.0f, 1, 0, 1);
                            }
                        }
                    });

                    break;
                case TYPE_TITLE:
                    //标题栏布局
                    HeaderViewHolder headerHolder;
                    if (convertView == null) {
                        convertView = View.inflate(TaskActivity.this,
                                R.layout.list_item_title, null);
                        headerHolder = new HeaderViewHolder();
                        headerHolder.tvTitle = (TextView) convertView
                                .findViewById(R.id.tv_title);

                        convertView.setTag(headerHolder);
                    } else {
                        headerHolder = (HeaderViewHolder) convertView.getTag();
                    }

                    if (position == 0) {
                        if (mNewComerList.size() == 0) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        } else {
                            //新手任务
                            headerHolder.tvTitle.setText("新手任务");
                        }
                    } else if (position == mNewComerList.size() + 1) {
                        if (mEveryDayList.size() == 0) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        } else {
                            //每日任务
                            headerHolder.tvTitle.setText("每日任务");
                        }

                    } else {
                        if (mAdvanceList.size() == 0) {
                            headerHolder.tvTitle.setVisibility(View.GONE);
                        } else {
                            //进阶任务
                            headerHolder.tvTitle.setText("进阶任务");
                        }

                    }
                    break;
            }
            return convertView;
        }
    }


    private void getAward(int userMissionId, final int missionType, final int position) {
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);

        final GetAwardService service = new GetAwardService(TaskActivity.this);
        service.setCallback(new IOpenApiDataServiceCallback<GetAwardResponse>() {

            @Override
            public void onGetData(GetAwardResponse data) {
                Log.d(TAG, "获取成功：onGetData: " + data.msg);
                loadingDialog.dismissDialog();
                UIUtils.showToast(TaskActivity.this, "领取成功");
                if (missionType == 1) {
                    mNewComerList.get(position).isAward = 1;
                } else if (missionType == 2) {
                    mEveryDayList.get(position).isAward = 1;
                } else {
                    mAdvanceList.get(position).isAward = 1;
                }
                mTaskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Log.e(TAG, "获取失败：onGetData: " + errorMsg.toString());
                UIUtils.showToast(TaskActivity.this, errorMsg);
                loadingDialog.dismissDialog();

            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postLogined("userMissionId=" + userMissionId + "&missionType=" + missionType, false);
    }

    static class ViewHolder {
        public TextView tvTasks;
        public TextView tvGetGold;
        public TextView tvFinish;
        public Button   btnTask;
    }

    static class HeaderViewHolder {
        public TextView tvTitle;
    }

}
