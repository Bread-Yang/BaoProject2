package com.mdground.yizhida.adapter;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.db.dao.ScheduleDao;

public class ScheduleItemAdapter extends SimpleAdapter<Employee> {

	private Date date;
	private ScheduleDao mScheduleDao;
	java.text.DecimalFormat df = new java.text.DecimalFormat("00");

	protected static class ViewHolder extends BaseViewHolder {
		ImageView imgAdd;
		TextView tvSchedule;
	}

	public ScheduleItemAdapter(Context context) {
		super(context, ViewHolder.class);
		mScheduleDao = ScheduleDao.getInstance(context);
	}

	@Override
	protected void bindData(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, int position, View convertView) {
		ViewHolder viewHolder = (ViewHolder) holder;

		Employee employee = getItem(position);
		if (employee == null) {
			return;
		}
		
		Log.i("HH", employee.toString());

		viewHolder.imgAdd.setVisibility(View.VISIBLE);
		viewHolder.tvSchedule.setVisibility(View.VISIBLE);
		List<Schedule> schedules = mScheduleDao.getSchedules(date, employee.getEmployeeID());

		if (date != null && date.before(new Date(new Date().getTime() + 2 * 24 * 60 * 60 * 1000))) {
			viewHolder.imgAdd.setVisibility(View.GONE);
		}

		if (schedules == null) {
			viewHolder.tvSchedule.setVisibility(View.GONE);
			return;
		}

		StringBuffer sb = new StringBuffer();
		boolean isVacation = true;
		for (int j = 0; j < schedules.size(); j++) {
			Schedule s = schedules.get(j);
			if (s.getStatus() == 3) {// 休假
				continue;
			} else {
				isVacation = false;
				sb.append(df.format(s.getBeginHour() / 60) + ":" + df.format(s.getBeginHour() % 60));
				sb.append("~");
				sb.append(df.format(s.getEndHour() / 60) + ":" + df.format(s.getEndHour() % 60));
				sb.append("\n");
			}

		}

		if (isVacation) {
			viewHolder.tvSchedule.setText("休息");
		} else {
			viewHolder.tvSchedule.setText(sb.toString());
		}

	}

	@Override
	protected void initHolder(com.mdground.yizhida.adapter.SimpleAdapter.BaseViewHolder holder, View convertView) {
		ViewHolder viewHolder = (ViewHolder) holder;
		viewHolder.imgAdd = (ImageView) convertView.findViewById(R.id.img_add);
		viewHolder.tvSchedule = (TextView) convertView.findViewById(R.id.tv_schedule);
	}

	@Override
	protected int getViewResource() {
		return R.layout.item_schedule_body;
	}

	// public List<EmployeeSchedule> getEmployeeSchedules() {
	// return employeeSchedules;
	// }
	//
	// public void setEmployeeSchedules(List<EmployeeSchedule>
	// employeeSchedules) {
	// this.employeeSchedules = employeeSchedules;
	// }

	public void setDate(Date date) {
		this.date = date;
	}

}
