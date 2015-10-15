package com.mdground.yizhida.activity.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.extras.PullToRefreshFixedTable;
import com.handmark.pulltorefresh.library.extras.fixedheadtable.TableFixHeaders;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.adapter.ScheduleTableAdapter;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.ScheduleDao;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.view.ScheduleHScrollView;
import com.mdground.yizhida.view.TitleBar;

/**
 * 排班表
 * 
 * @author Vincent
 * 
 */
public class ScheduleTableActivity extends TitleBarActivity implements ScheduleTableView, OnRefreshListener2<TableFixHeaders> {

	private static final int PAGE_SIZE = 14;

	private RelativeLayout mHead;
	private LinearLayout mTitleContent;
	private LayoutInflater mInflater;
	private TableFixHeaders mTableFixHeaders;
	private PullToRefreshFixedTable mPullToRefreshFixedTable;
	private ScheduleHScrollView headSrcrollView;
	private TextView tvMounth;

	private ScheduleTableAdapter mTableAdapter;

	private SimpleDateFormat dateDf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);

	private List<Employee> employees = new ArrayList<Employee>();
	private List<Date> dates = new ArrayList<Date>();
	private ScheduleDao mScheduleDao;

	private ScheduleTablePresenter presenter;

	private Handler mHandle = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			presenter.getEmployeeList();
			return false;
		}
	});

	@Override
	public int getContentLayout() {
		return R.layout.activity_schedule_table;
	}

	@Override
	public void findView() {
		mHead = (RelativeLayout) findViewById(R.id.layout_table_head);
		tvMounth = (TextView) mHead.findViewById(R.id.tv_mounth);
		mTitleContent = (LinearLayout) mHead.findViewById(R.id.layout_head_content);
		headSrcrollView = (ScheduleHScrollView) mHead.findViewById(R.id.schedule_hscrollview);
		mPullToRefreshFixedTable = (PullToRefreshFixedTable) findViewById(R.id.table_schedule);
		mPullToRefreshFixedTable.setMode(Mode.BOTH);
		mPullToRefreshFixedTable.setOnRefreshListener(this);
		mTableFixHeaders = mPullToRefreshFixedTable.getRefreshableView();
		mTableFixHeaders.setOnScrollListener(new TableFixHeaders.OnScrollListener() {

			@Override
			public void onScrollChanged(int x, int y) {
				headSrcrollView.smoothScrollTo(x, y);
			}
		});

	}

	@Override
	public void initView() {
		mTableFixHeaders.setAdapter(mTableAdapter);
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);
		TextView textView = titleBar.inflateView(TitleBar.RIGHT, TextView.class);
		textView.setText("今天");
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(17);

		titleBar.setTitle("排班表");
		titleBar.setBackgroundResource(R.drawable.top_bg1);
	}

	@Override
	public void initMemberData() {
		mScheduleDao = ScheduleDao.getInstance(this);
		mScheduleDao.deleteAll();

		mTableAdapter = new ScheduleTableAdapter(this, employees, dates, tvMounth);
		mInflater = LayoutInflater.from(this);

		presenter = new ScheduleTablePresenterImp(this);
	}

	@Override
	public void setListener() {
		mHead.setFocusable(true);
		mHead.setClickable(true);
	}

	@Override
	public void onRightClick(View v) {
		String dateString = DateUtils.getDateString(new Date(), dateDf);
		for (int i = 0; i < dates.size(); i++) {
			if (DateUtils.getDateString(dates.get(i), dateDf).equals(dateString)) {
				mTableFixHeaders.scrollTo(0, i * mTableAdapter.getHeight(1));
				break;
			}
		}
	}

	// 加载更多
	private void nextPageEmployeeSchedules() {
		Date beginDate = null;
		if (dates.size() == 0) {
			beginDate = new Date();
		} else {
			beginDate = new Date(dates.get(dates.size() - 1).getTime() + 24 * 60 * 60 * 1000);// 获取最后一个日期加一天
		}

		for (int i = 0; i < PAGE_SIZE; i++) {
			dates.add(new Date(beginDate.getTime() + i * 24 * 60 * 60 * 1000));
		}
		presenter.getEmployeeScheduleList(beginDate, PAGE_SIZE);
	}

	// 向上加载
	private void prePageEmployeeSchedules() {
		Date beginDate = null;
		if (dates.size() == 0) {
			beginDate = new Date();
		} else {
			beginDate = new Date(dates.get(0).getTime() - PAGE_SIZE * 24 * 60 * 60 * 1000);// 获取最后一个日期加一天
		}

		for (int i = PAGE_SIZE - 1; i >= 0; i--) {
			dates.add(0, new Date(beginDate.getTime() + i * 24 * 60 * 60 * 1000));
		}
		presenter.getEmployeeScheduleList(beginDate, PAGE_SIZE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		presenter.getEmployeeList();
	}

	public void initTitleView(List<Employee> employees) {
		if (employees == null || employees.size() == 0) {
			return;
		}
		mTitleContent.removeAllViews();
		for (int i = 0; i < employees.size(); i++) {
			TextView tvName = (TextView) mInflater.inflate(R.layout.item_schedule_name, mTitleContent, false);
			tvName.setText(employees.get(i).getEmployeeName());
			mTitleContent.addView(tvName);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<TableFixHeaders> refreshView) {
		prePageEmployeeSchedules();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<TableFixHeaders> refreshView) {
		nextPageEmployeeSchedules();
	}

	@Override
	public void updateEmployees(List<Employee> employees) {
		this.employees.clear();
		initTitleView(employees);
		this.employees.addAll(employees);
		nextPageEmployeeSchedules();
	}

	@Override
	public void updateSchedules(List<Schedule> schedules) {
		mTableAdapter.notifyDataSetChanged();
	}

	@Override
	public void refreshComplete() {
		mPullToRefreshFixedTable.onRefreshComplete();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == MemberConstant.SCHEDULE_REQUESTCODE && resultCode == RESULT_OK) {
			dates.clear();
			mScheduleDao.deleteAll();
			mHandle.sendEmptyMessage(0);
		}
	}
}
