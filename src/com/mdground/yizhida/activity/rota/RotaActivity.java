package com.mdground.yizhida.activity.rota;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.adapter.RotaAdapter;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.db.dao.ScheduleDao;
import com.mdground.yizhida.view.TitleBar;

/**
 * 值班表
 * 
 * @author Vincent
 * 
 */
public class RotaActivity extends TitleBarActivity implements RotaView {
	private ListView mListView;
	private RotaAdapter mAdapter;
	private Employee loginEmployee;

	private RotaPresenter presenter;

	@Override
	public int getContentLayout() {
		return R.layout.activity_rota;
	}

	@Override
	public void findView() {
		mListView = (ListView) findViewById(R.id.lv_rota);
	}

	@Override
	public void initView() {
		mListView.setAdapter(mAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ScheduleDao.getInstance(this).deleteAll();
		Date today = new Date();
		// 只请求7天的值班表
		presenter.getEmployeeScheduleList(loginEmployee.getEmployeeID(), today, new Date(today.getTime() + 7 * 24 * 60 * 60 * 1000));
	}

	@Override
	public void initMemberData() {
		mAdapter = new RotaAdapter(this, getMedicalAppliction().getLoginEmployee());
		loginEmployee = ((MedicalAppliction) getApplication()).getLoginEmployee();
		presenter = new RotaPresenterImpl(this);
	}

	@Override
	public void setListener() {

	}

	@Override
	public void onRightClick(View v) {
		Date date = new Date();
		List<Schedule> schedules = ScheduleDao.getInstance(this).getSchedules(getMedicalAppliction().getLoginEmployee().getEmployeeID(), date,
				new Date(date.getTime() + 2 * 24 * 60 * 60 * 1000), 1);
		if (schedules == null || schedules.size() == 0) {
			Toast.makeText(this, "今明两天休息，无需紧急休假", Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent(this, VacationActivity.class);
		startActivity(intent);
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);
		TextView textView = titleBar.inflateView(TitleBar.RIGHT, TextView.class);
		textView.setText("紧急休假");
		textView.setTextColor(Color.WHITE);
		textView.setTextSize(17);
		titleBar.setTitle("值班表");
		titleBar.setBackgroundResource(R.drawable.top_bg1);
	}

	@Override
	public void updateScheduleListView(List<Schedule> schedulesList) {
		ScheduleDao.getInstance(this).deleteAll();
		ScheduleDao.getInstance(this).saveSchedules(schedulesList);
		mAdapter.notifyDataSetChanged();
	}

}
