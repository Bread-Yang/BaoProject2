package com.mdground.yizhida.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.rota.ScheduleHelper;
import com.mdground.yizhida.api.server.global.LoginEmployee;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.db.dao.ScheduleDao;
import com.mdground.yizhida.util.DateUtils;

/**
 * 值班表
 * 
 * @author Vincent
 * 
 */
public class RotaAdapter extends BaseAdapter {

	private SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日", Locale.CHINA);
	private LayoutInflater mInflater;

	ScheduleDao scheduleDao;

	Date today = new Date();
	String strToday;
	Employee loginEmployee;

	protected static class ViewHolder {
		private TextView tvDate;
		private TextView tvWeek;
		private TextView tvToday;
		private TextView tvTime;
	}

	public RotaAdapter(Context context, Employee loginEmployee) {
		this.mInflater = LayoutInflater.from(context);
		scheduleDao = ScheduleDao.getInstance(context);
		strToday = DateUtils.getDateString(today, sdf);
		this.loginEmployee = loginEmployee;
	}

	@Override
	public int getCount() {
		return 7;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_rota_info, null);
			initHolder(viewHolder, convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		bindData(viewHolder, position, convertView);
		return convertView;
	}

	private void bindData(ViewHolder viewHolder, int position, View convertView) {
		Date date = new Date(today.getTime() + position * 24 * 60 * 60 * 1000);

		String strSchedule = "";
		List<Schedule> schedules = null;
		schedules = scheduleDao.getSchedulesByWorkDate(loginEmployee.getEmployeeID(), date);
		strSchedule = ScheduleHelper.mergeSchedulesToStr(schedules);
		if (strSchedule == null || strSchedule.equals("")) {
			strSchedule = "休息";
		}

		if ("休息".equals(strSchedule)) {
			viewHolder.tvTime.setTextSize(26);
		} else {
			viewHolder.tvTime.setTextSize(20);
		}

		viewHolder.tvTime.setText(strSchedule);

		String dateStr = DateUtils.getDateString(date, sdf);
		viewHolder.tvDate.setText(dateStr);
		viewHolder.tvWeek.setText(DateUtils.getWeekDay(date));

		if (strToday.equals(dateStr)) {
			viewHolder.tvToday.setVisibility(View.VISIBLE);
			convertView.setBackgroundResource(R.color.selected_item_bg);
		} else {
			viewHolder.tvToday.setVisibility(View.GONE);
			convertView.setBackgroundResource(R.color.white);
		}
	}

	private void initHolder(ViewHolder viewHolder, View convertView) {
		viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
		viewHolder.tvWeek = (TextView) convertView.findViewById(R.id.tv_weekday);
		viewHolder.tvToday = (TextView) convertView.findViewById(R.id.tv_today);
		viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
	}

}
