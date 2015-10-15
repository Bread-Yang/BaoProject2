package com.mdground.yizhida.activity.rota;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.adapter.VacationAdapter;
import com.mdground.yizhida.adapter.vo.EmployeeSchedule;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.dialog.NotifyDialog;
import com.mdground.yizhida.view.TitleBar;

public class VacationActivity extends TitleBarActivity implements VacationView {
	private NotifyDialog mDialog;
	private List<EmployeeSchedule> schedules = new ArrayList<EmployeeSchedule>();
	private VacationAdapter mAdapter;
	private ListView mList;
	private Employee loginEmployee;

	private VacationPresenter presenter;

	@Override
	public int getContentLayout() {
		return R.layout.activity_vacation;
	}

	@Override
	public void findView() {
		mList = (ListView) findViewById(R.id.lv_vacation);
	}

	@Override
	public void initView() {
		mList.setAdapter(mAdapter);
	}

	@Override
	public void onRightClick(View v) {
		final List<Schedule> scheduleList = getLeaveSchedules();
		if (scheduleList == null || scheduleList.size() == 0) {
			Toast.makeText(VacationActivity.this, "请选择休假时段", Toast.LENGTH_SHORT).show();
			return;
		}

		if (mDialog == null) {
			mDialog = new NotifyDialog(this);
			mDialog.setOnSureClickListener(new NotifyDialog.OnSureClickListener() {

				@Override
				public void onSureClick() {
					// 获取休假时间段
					presenter.SaveDoctorEmergencyLeave(scheduleList);
				}
			});
		}
		mDialog.show();
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);
		TextView textView = titleBar.inflateView(TitleBar.RIGHT, TextView.class);
		textView.setText("确定");
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(17);
		titleBar.setTitle("紧急休假");
		titleBar.setBackgroundResource(R.drawable.top_bg1);
	}

	@Override
	public void initMemberData() {
		loginEmployee = ((MedicalAppliction) getApplication()).getLoginEmployee();
		mAdapter = new VacationAdapter(this, schedules);

		presenter = new VacationPresenterImpl(this);
		// 只请求2天的值班表
		Date today = new Date();
		presenter.getEmployeeScheduleList(loginEmployee.getEmployeeID(), today, new Date(today.getTime() + 24 * 60 * 60 * 1000));
	}

	@Override
	public void setListener() {

	}

	/**
	 * 获取休假时间段
	 * 
	 * @return
	 */
	private List<Schedule> getLeaveSchedules() {
		List<Schedule> schedulesList = new ArrayList<Schedule>();
		for (int i = 0; i < schedules.size(); i++) {
			EmployeeSchedule ems = schedules.get(i);
			List<Schedule> schedules = ems.getSchedules();
			if (schedules == null) {
				continue;
			}
			Date today = new Date();
			for (int j = 0; j < schedules.size(); j++) {
				Schedule s = schedules.get(j);
				Date endDate = new Date(s.getWorkDate().getTime() + s.getEndHour() * 60 * 1000);
				if (endDate.after(today) && s.isSelected() && s.getStatus() != 3) {
					schedulesList.add(s);
				}
			}
		}

		return schedulesList;
	}

	@Override
	public void updateScheduleListView(List<EmployeeSchedule> schedulesList) {
		this.schedules.clear();
		if (schedulesList == null || schedulesList.size() == 0) {
			return;
		}

		this.schedules.addAll(schedulesList);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void finishSaveEmergencyLeave() {
		mDialog.dismiss();
		finish();
	}
}
