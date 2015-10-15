package com.mdground.yizhida.activity.schedule;

import java.util.Date;

public interface ScheduleTablePresenter {

	public void getEmployeeList();

	/**
	 * 获取排班表
	 * 
	 * @param beginDate
	 *            起始时间
	 * @param count
	 *            数量
	 */
	public void getEmployeeScheduleList(Date beginDate, int count);

}
