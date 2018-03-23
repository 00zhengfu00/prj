 package com.physicmaster.widget;

 import android.content.Context;
 import android.util.AttributeSet;
 import android.view.MotionEvent;
 import android.view.View;
 import android.view.animation.Animation;
 import android.view.animation.RotateAnimation;
 import android.widget.AbsListView;
 import android.widget.AbsListView.OnScrollListener;
 import android.widget.ImageView;
 import android.widget.ListView;
 import android.widget.ProgressBar;
 import android.widget.TextView;

 import com.physicmaster.R;

 import java.text.SimpleDateFormat;
 import java.util.Date;


 /**
 * 下拉刷新ListView
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

	private static final int STATE_PULL_TO_REFRESH = 0;//下拉刷新状态
	private static final int STATE_RELEASE_TO_REFRESH = 1;//松开刷新状态
	private static final int STATE_REFRESHING = 2;//正在刷新状态

	private int mCurrentState = STATE_PULL_TO_REFRESH;//当前状态, 默认下拉刷新

	private View mHeaderView;
	private int mHeaderViewHeight;
	private int startY = -1;

	private ImageView ivArrow;
	private ProgressBar pbLoading;
	private TextView tvStatus;
	private TextView tvTime;

	private RotateAnimation animUp;
	private RotateAnimation animDown;

	private boolean isLoadMore = false;//标记是否正在加载更多

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initHeaderView();
		initFooterView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public PullToRefreshListView(Context context) {
		this(context, null);
	}

	//初始化头布局
	private void initHeaderView() {
		mHeaderView = View.inflate(getContext(),
				R.layout.pull_to_refresh_header, null);
		addHeaderView(mHeaderView);

		ivArrow = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
		pbLoading = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);
		tvStatus = (TextView) mHeaderView.findViewById(R.id.tv_status);
		tvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);

		//隐藏头布局
		//设置负paddingTop, 头布局高度
		//int height = mHeaderView.getHeight();//直接获取的高度为0, 由于界面没有绘制完成
		//System.out.println("头布局高度:" + height);
		mHeaderView.measure(0, 0);//手动测量, 参数表示宽高限制, 没有限制,完全以布局为准, 此时传0就可以
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		//System.out.println("头布局高度:" + measuredHeight);
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

		initAnim();
		setRefreshTime();//设置默认时间
	}

	//初始化脚布局
	private void initFooterView() {
		mFooterView = View.inflate(getContext(),
				R.layout.pull_to_refresh_footer, null);
		addFooterView(mFooterView);

		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);

		//设置滑动监听
		setOnScrollListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//System.out.println("ACTION_DOWN");
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			//System.out.println("ACTION_MOVE");
			//如果按住头条新闻向下滑动, 会导致ACTION_DOWN被ViewPager消费, 此时就无法初始化起点坐标
			//重新初始化起点坐标
			if (startY == -1) {
				startY = (int) ev.getY();
			}

			if (mCurrentState == STATE_REFRESHING) {//如果正在刷新,什么都不做
				break;
			}

			int moveY = (int) ev.getY();

			int dy = moveY - startY;

			int firstVisiblePosition = getFirstVisiblePosition();//当前显示的第一个item位置

			if (dy > 0 && firstVisiblePosition == 0) {
				//下拉并且在listview顶端, 才显示下拉刷新控件
				int paddingTop = -mHeaderViewHeight + dy;//当前paddingTop值

				if (paddingTop > 0 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
					//松开刷新
					mCurrentState = STATE_RELEASE_TO_REFRESH;
					refreshState();
				} else if (paddingTop < 0
						&& mCurrentState != STATE_PULL_TO_REFRESH) {
					//下拉刷新
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshState();
				}

				mHeaderView.setPadding(0, paddingTop, 0, 0);//更新控件位置

				return true;//消费掉事件, 不交给其他控件处理
			}

			break;
		case MotionEvent.ACTION_UP:

			startY = -1;//起点坐标归-1

			if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
				//切换到正在刷新, 完整展示刷新控件
				mCurrentState = STATE_REFRESHING;
				mHeaderView.setPadding(0, 0, 0, 0);

				refreshState();

				if (mListener != null) {
					mListener.onRefresh();//回调刷新状态
				}

			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				//隐藏刷新控件
				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
			}

			break;

		default:
			break;
		}

		return super.onTouchEvent(ev);//不能返回true, 处理下拉刷新情况之外的事件都要交给原生ListView处理
	}

	private void initAnim() {
		//向上旋转
		animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animUp.setDuration(300);
		animUp.setFillAfter(true);//保持住当前状态

		//向下旋转
		animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animDown.setDuration(300);
		animDown.setFillAfter(true);
	}

	//根据当前状态更新界面
	private void refreshState() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			tvStatus.setText("下拉刷新");

			ivArrow.startAnimation(animDown);
			break;
		case STATE_RELEASE_TO_REFRESH:
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			tvStatus.setText("松开刷新");

			ivArrow.startAnimation(animUp);
			break;
		case STATE_REFRESHING:
			pbLoading.setVisibility(View.VISIBLE);
			//必须先结束动画, 然后才能隐藏控件
			ivArrow.clearAnimation();
			ivArrow.setVisibility(View.INVISIBLE);
			tvStatus.setText("正在刷新...");
			break;

		default:
			break;
		}
	}

	//设置刷新时间
	private void setRefreshTime() {
		//MM:如果遇到各位,自动补0; 01,02,12
		//HH: 24小时制; hh:12小时制
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = format.format(new Date());
		tvTime.setText(time);
	}

	private OnRefreshListener mListener;
	private View mFooterView;
	private int mFooterViewHeight;

	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	public interface OnRefreshListener {
		void onRefresh();

		void onLoadMore();//加载更多
	}

	//收起下拉刷新控件
	public void onRefreshComplete() {
		if (!isLoadMore) {//表示正在下拉刷新
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);

			//一切状态归为默认值
			mCurrentState = STATE_PULL_TO_REFRESH;
			pbLoading.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			tvStatus.setText("下拉刷新");

			//更新刷新时间
			setRefreshTime();
		} else {//正在加载更多
			//隐藏脚布局
			mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
			isLoadMore = false;//一定要重新初始化标记
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			int lastVisiblePosition = getLastVisiblePosition();
			if (lastVisiblePosition == getCount() - 1 && !isLoadMore) {
				System.out.println("到底了...");
				//显示脚布局
				mFooterView.setPadding(0, 0, 0, 0);

				//显示到最后一个item上
				setSelection(getCount() - 1);

				//开始加载数据
				isLoadMore = true;

				if (mListener != null) {
					mListener.onLoadMore();//回调通知加载下一页数据
				}
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

}
