package com.mdground.yizhida.activity.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.adapter.SchedulingAdapter;
import com.mdground.yizhida.adapter.vo.EmployeeSchedule;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.db.dao.EmployeeDao;
import com.mdground.yizhida.db.dao.ScheduleDao;
import com.mdground.yizhida.dialog.NotifyDialog;
import com.mdground.yizhida.util.DateUtils;
import com.mdground.yizhida.view.TimeRangWheelView;
import com.mdground.yizhida.view.TitleBar;

/**
 * 排班
 * 
 * @author Vincent
 * 
 */
public class SchedulingActivity extends TitleBarActivity implements SchedulingView, OnClickListener, OnScrollListener {

	private static final int NULL = 0;
	private static final int VACTION = 1;
	private static final int WORK = 2;

	private ListView mListView;
	private ImageView leftView;
	private ImageView rightView;
	private TextView tvDate;
	private TextView tvWeek;

	private NotifyDialog mNotifyDialog;
	private TimeRangWheelView mTimeRangWheelView;

	private Date currentDate;
	private Date mDate;

	private int mEmployeeId;
	private EmployeeDao mEmployeeDao;
	private ScheduleDao mScheduleDao;

	//
	private List<Employee> employees = new ArrayList<Employee>();
	private List<EmployeeSchedule> employeeScheduleList = new ArrayList<EmployeeSchedule>();

	private SchedulingPresenter presenter;
	private SchedulingAdapter mAdapter;

	private int nCurrentActionId = 0;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日", Locale.CHINA);

	private Handler mHandle = new Handler();

	@Override
	public int getContentLayout() {
		return R.layout.activity_scheduling;
	}

	@Override
	public void findView() {
		mListView = (ListView) findViewById(R.id.lv_scheduling);
		leftView = (ImageView) findViewById(R.id.img_left);
		rightView = (ImageView) findViewById(R.id.img_right);
		tvDate = (TextView) findViewById(R.id.tv_date);
		tvWeek = (TextView) findViewById(R.id.tv_week);
		mTimeRangWheelView = (TimeRangWheelView) findViewById(R.id.layout_content);
	}

	@Override
	public void initView() {
		mListView.setAdapter(mAdapter);
		if (currentDate.after(mDate)) {
			leftView.setVisibility(View.VISIBLE);
		} else {
			leftView.setVisibility(View.GONE);
		}
		tvDate.setText(DateUtils.getDateString(currentDate, sdf));
		tvWeek.setText(DateUtils.getWeekDay(currentDate));
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);
		TextView textView = titleBar.inflateView(TitleBar.RIGHT, TextView.class);
		textView.setText("保存");
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(17);

		titleBar.setTitle("排班");
		titleBar.setBackgroundResource(R.drawable.top_bg1);
	}

	@Override
	public void initMemberData() {
		Intent intent = getIntent();
		long time = intent.getLongExtra(MemberConstant.SCHEDULE_DATE, 0);
		if (time == 0) {
			currentDate = new Date();
		} else {
			currentDate = new Date(time);
		}
		mDate = new Date(new Date().getTime() + 2 * 24 * 60 * 60 * 1000);
		mEmployeeId = intent.getIntExtra(MemberConstant.EMPLOYEE_ID, 0);

		mEmployeeDao = EmployeeDao.getInstance(this);
		mScheduleDao = ScheduleDao.getInstance(this);
		employees = mEmployeeDao.getAll();
		presenter = new SchedulingPresenterImp(this);
		mAdapter = new SchedulingAdapter(this, employeeScheduleList, getMedicalAppliction().getLoginEmployee(), mTimeRangWheelView, mListView);

		getEmployeeSchedules();

		mHandle.postDelayed(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < employees.size(); i++) {
					Employee e = employees.get(i);
					if (e.getEmployeeID() == mEmployeeId) {
						mListView.smoothScrollToPositionFromTop(i, 0);
					}
				}
			}
		}, 1000);
	}

	@Override
	public void setListener() {
		leftView.setOnClickListener(this);
		rightView.setOnClickListener(this);
		mListView.setOnScrollListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void saveEmployeeSchedule() {
		List<Schedule> scheduleList = new ArrayList<Schedule>();
		for (int i = 0; i < employeeScheduleList.size(); i++) {
			EmployeeSchedule employeeSchedule = employeeScheduleList.get(i);
			if (employeeSchedule.isVacation()) {
				scheduleList.add(creatValutionSchedule(employeeSchedule));
				continue;
			}
			List<Schedule> schedules = employeeScheduleList.get(i).getSchedules();
			if (schedules == null) {
				continue;
			}
			scheduleList.addAll(schedules);
		}

		presenter.saveEmployeeScheduleList(currentDate, scheduleList);
	}

	private Schedule creatValutionSchedule(EmployeeSchedule es) {
		Schedule ss = new Schedule();
		ss.setBeginHour(0);
		ss.setEndHour(24 * 60);
		ss.setStatus(3);
		Employee employee = es.getEmployee();
		ss.setClinicId(employee.getClinicID());
		ss.setEmployeeID(employee.getEmployeeID());
		ss.setUpdateTime(new Date());
		ss.setWorkDate(es.getDate());
		return ss;
	}

	@Override
	public void onRightClick(View v) {
		if (mAdapter.isModify()) {
			saveEmployeeSchedule();
		} else {
			setResult(RESULT_OK);
			onBackPressed();
		}
	}

	@Override
	public void onLeftClick(View v) {
		setResult(RESULT_OK);
		super.onLeftClick(v);
	}

	private void getEmployeeSchedules() {
		if (employees != null && employees.size() == 0) {
			return;
		}
		employeeScheduleList.clear();
		for (int i = 0; i < employees.size(); i++) {
			Employee employee = employees.get(i);
			EmployeeSchedule ems = new EmployeeSchedule();
			ems.setEmployee(employee);
			ems.setDate(currentDate);
			List<Schedule> scheduleList = mScheduleDao.getSchedules(currentDate, employee.getEmployeeID());
			if (isVacation(scheduleList) == VACTION) {// 判断是否为休假状态
				ems.setVacation(true);
			} else {
				ems.setVacation(false);
				ems.setSchedules(scheduleList);
			}
			this.employeeScheduleList.add(ems);
		}

		mAdapter.notifyDataSetChanged();
		mAdapter.setModify(false);
	}

	/**
	 * 是否休假, NULL 为无排班数据，VACATION 为休假，WORK为不休假
	 * 
	 * @return
	 */
	private int isVacation(List<Schedule> scheduleList) {
		if (scheduleList == null || scheduleList.size() == 0) {
			return NULL;
		}
		boolean isVacation = true;
		for (int i = 0; i < scheduleList.size(); i++) {
			Schedule s = scheduleList.get(i);
			if (s.getStatus() == 1) {
				isVacation = false;
				break;
			}
		}

		if (isVacation) {
			return VACTION;
		} else {
			return WORK;
		}
	}

	@Override
	public void onClick(View v) {
		nCurrentActionId = v.getId();
		if (mAdapter.isModify()) {
			showNotifyDialog();
		} else {
			changeDate(nCurrentActionId);
		}
	}

	private void showNotifyDialog() {
		if (mNotifyDialog == null) {
			mNotifyDialog = new NotifyDialog(this);
			mNotifyDialog.setOnSureClickListener(new NotifyDialog.OnSureClickListener() {

				@Override
				public void onSureClick() {
					saveEmployeeSchedule();
				}
			});
			mNotifyDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					changeDate(nCurrentActionId);
				}
			});
		}
		mNotifyDialog.show();

		mNotifyDialog.setTitle("温馨提示");
		mNotifyDialog.setMessage("亲，排班已修改，是否保存？");
		mNotifyDialog.setSureMessage("保存");
	}

	private void changeDate(int viewId) {
		Date date = null;
		switch (viewId) {
		case R.id.img_left:
			date = new Date(currentDate.getTime() - 24 * 60 * 60 * 1000);
			if (DateUtils.getDateString(date, sdf).equals(DateUtils.getDateString(mDate, sdf))) {
				leftView.setVisibility(View.GONE);
			}
			currentDate = date;
			tvDate.setText(DateUtils.getDateString(currentDate, sdf));
			tvWeek.setText(DateUtils.getWeekDay(currentDate));
			presenter.getEmployeeSchedules(currentDate, mEmployeeId);
			break;
		case R.id.img_right:
			date = new Date(currentDate.getTime() + 24 * 60 * 60 * 1000);
			currentDate = date;
			tvDate.setText(DateUtils.getDateString(currentDate, sdf));
			tvWeek.setText(DateUtils.getWeekDay(currentDate));
			if (date.after(mDate)) {
				leftView.setVisibility(View.VISIBLE);
			}
			presenter.getEmployeeSchedules(currentDate, mEmployeeId);
			break;

		default:
			break;
		}
		nCurrentActionId = 0;
	}

	@Override
	public void hidProgress() {
		super.hidProgress();
		if (mNotifyDialog != null) {
			mNotifyDialog.dismiss();
		}
	}

	@Override
	public void finishSaveSchedule() {
		this.mTimeRangWheelView.close();
		if (nCurrentActionId == R.id.img_left || nCurrentActionId == R.id.img_right) {
			changeDate(nCurrentActionId);
		} else {
			setResult(RESULT_OK);
			finish();
		}
	}

	@Override
	public void updateScheduleListView(List<Schedule> schedulesList) {
		getEmployeeSchedules();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// 滚动时关闭输入
		// mTimeRangWheelView.close();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}
}
