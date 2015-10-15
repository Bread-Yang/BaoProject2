package com.mdground.yizhida.db.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;

import com.mdground.yizhida.bean.Schedule;
import com.mdground.yizhida.db.dao.inter.IScheduleDao;
import com.mdground.yizhida.util.DateUtils;

/**
 * 排班数据
 * 
 * @author qinglong.huang
 * 
 */
public class ScheduleDao extends BaseDao<Schedule> implements IScheduleDao {

	private static ScheduleDao instance = null;

	protected ScheduleDao(Context context) {
		super(context);
	}

	public static ScheduleDao getInstance(Context context) {
		if (instance == null) {
			instance = new ScheduleDao(context);
		}

		return instance;
	}

	@Override
	public List<Schedule> getSchedules(Date workDate, int employeeId) {
		return getDb().findAllByWhere(
				Schedule.class,
				"EmployeeID = " + employeeId + " and workDate like '"
						+ DateUtils.getDateString(workDate, new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) + "%'");
	}

	/**
	 * 获取未休假的
	 * 
	 * @return
	 */
	public List<Schedule> getWorkSchedules(Date workDate, int employeeId) {
		return getDb().findAllByWhere(
				Schedule.class,
				"EmployeeID = " + employeeId + " and  status == 1" + " and workDate like '"
						+ DateUtils.getDateString(workDate, new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) + "%'");
	}

	@Override
	public void saveSchedules(List<Schedule> schedules) {
		if (schedules == null) {
			return;
		}

		for (int i = 0; i < schedules.size(); i++) {
			List<Schedule> ss = getDb().findAllByWhere(Schedule.class, "esid = " + schedules.get(i).getEsid());
			if (ss.size() > 0) {
				continue;
			}
			save(schedules.get(i));
		}
	}

	public List<Schedule> getSchedulesByWorkDate(int employeeId, Date workDate) {
		return getDb().findAllByWhere(
				Schedule.class,
				"employeeID = " + employeeId + " and workDate like '"
						+ DateUtils.getDateString(workDate, new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) + "%'");
	}

	public void deleteAll() {
		getDb().deleteAll(Schedule.class);
	}

	/**
	 * 获取时间范围类的排班数据
	 * 
	 * @param startDate
	 * @param endDate
	 * @param status
	 * @return
	 */
	public List<Schedule> getSchedules(int employeeId, Date startDate, Date endDate, int status) {
		return getDb().findAllByWhere(
				Schedule.class,
				"employeeID = " + employeeId + " and workDate >= '"
						+ DateUtils.getDateString(startDate, new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) + "' and workDate <= '"
						+ DateUtils.getDateString(endDate, new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) + "' and status = " + status);
	}

	public List<Schedule> getSchedules(int employeeId, Date startDate, Date endDate) {
		return getDb().findAllByWhere(
				Schedule.class,
				"employeeID = " + employeeId + " and workDate >= '"
						+ DateUtils.getDateString(startDate, new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) + "' and workDate <= '"
						+ DateUtils.getDateString(endDate, new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) + "'");
	}

	public List<Schedule> getSchedules(Date beginDate, Date endDate) {
		return getDb().findAllByWhere(
				Schedule.class,
				"workDate >= '" + DateUtils.getDateString(beginDate, new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) + "' and workDate <= '"
						+ DateUtils.getDateString(endDate, new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)) + "'");
	}
}
