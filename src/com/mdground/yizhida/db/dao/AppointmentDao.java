package com.mdground.yizhida.db.dao;

import java.util.List;

import android.content.Context;

import com.mdground.yizhida.bean.AppointmentInfo;

public class AppointmentDao extends BaseDao<AppointmentInfo> {

	private static AppointmentDao instance;

	public AppointmentDao(Context context) {
		super(context);
	}

	public static AppointmentDao getInstance(Context context) {

		if (instance == null) {
			instance = new AppointmentDao(context);
		}

		return instance;
	}
	
	/**
	 * 正序
	 * @param OPDatePeriod
	 * @return
	 */
	public List<AppointmentInfo> getAppointmentByOPDatePeriod(int OPDatePeriod) {
		return getDb().findAllByWhere(AppointmentInfo.class, "OPDatePeriod = " + OPDatePeriod, "OPNo");
	}
	
	/**
	 * 倒叙
	 * @param OPDatePeriod
	 * @return
	 */
	public List<AppointmentInfo> getAppointmentByOPDatePeriodDesc(int OPDatePeriod) {
		return getDb().findAllByWhere(AppointmentInfo.class, "OPDatePeriod = " + OPDatePeriod, "OPNo desc");
	}

	public void saveAppointment(List<AppointmentInfo> appointments) {

		if (appointments == null) {
			return;
		}

		for (int i = 0; i < appointments.size(); i++) {
			List<AppointmentInfo> ss = getDb().findAllByWhere(AppointmentInfo.class, "id = " + appointments.get(i).getId());
			if (ss.size() > 0) {
				continue;
			}
			save(appointments.get(i));
		}
	}
	
	public void deleteAll() {
		getDb().deleteAll(AppointmentInfo.class);
	}

}
